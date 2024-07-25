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
            :label-width="103"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-form-item label="番号">
              <n-input v-model:value="formValue.carNo" :disabled="true" />
            </n-form-item>
            <n-form-item label="前回点検（年）" path="lastYearCheckDate">
              <div class="items-center flex">
                <n-date-picker v-model:value="formValue.lastYearCheckDate" type="date" clearable />
              </div>
            </n-form-item>
            <n-form-item label="前回点検" path="lastMonthCheckDate">
              <n-date-picker v-model:value="formValue.lastMonthCheckDate" type="date" clearable />
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
  carNo: '',
  lastYearCheckDate: undefined,
  lastMonthCheckDate: undefined
});
const user = ref<MyModel.User>();
const rules = {};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.CarCheck | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id ?? '';
    formValue.value.carNo = row?.carNo ?? '';
    formValue.value.lastMonthCheckDate = row?.lastYearCheckDate ?? undefined;
    formValue.value.lastYearCheckDate = row?.lastYearCheckDate ?? undefined;
  } else {
    user.value = undefined;
    formValue.value = {
      id: '',
      carNo: '',
      lastYearCheckDate: undefined,
      lastMonthCheckDate: undefined
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
  setLastDate: `/car/check/setLastDate`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.CarCheck = {
        id: formValue.value.id,
        lastYearCheckDate: formValue.value.lastYearCheckDate,
        lastMonthCheckDate: formValue.value.lastMonthCheckDate
      };
      const promise = request.post<Boolean>(`${urls.setLastDate}`, params);
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
