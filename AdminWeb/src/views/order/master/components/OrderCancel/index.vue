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
      <n-spin :show="loading">
        <n-form ref="formRef" label-placement="left" :label-width="120" :model="formValue" :rules="rules" :size="size">
          <n-form-item path="companyRemark" label="会社備考">
            <n-input
              v-model:value="formValue.companyRemark"
              :autosize="{
                minRows: 3,
                maxRows: 3
              }"
              placeholder="キャンセル理由を説明してください"
              type="textarea"
            />
          </n-form-item>
        </n-form>
        <n-divider />
        <n-space justify="center">
          <n-button type="primary" ghost @click="close">Cancel</n-button>
          <n-button type="primary" @click="handleValidateClick">保存</n-button>
        </n-space>
      </n-spin>
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
const formValue = ref({
  id: '',
  companyRemark: ''
});

const rules = {
  companyRemark: {
    required: true,
    message: 'キャンセル理由を説明してください'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (id: string) => {
  formValue.value.id = id;
  showModalRef.value = true;
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
  cancel: `/order/cancel`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params = {
        id: formValue.value.id,
        companyRemark: formValue.value.companyRemark
      };
      const promise = request.put<Boolean>(`${urls.cancel}`, params);
      loadingBar.start();
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('キャンセルしました');
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
