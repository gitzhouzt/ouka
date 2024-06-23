<template>
	<div style="margin-top: 20px">
		<van-form @submit="onSubmit">
			<van-cell-group inset>
				<van-field v-model="modelRef.carNo" label="車両番号" readonly />
				<file-upload class="m-4" :max="4" file-key="files/advance" @finish="onFinish" />
				<van-field v-model="formValue.remark" name="remark" label="備考" type="textarea" rows="3" maxlength="200"
					show-word-limit />
			</van-cell-group>
			<div style="margin: 16px">
				<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
					報告
				</van-button>
			</div>
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';

const modelRef = ref<MyModel.Car>({
	id: '',
});
const formValue = ref<MyModel.CarDamage>({
	id: '',
});

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const images = ref<any>([]);
const route = useRouter();
const urls = {
	report: '/driver/car/report',
	upload: '/common/upload',
};
const onload = () => {
	const jsonStr: any = route.currentRoute.value.query.item;
	modelRef.value = JSON.parse(decodeURIComponent(jsonStr));
};

const toCar = () => {
	routerPush({
		path: '/car/list',
	});
};
const onSubmit = () => {
	const params = {
		carId: modelRef.value?.id,
		carNo: modelRef.value?.carNo,
		image1: images.value[0],
		image2: images.value.length > 1 ? images.value[1] : '',
		image3: images.value.length > 2 ? images.value[2] : '',
		image4: images.value.length > 3 ? images.value[3] : '',
		remark: formValue.value.remark,
	};
	const promise = request.post<Boolean>(`${urls.report}`, params);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				toCar();
				showNotify({
					type: 'success',
					message: '報告しました',
				});
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

const onFinish = (fileUrl: string) => {
	if (!fileUrl) {
		images.value = [];
		return;
	}
	images.value.push(fileUrl);
};
onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
