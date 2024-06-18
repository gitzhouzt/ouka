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
          <n-form ref="formRef" label-placement="left" :label-width="80" :model="formValue" :rules="rules" :size="size">
            <n-form-item label="分類" path="dictName">
              <n-input v-model:value="formValue.dictName" />
            </n-form-item>
            <n-form-item label="コード" path="dictCode">
              <n-input v-model:value="formValue.dictCode" />
            </n-form-item>
            <n-form-item label="備考" path="remark">
              <n-input-group>
                <n-input v-model:value="formValue.remark" placeholder="備考" />
              </n-input-group>
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
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.Dict>({
  id: '',
  dictName: '',
  dictCode: '',
  remark: ''
});
const rules = {
  dictName: {
    required: true,
    message: '1-20文字まで入力してください',
    trigger: 'input',
    max: 20
  },
  dictCode: {
    required: true,
    message: '1-20文字まで入力してください',
    trigger: 'input',
    max: 20
  }
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Dict | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    formValue.value.dictName = row?.dictName;
    formValue.value.dictCode = row?.dictCode;
    formValue.value.remark = row?.remark;
  } else {
    formValue.value = {
      id: '',
      dictName: '',
      dictCode: '',
      remark: ''
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
  addDict: `base/dict/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.Dict = {
        id: formValue.value.id,
        dictName: formValue.value.dictName,
        dictCode: formValue.value.dictCode,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.addDict}`, params);
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
