<template>
	<div style="margin-top: 20px">
		<van-form @submit="onSubmit">
			<van-cell-group inset :title="`${moment().format('yyyy-MM-DD')}`">
				<van-field v-model="formValue.start" name="start" label="出发地" placeholder="入力してください" />
				<van-field v-model="formValue.end" name="end" label="目的地" placeholder="入力してください" />
				<van-field v-model="formValue.distance" name="distance" label="距离(km)" type="number" placeholder="入力してください" />
				<van-field v-model="formValue.jissha" name="distance" label="实车(km)" type="number" placeholder="入力してください" />
				<van-field v-model="formValue.kaisou" name="kaisou" label="回送(km)" type="number" placeholder="入力してください" />
				<van-field v-model="formValue.rest" name="rest" label="休息时间段" type="number" placeholder="入力してください" />
				<van-field v-model="formValue.restNum" name="restNum" label="休息数" type="number" placeholder="入力してください" />
				<van-field v-model="formValue.remark" name="remark" label="备注" type="textarea" rows="3" maxlength="200"
					show-word-limit />
			</van-cell-group>
			<div style="margin: 16px">
				<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
					保存
				</van-button>
			</div>
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';

const formValue = ref<MyModel.LogRecord>({
	id: '',
});

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const urls = {
	record: '/driver/user/writeRecord',
};
const onload = () => {
	//
};

const toHome = () => {
	routerPush({
		path: '/home',
	});
};
const onSubmit = () => {
	const params = {
		date: moment().format('yyyy-MM-DD'),
		start: formValue.value?.start,
		startTime: formValue.value?.startTime,
		end: formValue.value?.end,
		endTime: formValue.value?.endTime,
		distance: Number(formValue.value?.distance),
		jissha: Number(formValue.value?.jissha),
		kaisou: Number(formValue.value?.kaisou),
		rest: formValue.value?.rest,
		restNum: formValue.value?.restNum,
		remark: formValue.value?.remark,
	};
	const promise = request.post<Boolean>(`${urls.record}`, params);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				toHome();
				showNotify({
					type: 'success',
					message: '保存しました',
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

onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
