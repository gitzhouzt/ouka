<template>
	<div>
		<div style="margin-top: 20px">
			<van-form @submit="onSubmit">
				<van-cell-group inset>
					<van-field v-model="model.loginName" name="loginName" label="ID" placeholder="IDを入力"
						:rules="[{ required: true, message: '入力してください' }]" />
					<van-field v-model="model.loginPass" type="password" name="loginPass" label="PASS" placeholder="パスワードを入力"
						:rules="[{ required: true, message: '入力してください' }]" />
				</van-cell-group>
				<div style="margin: 16px">
					<van-button block color="#fb8c00" size="large" native-type="submit" :loading="auth.loginLoading">
						OK
					</van-button>
				</div>
			</van-form>
		</div>
	</div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useAuthStore } from '@/store';

const auth = useAuthStore();
const { login } = useAuthStore();
const model = reactive({
	loginName: import.meta.env.DEV ? 'huang@cbsdata.co.jp' : '',
	loginPass: import.meta.env.DEV ? 'abcd@6789' : '',
});

function onSubmit() {
	const { loginName, loginPass } = model;
	login(loginName, loginPass);
}
</script>
<style scoped></style>
