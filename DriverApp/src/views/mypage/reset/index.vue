<template>
	<div class="mt-10px">
		<van-form @submit="handleSubmit">
			<van-cell-group inset>
				<van-field v-model="model.pwd" name="pwd" label="現在" type="password" placeholder="現在パスワードを入力"
					:rules="[{ required: true, message: '入力してください' }]" />
				<van-field v-model="model.newPwd" name="newPwd" label="新しい" type="password" placeholder="新しいパスワードを入力"
					:rules="[{ required: true, message: '入力してください' }]" />
				<van-field v-model="model.confirmPwd" name="confirmPwd" type="password" label="確認" placeholder="新しいパスワード確認"
					:rules="[{ required: true, message: '入力してください' }]" /></van-cell-group>
			<div style="margin: 16px">
				<van-button block size="large" color="#fb8c00" native-type="submit" :loading="loading">
					更新
				</van-button>
			</div>
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { showNotify } from 'vant';
import { useAuthStore } from '@/store';
import { fetchLogout } from '@/service/api';
import { request } from '@/service/request';

const loading = ref<boolean>(false);

const urls = {
	resetPwd: '/user/resetPwd',
};
const model = ref<MyModel.UserPwd>({
	pwd: '',
	newPwd: '',
	confirmPwd: '',
});

const auth = useAuthStore();
function handleSubmit() {
	const params: any = {
		pwd: model.value.pwd,
		newPwd: model.value.newPwd,
		confirmPwd: model.value.confirmPwd,
	};
	const promise = request.post<Boolean>(`${urls.resetPwd}`, params);
	loading.value = true;
	promise
		.then(async (res) => {
			if (res.data) {
				showNotify({
					type: 'success',
					message: '変更しました。再度ログインしてください。',
				});
				await fetchLogout();
				auth.resetAuthStore();
			}
		})
		.catch((err) => {
			showNotify({
				type: 'danger',
				message: err,
			});
		})
		.finally(() => {
			loading.value = false;
		});
}
</script>

<style lang="scss"></style>
