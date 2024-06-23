<template>
	<div class="pb-20">
		<van-skeleton title :row="10" :loading="loading">
			<van-form>
				<van-cell-group inset title="ー 車両情報 ー">
					<van-cell>
						<div class="flex justify-around m-1">
							<van-button color="#fb8c00" @click="toReport">
								キズ報告
							</van-button>
						</div>
					</van-cell>
					<van-cell title="車両番号">{{ modelRef.carNo }}</van-cell>
					<van-cell title="タイプ">{{ modelRef.carType }}</van-cell>
					<van-cell title="ナンバー">{{ modelRef.plateNum }}</van-cell>
					<van-cell title="座席数">{{ modelRef.carSeat + '座' }}</van-cell>
					<van-cell title="備考">
						<template #label>
							{{ modelRef.remark }}
						</template>
					</van-cell>
				</van-cell-group>
				<van-cell-group v-if="carDamageRef.length > 0" inset title="ー 车伤 ー">
					<van-cell v-for="d in carDamageRef" :key="d.id" :title="d.createTime">
						<template #label>
							<div class="flex justify-around">
								<van-image v-if="d.image1" width="100" height="100" :src="`${envConfig.static}${d.image1}`" />
								<van-image v-if="d.image2" width="100" height="100" :src="`${envConfig.static}${d.image2}`" />
								<van-image v-if="d.image3" width="100" height="100" :src="`${envConfig.static}${d.image3}`" />
								<van-image v-if="d.image4" width="100" height="100" :src="`${envConfig.static}${d.image4}`" />
							</div>
						</template>
					</van-cell>
				</van-cell-group>
			</van-form>
		</van-skeleton>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const envConfig = getEnvConfig(import.meta.env);

const modelRef = ref<MyModel.Car>({
	id: '',
});
const carDamageRef = ref<MyModel.CarDamage[]>([
	{
		id: '',
	},
]);

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const route = useRouter();

const urls = {
	details: '/driver/car/details/',
	report: '/driver/car/report',
};
const onload = () => {
	loading.value = true;
	const { id } = route.currentRoute.value.query;
	console.log(id);

	request
		.get<MyModel.CarDetails>(`${urls.details}${id}`)
		.then((res) => {
			if (res.data) {
				const { data } = res;
				modelRef.value = data.carVO;
				carDamageRef.value = data.carDamageVOs;
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

const toReport = () => {
	const jsonStr = JSON.stringify(modelRef.value);
	routerPush({
		path: '/car/report',
		query: { item: encodeURIComponent(jsonStr) },
	});
};
onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
