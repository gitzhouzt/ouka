<template>
	<div>
		<van-form>
			<van-cell-group inset>
				<van-cell>
					<div class="flex justify-around m-1">
						<van-button color="#fb8c00" to="/mypage/reset">
							パスワード変更
						</van-button>
						<van-button plain type="danger" @click="logout">
							ログアウト
						</van-button>
					</div>
				</van-cell>
				<van-cell title="番号">{{ modelRef.userNo }}</van-cell>
				<van-cell title="ドライバータイプ">{{
							modelRef.driverType ?? '-'
						}}</van-cell>
				<van-cell title="運転免許番号">{{
								modelRef.driverLicense ?? '-'
							}}</van-cell>
				<van-cell title="免許有効期限">{{
								modelRef.expiryDate ?? '-'
							}}</van-cell>
				<van-cell title="前回健康診断日">{{
								modelRef.healthyDate ?? '-'
					}}</van-cell>
			</van-cell-group>
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import { useAuthStore } from '@/store';
import { request } from '@/service/request';
import { fetchLogout } from '@/service/api';

const modelRef = ref<MyModel.UserDriver>({
	id: '',
	userId: '',
	userNo: '',
});

const loading = ref<boolean>(false);

const urls = {
	details: '/driver/user/details',
};
const onload = () => {
	request
		.get<MyModel.UserDriver>(`${urls.details}`)
		.then((res) => {
			if (res.data) {
				const { data } = res;
				modelRef.value = data;
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
};
const auth = useAuthStore();
const logout = async () => {
	await fetchLogout();
	auth.resetAuthStore();
};

onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
