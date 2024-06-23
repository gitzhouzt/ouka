<template>
  <div>
    <n-modal
      v-model:show="showModalRef"
      :style="bodyStyleRef"
      transform-origin="center"
      :mask-closable="false"
      preset="card"
      :title="titleRef"
      closable
      @update-show="onUpdateShow"
    >
      <n-card size="huge" role="dialog" aria-modal="true">
        <n-spin :show="loading">
          <n-form
            ref="formRef"
            label-placement="left"
            :label-width="100"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-form-item label="番号">
              <n-input v-model:value="formValue.carNo" :disabled="true" />
            </n-form-item>
            <n-form-item label="年度" path="trainYear">
              <n-date-picker v-model:value="formValue.year" type="year" clearable :disabled="true" />
            </n-form-item>
            <n-form-item label="年検" path="yearCheckDate">
              <div class="items-center flex">
                <n-date-picker v-model:value="formValue.yearCheckDate" type="date" clearable />
                <n-switch v-model:value="formValue.yearCheckStatus" class="ml-2">
                  <template #checked> 点検済 </template>
                  <template #unchecked> なし </template>
                </n-switch>
              </div>
            </n-form-item>
            <n-form-item label="第一回点検" path="monthCheckDate1">
              <n-date-picker v-model:value="formValue.monthCheckDate1" type="date" clearable />
              <n-switch v-model:value="formValue.checkStatus1" class="ml-2">
                <template #checked> 点検済 </template>
                <template #unchecked> なし </template>
              </n-switch>
            </n-form-item>
            <n-form-item label="第二回点検" path="monthCheckDate2">
              <n-date-picker v-model:value="formValue.monthCheckDate2" type="date" clearable />
              <n-switch v-model:value="formValue.checkStatus2" class="ml-2">
                <template #checked> 点検済 </template>
                <template #unchecked> なし </template>
              </n-switch>
            </n-form-item>
            <n-form-item label="第三回点検" path="monthCheckDate3">
              <n-date-picker v-model:value="formValue.monthCheckDate3" type="date" clearable />
              <n-switch v-model:value="formValue.checkStatus3" class="ml-2">
                <template #checked> 点検済 </template>
                <template #unchecked> なし </template>
              </n-switch>
            </n-form-item>
          </n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import moment from 'moment';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.CarCheck>({
  id: '',
  carId: '',
  carNo: '',
  year: '',
  yearCheckDate: undefined,
  yearCheckStatus: undefined,
  monthCheckDate1: undefined,
  monthCheckDate2: undefined,
  monthCheckDate3: undefined,
  checkStatus1: undefined,
  checkStatus2: undefined,
  checkStatus3: undefined
});
const user = ref<MyModel.User>();
const rules = {};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.CarCheck | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id ?? '';
    formValue.value.carId = row?.carId;
    formValue.value.carNo = row?.carNo;
    formValue.value.year = row?.year ? moment().set('year', Number(row.year)).valueOf() : new Date().valueOf();
    formValue.value.yearCheckDate = row?.yearCheckDate ? new Date(row?.yearCheckDate).valueOf() : undefined;
    formValue.value.monthCheckDate1 = row?.monthCheckDate1 ? new Date(row?.monthCheckDate1).valueOf() : undefined;
    formValue.value.monthCheckDate2 = row?.monthCheckDate2 ? new Date(row?.monthCheckDate2).valueOf() : undefined;
    formValue.value.monthCheckDate3 = row?.monthCheckDate3 ? new Date(row?.monthCheckDate3).valueOf() : undefined;

    formValue.value.yearCheckStatus = row?.yearCheckStatus;
    formValue.value.checkStatus1 = row?.checkStatus1;
    formValue.value.checkStatus2 = row?.yearCheckStatus;
    formValue.value.checkStatus3 = row?.checkStatus3;
  } else {
    user.value = undefined;
    formValue.value = {
      id: '',
      carId: '',
      carNo: '',
      year: '',
      yearCheckDate: undefined,
      yearCheckStatus: undefined,
      monthCheckDate1: undefined,
      monthCheckDate2: undefined,
      monthCheckDate3: undefined,
      checkStatus1: undefined,
      checkStatus2: undefined,
      checkStatus3: undefined
    };
  }
};

const close = () => {
  emits('close');
  showModalRef.value = false;
};
const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};

const urls = {
  addOrEdit: `/car/check/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.CarCheck = {
        id: formValue.value.id,
        carId: formValue.value.carId,
        carNo: formValue.value.carNo,
        year: moment(formValue.value.year).format('yyyy'),
        yearCheckDate: formValue.value.yearCheckDate,
        yearCheckStatus: formValue.value.yearCheckStatus,
        monthCheckDate1: formValue.value.monthCheckDate1,
        monthCheckDate2: formValue.value.monthCheckDate2,
        monthCheckDate3: formValue.value.monthCheckDate3,
        checkStatus1: formValue.value.checkStatus1,
        checkStatus2: formValue.value.checkStatus2,
        checkStatus3: formValue.value.checkStatus3
      };
      const promise = request.post<Boolean>(`${urls.addOrEdit}`, params);
      loadingBar.start();
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('保存しました');
            close();
          }
        })
        .catch(err => {
          message.warning(err);
        })
        .finally(() => {
          loading.value = false;
          loadingBar.finish();
        });
    }
  });
};

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
