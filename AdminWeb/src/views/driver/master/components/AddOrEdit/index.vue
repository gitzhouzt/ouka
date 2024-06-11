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
            <n-form-item label="ドライバータイプ" path="driverType">
              <n-input
                v-model:value="formValue.driverType"
                placeholder="クリックタイプを選択"
                readonly
                @click="showDict('driver_type')"
              />
            </n-form-item>
            <n-form-item label="免許番号" path="driverLicense">
              <n-input v-model:value="formValue.driverLicense" />
            </n-form-item>
            <n-form-item label="免許タイプ" path="licenseType">
              <n-input
                v-model:value="formValue.licenseType"
                placeholder="クリックタイプを選択"
                readonly
                @click="showDict('driver_license_type')"
              />
            </n-form-item>
            <n-form-item label="有効期限日" path="expiryDate" required>
              <n-date-picker v-model:value="formValue.expiryDate" format="yyyy/MM/dd" type="date" clearable />
            </n-form-item>
            <n-form-item label="健康診断日" path="healthyDate" required>
              <n-date-picker v-model:value="formValue.healthyDate" type="date" format="yyyy/MM/dd" clearable />
            </n-form-item>
          </n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
        <dict-select-modal ref="dictModal" @click="selectDict" />
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.Driver>({
  id: '',
  userId: '',
  userName: '',
  userNo: '',
  userVO: undefined,
  driverLicense: '',
  licenseType: '',
  licenseTypeCode: '',
  driverType: '',
  driverTypeCode: '',
  expiryDate: new Date().valueOf(),
  healthyDate: new Date().valueOf()
});
const user = ref<MyModel.User>();
const rules = {
  driverType: {
    required: true,
    message: '選択してください',
    trigger: 'input'
  },
  licenseType: {
    required: true,
    message: '選択してください',
    trigger: 'input'
  },
  driverLicense: {
    required: true,
    message: '入力してください',
    trigger: 'input'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Driver | undefined) => {
  showModalRef.value = true;
  if (row) {
    user.value = row.userVO;
    formValue.value.id = row?.id ?? '';
    formValue.value.userId = row?.userId;
    formValue.value.userNo = row?.userNo;
    formValue.value.userName = row?.userName;
    formValue.value.driverLicense = row?.driverLicense;
    formValue.value.licenseType = row?.licenseType ?? '';
    formValue.value.licenseTypeCode = row?.licenseTypeCode ?? '';
    formValue.value.driverType = row?.driverType ?? '';
    formValue.value.driverTypeCode = row?.driverTypeCode ?? '';
    formValue.value.expiryDate = row?.expiryDate ? new Date(row?.expiryDate).valueOf() : new Date().valueOf();
    formValue.value.healthyDate = row?.healthyDate ? new Date(row?.healthyDate).valueOf() : new Date().valueOf();
  } else {
    user.value = undefined;
    formValue.value = {
      id: '',
      userId: '',
      userNo: '',
      userName: '',
      userVO: undefined,
      driverLicense: '',
      licenseType: '',
      licenseTypeCode: '',
      driverType: '',
      driverTypeCode: '',
      expiryDate: new Date().valueOf(),
      healthyDate: new Date().valueOf()
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
  addOrEdit: `driver/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.Driver = {
        id: formValue.value.id,
        userId: formValue.value.userId,
        userNo: user.value?.userNo,
        userName: user.value?.userName,
        driverLicense: formValue.value.driverLicense,
        licenseType: formValue.value.licenseType,
        licenseTypeCode: formValue.value.licenseTypeCode,
        driverType: formValue.value.driverType,
        driverTypeCode: formValue.value.driverTypeCode,
        expiryDate: formValue.value.expiryDate,
        healthyDate: formValue.value.healthyDate
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
const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'driver_license_type':
      formValue.value.licenseType = result.text;
      formValue.value.licenseTypeCode = result.value;
      break;
    case 'driver_type':
      formValue.value.driverType = result.text;
      formValue.value.driverTypeCode = result.value;
      break;
    default:
      break;
  }
};
defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
