<template>
	<n-form ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
		<n-form-item path="userName">
			<n-input v-model:value="model.userName" placeholder="ユーザー名またはメールアドレス" />
		</n-form-item>
		<n-form-item path="password">
			<n-input v-model:value="model.password" type="password" show-password-on="click" placeholder="パスワード" />
		</n-form-item>
		<n-space :vertical="true" :size="24">
			<div class="flex-y-center justify-between">
				<n-checkbox v-if="false" v-model:checked="rememberMe">覚えてます</n-checkbox>
				<n-button v-if="false" :text="true" @click="toLoginModule('reset-pwd')">パスワードをお忘れた場合</n-button>
			</div>
			<n-button type="primary" size="large" :block="true" :round="true" :loading="auth.loginLoading"
				@click="handleSubmit">
				ログイン
			</n-button>
		</n-space>
	</n-form>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInst, FormRules } from 'naive-ui';
import { useAuthStore } from '@/store';
import { useRouterPush } from '@/composables';
import { formRules } from '@/utils';

const auth = useAuthStore();
const { login } = useAuthStore();
const { toLoginModule } = useRouterPush();

const formRef = ref<(HTMLElement & FormInst) | null>(null);
const model = reactive({
	userName: import.meta.env.DEV ? 'huang@cbsdata.co.jp' : '',
	password: import.meta.env.DEV ? 'abcd@6789' : ''
});
const rules: FormRules = {
	password: formRules.pwd
};
const rememberMe = ref(false);

function handleSubmit(e: MouseEvent) {
	if (!formRef.value) return;
	e.preventDefault();

	formRef.value.validate(errors => {
		if (!errors) {
			const { userName, password } = model;
			login(userName, password);
		}
	});
}
</script>
<style scoped></style>
