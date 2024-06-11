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
              <n-input v-model:value="user.userNo" :disabled="true" />
            </n-form-item>
            <n-form-item label="名前">
              <n-input v-model:value="user.userName" :disabled="true" />
            </n-form-item>
            <n-form-item label="年度" path="trainYear">
              <n-date-picker v-model:value="formValue.trainYear" type="year" clearable :disabled="true" />
            </n-form-item>
            <n-form-item label="第一回" path="trainDate1">
              <n-date-picker v-model:value="formValue.trainDate1" type="date" clearable />
            </n-form-item>
            <n-form-item label="第二回" path="trainDate2">
              <n-date-picker v-model:value="formValue.trainDate2" type="date" clearable />
            </n-form-item>
            <n-form-item label="第三回" path="trainDate3">
              <n-date-picker v-model:value="formValue.trainDate3" type="date" clearable />
            </n-form-item>
            <n-form-item label="第四回" path="trainDate4">
              <n-date-picker v-model:value="formValue.trainDate4" type="date" clearable />
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
const formValue = ref<MyModel.Train>({
  id: '',
  userId: '',
  userVO: undefined,
  trainYear: new Date().valueOf(),
  trainDate1: undefined,
  trainDate2: undefined,
  trainDate3: undefined,
  trainDate4: undefined
});
const user = ref<MyModel.User>();
const rules = {};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Train | undefined) => {
  showModalRef.value = true;
  if (row) {
    user.value = row.userVO;
    formValue.value.id = row?.id ?? '';
    formValue.value.userId = row?.userId;
    formValue.value.trainYear = row?.trainYear ? moment().set('year', row.trainYear).valueOf() : new Date().valueOf();
    formValue.value.trainDate1 = row?.trainDate1 ? new Date(row?.trainDate1).valueOf() : undefined;
    formValue.value.trainDate2 = row?.trainDate2 ? new Date(row?.trainDate2).valueOf() : undefined;
    formValue.value.trainDate3 = row?.trainDate3 ? new Date(row?.trainDate3).valueOf() : undefined;
    formValue.value.trainDate4 = row?.trainDate4 ? new Date(row?.trainDate4).valueOf() : undefined;
  } else {
    user.value = undefined;
    formValue.value = {
      id: '',
      userId: '',
      userVO: undefined,
      trainYear: new Date().valueOf(),
      trainDate1: undefined,
      trainDate2: undefined,
      trainDate3: undefined,
      trainDate4: undefined
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
  addOrEdit: `train/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.Train = {
        id: formValue.value.id,
        userId: formValue.value.userId,
        trainYear: moment(formValue.value.trainDate4).format('yyyy'),
        trainDate1: formValue.value.trainDate1,
        trainDate2: formValue.value.trainDate2,
        trainDate3: formValue.value.trainDate3,
        trainDate4: formValue.value.trainDate4
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
