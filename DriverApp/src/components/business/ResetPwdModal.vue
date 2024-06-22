<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable>
			<n-card class="h-full shadow-sm rounded-16px">
				<n-spin :show="loading">
					<n-form ref="formRef" label-placement="left" :label-width="160" :model="model" :rules="rules"
						:size="size">
						<n-form-item path="pwd" label="現在パスワード">
							<n-input v-model:value="model.pwd" type="password" placeholder="現在パスワード" />
						</n-form-item>
						<n-form-item path="newPwd" label="新しいパスワード">
							<n-input v-model:value="model.newPwd" type="password" placeholder="新しいパスワード" />
						</n-form-item>
						<n-form-item path="confirmPwd" label="新しいパスワード確認">
							<n-input v-model:value="model.confirmPwd" type="password" placeholder="新しいパスワード確認" />
						</n-form-item>
					</n-form>
					<n-divider />
					<van-space justify="center">
						<n-button color="#fb8c00" @click="handleSubmit">更新</n-button>
					</van-space>
				</n-spin>
			</n-card>
		</n-modal>
	</div>
</template>

<script setup lang="ts">
import { reactive, ref, toRefs } from 'vue';
import { useAuthStore } from '@/store';
import { formRules, getConfirmPwdRule } from '@/utils';
import { request } from '@/service/request';
import { fetchLogout } from '@/service/api';

const bodyStyleRef = ref({
	width: '600px',
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = () => {
	showModalRef.value = true;
};
const titleRef = ref<string | undefined>('パスワード変更');
const setTitle = (title: string) => {
	titleRef.value = title;
};
const close = () => {
	showModalRef.value = false;
};

const formRef = ref<any | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const loading = ref<boolean>(false);

const model = reactive({
	pwd: '',
	newPwd: '',
	confirmPwd: '',
});
const rules: any = {
	pwd: formRules.pwd,
	newPwd: formRules.pwd,
	confirmPwd: getConfirmPwdRule(toRefs(model).newPwd),
};

const urls = {
	resetPwd: `/sys/user/resetPwd`,
};

const auth = useAuthStore();
function handleSubmit(e: MouseEvent) {
	if (!formRef.value) return;
	e.preventDefault();
	formRef.value.validate((errors: any) => {
		if (!errors) {
			const params: any = {
				pwd: model.pwd,
				newPwd: model.newPwd,
				confirmPwd: model.confirmPwd,
			};
			const promise = request.post<Boolean>(`${urls.resetPwd}`, params);
			// loadingBar.start();
			// loading.value = true;
			promise
				.then(async (res: any) => {
					if (res.data) {
						// message.success(
						// 	'パスワードを変更しました。再度ログインしてください。'
						// );
						await fetchLogout();
						auth.resetAuthStoreToHome();
						close();
					}
				})
				.catch(() => {
					//
				})
				.finally(() => {
					loading.value = false;
				});
		}
	});
}
defineExpose({
	showModal,
	setTitle,
	close,
});
</script>

<style scoped>

</style>
