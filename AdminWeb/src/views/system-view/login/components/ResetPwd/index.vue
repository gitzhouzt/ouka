<template>
  <n-form ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <n-form-item path="mail">
      <n-input v-model:value="model.mail" placeholder="メールアドレス" />
    </n-form-item>
    <n-form-item path="code">
      <div class="flex-y-center w-full">
        <n-input v-model:value="model.code" placeholder="確認コード" />
        <div class="w-18px"></div>
        <n-button size="large" :disabled="isCounting" :loading="smsLoading" @click="handleSmsCode">
          {{ label }}
        </n-button>
      </div>
    </n-form-item>
    <n-form-item path="pwd">
      <n-input v-model:value="model.pwd" placeholder="パスワード" />
    </n-form-item>
    <n-form-item path="confirmPwd">
      <n-input v-model:value="model.confirmPwd" placeholder="パスワード（確認）" />
    </n-form-item>
    <n-space :vertical="true" size="large">
      <n-button type="primary" size="large" :block="true" :round="true" @click="handleSubmit">変更</n-button>
      <n-button size="large" :block="true" :round="true" @click="toLoginModule('pwd-login')">戻る</n-button>
    </n-space>
  </n-form>
</template>

<script lang="ts" setup>
import { reactive, ref, toRefs } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { useRouterPush } from '@/composables';
import { useSmsCode } from '@/hooks';
import { formRules, getConfirmPwdRule } from '@/utils';

const { toLoginModule } = useRouterPush();
const { label, isCounting, loading: smsLoading, start } = useSmsCode();

const formRef = ref<(HTMLElement & FormInst) | null>(null);
const model = reactive({
  mail: '',
  code: '',
  pwd: '',
  confirmPwd: ''
});
const rules: FormRules = {
  mail: formRules.mail,
  code: formRules.code,
  pwd: formRules.pwd,
  confirmPwd: getConfirmPwdRule(toRefs(model).pwd)
};

function handleSmsCode() {
  start();
}

function handleSubmit(e: MouseEvent) {
  if (!formRef.value) return;
  e.preventDefault();

  formRef.value.validate(errors => {
    if (!errors) {
      window.$message?.success('変更しました');
    } else {
      window.$message?.error('失敗');
    }
  });
}
</script>
<style scoped></style>
