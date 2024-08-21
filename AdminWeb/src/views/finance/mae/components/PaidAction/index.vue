<template>
  <div>
    <n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
      preset="card" :title="titleRef" closable @update-show="onUpdateShow">
      <n-spin :show="loading">
        <n-form ref="formRef" label-placement="left" :label-width="80" :model="formValue" :rules="rules" :size="size">
          <n-grid :cols="2">
            <n-gi :span="2">
              <n-form-item label="支払方法" path="payMethod">
                <n-input v-model:value="formValue.payMethod" placeholder="クリック方法を選択" readonly
                  @click="showDict('pay_method')" />
              </n-form-item>
            </n-gi>
            <n-gi v-if="formValue.payMethod.indexOf('口座') >= 0" :span="2">
              <n-form-item label="金融機関" path="bank">
                <n-input v-model:value="formValue.bank" placeholder="クリック機関を選択" readonly
                  @click="showDict('pay_bank')" /></n-form-item>
            </n-gi>
          </n-grid>
        </n-form>
        <n-divider />
        <n-space justify="center">
          <n-button type="primary" ghost @click="close">Cancel</n-button>
          <n-button type="primary" @click="handleValidateClick">保存</n-button>
        </n-space>
      </n-spin>
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, NButton } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});

const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
// const labelStyle = ref({ width: '20%' });
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref(false);
const modelRef = ref<Array<string | number>>([]);
const showModalRef = ref<boolean | undefined>(false);
const formValue = ref<MyModel.PayRecord>({
  id: '',
  payMethod: '',
  payMethodCode: '',
  bank: ''
});
const rules = {
  payMethod: {
    required: true,
    message: '入力してください'
  }
};

const urls = {
  paid: `/order/paid`
};

const showModal = (ids: Array<string | number>) => {
  modelRef.value = ids;
  showModalRef.value = true;
};

const close = () => {
  emits('close');
  showModalRef.value = false;
};
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params = {
        ids: modelRef.value,
        bank: formValue.value.bank,
        payMethod: formValue.value.payMethod,
        payMethodCode: formValue.value.payMethodCode
      };
      const promise = request.post<Boolean>(`${urls.paid}`, params);
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('決算しました');
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

const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'pay_method':
      formValue.value.payMethod = result.text;
      formValue.value.payMethodCode = result.value;
      break;
    case 'pay_bank':
      formValue.value.bank = result.text;
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
