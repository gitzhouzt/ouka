<template>
	<div>
		<div class="m-5">
			<van-grid>
				<van-grid-item icon="orders-o" :badge="driverRef.orderCnt" text="配車注文" @click="toOrder" />
				<van-grid-item icon="location-o" text="チェックイン" @click="toCheck" />
				<van-grid-item icon="records-o" text="仕事日記" @click="toLogList" />
				<van-grid-item icon="user-o" text="マイページ" @click="toMyPage" />
			</van-grid>
		</div>
		<div class="m-5">
			<van-skeleton title :row="10" :loading="loading">
				<van-cell-group :title="driverRef.today">
					<!-- <van-cell :title="driverRef.today"> </van-cell> -->
					<van-cell is-link @click="toMyPage">
						<template #title>
							{{ `${driverRef.userName}(${driverRef.userNo})` }}
						</template>
					</van-cell>
					<van-cell is-link @click="toOrder">
						<!-- <template #value>
						{{ driverRef.orderCnt + ' 单' }}
					</template> -->
						<template #title>
							<div>オーダー</div>
							<div>
								{{
					`今日：${showOrderCnt('today', -1)}件，${showOrderCnt(
						'today',
						6
					)}件完了`
				}}
							</div>
							<div>
								{{ `明日：${showOrderCnt('', -1)}件` }}
							</div>
						</template>
					</van-cell>
					<van-cell is-link @click="toCar">
						<template #title>
							<div>車両番号</div>
							<div>
								{{ `今日：${showCar('today')}` }}
							</div>
							<div>
								{{ `明日：${showCar('')}` }}
							</div>
						</template>
					</van-cell>
				</van-cell-group>
			</van-skeleton>
			<van-skeleton title :row="5" :loading="loading">
				<van-cell-group title="今日必做">
					<van-cell title="乗務前点呼" is-link @click="toCall('am')">
						<template #value>
							<span :class="`${driverRef.amCall === 0
						? 'text-red'
						: driverRef.amCall === 1
							? ''
							: 'text-red'
					}`">
								{{
					driverRef.amCall === 0
						? '未完成'
						: driverRef.amCall === 1
							? '完成'
							: '異常'
				}}</span>
						</template></van-cell>
					<van-cell title="乗務後点呼" is-link @click="toCall('pm')">
						<template #value>
							<span :class="`${driverRef.pmCall === 0
						? 'text-red'
						: driverRef.pmCall === 1
							? ''
							: 'text-red'
					}`">
								{{
					driverRef.pmCall === 0
						? '未完成'
						: driverRef.pmCall === 1
							? '完成'
							: '異常'
				}}
							</span>
						</template>
					</van-cell>
					<van-cell title="仕事日記" is-link @click="toLog">{{
					driverRef.log ?? 0 > 1 ? '提出済' : '未提出'
				}}</van-cell>
				</van-cell-group></van-skeleton>
		</div>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { showNotify, showToast } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';

const loading = ref<boolean>(false);
const urls = {
	todayOrders: '/driver/todayOrders',
};

const driverRef = ref<MyModel.Today>({
	id: '',
	userName: '',
	userNo: '',
	today: '',
	orderCnt: 0,
	todayOrderVOs: [],
});
const initTodayOrders = () => {
	const promise = request.get<MyModel.Today>(`${urls.todayOrders}`);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				driverRef.value = res.data;
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

const { routerPush } = useRouterPush();
const toMyPage = () => {
	routerPush({
		path: '/mypage/details',
	});
};
const toOrder = () => {
	routerPush({
		path: '/order/list',
	});
};
const toCar = () => {
	routerPush({
		path: '/car/list',
	});
};
const toCheck = () => {
	if (!driverRef.value.log || driverRef.value.log <= 0) {
		showToast({
			message: '仕事日記を提出してください',
		});
		return;
	}
	routerPush({
		path: '/checkIn/check',
	});
};
const toLog = () => {
	routerPush({
		path: '/log/write',
	});
};
const toLogList = () => {
	routerPush({
		path: '/log/list',
	});
};
const showOrderCnt = (flag: string, status: number) => {
	if (driverRef.value.todayOrderVOs.length <= 0) {
		return 0;
	}
	let { todayOrderVOs } = driverRef.value;
	if (flag === 'today') {
		todayOrderVOs = todayOrderVOs.filter(
			(i) => moment(i.startTime).date() === moment().date()
		);
	} else {
		todayOrderVOs = todayOrderVOs.filter(
			(i) => moment(i.startTime).date() !== moment().date()
		);
	}
	if (status !== -1) {
		todayOrderVOs = todayOrderVOs.filter((i) => i.orderStatus === status);
	}
	return todayOrderVOs.length;
};
const showCar = (flag: string) => {
	if (driverRef.value.todayOrderVOs.length <= 0) {
		return '-';
	}
	let { todayOrderVOs } = driverRef.value;
	if (flag === 'today') {
		todayOrderVOs = todayOrderVOs.filter(
			(i) => moment(i.startTime).date() === moment().date()
		);
	} else {
		todayOrderVOs = todayOrderVOs.filter(
			(i) => moment(i.startTime).date() !== moment().date()
		);
	}
	const cars: any = [];
	todayOrderVOs.forEach((v, i, a) => {
		if (!cars.includes(v.carNo)) {
			cars.push(v.carNo);
		}
	});
	return cars.length ? cars.map((i) => i).join(',') : '-';
};
const toCall = (flag: string) => {
	if (flag === 'pm' && driverRef.value.amCall === 0) {
		showToast('乗務前点呼を完了する後にしてください');
		return;
	}
	let jsonStr = '';
	if (driverRef.value.callVOs && driverRef.value.callVOs.length > 0) {
		jsonStr = JSON.stringify(driverRef.value.callVOs[0]);
	}
	routerPush({
		path: '/call/details',
		query: {
			item: encodeURIComponent(jsonStr),
			flag,
			isOk: flag === 'am' ? driverRef.value.amCall : driverRef.value.pmCall,
		},
	});
};

onMounted(() => {
	initTodayOrders();
});
</script>

<style lang="scss"></style>
