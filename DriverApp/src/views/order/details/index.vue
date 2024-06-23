<template>
	<div class="pb-20">
		<van-skeleton title :row="10" :loading="loading">
			<van-form>
				<van-cell-group inset title="ー 注文情報 ー">
					<van-cell>
						<div class="flex justify-around m-1">
							<van-button v-if="EnumOrderStatus[modelRef.orderStatus ?? ''] ===
			EnumOrderStatus.Check
			" color="#fb8c00" @click="onConfirm">
								注文確認
							</van-button>
							<van-button v-else-if="EnumOrderStatus[modelRef.orderStatus ?? ''] ===
			EnumOrderStatus.Booked
			" color="#fb8c00" @click="onWork">
								注文開始
							</van-button>
							<van-button v-else color="#fb8c00" @click="onComplete">
								注文終了
							</van-button>
							<van-button color="#fb8c00" @click="toAdvance">
								立替申請
							</van-button>
							<van-checkbox v-model="modelRef.isETC" shape="square" @click="setETC">
								ETCあり
							</van-checkbox>
						</div>
					</van-cell>
					<van-cell title="注文番号">{{ modelRef.orderNo }}</van-cell>
					<van-cell title="注文内容">{{ modelRef.orderTypeName }}</van-cell>
					<van-cell title="基础料金">{{ modelRef.orderPrice + '円' }}</van-cell>
					<van-cell title="车费内容">{{ modelRef.feeType ?? '全包' }}</van-cell>
					<van-cell title="是否收现">{{
			modelRef.isCash ? '是(具体查看收现名目)' : '否'
		}}</van-cell>
					<van-cell title="超时收现">
						<template #value>
							{{ modelRef.isOutTimeCash ? '是' : '否' }}
						</template>
						<template #label>
							{{
			modelRef.isOutTimeCash ? `${modelRef.outTimeAmount}円/30分` : ''
		}}
						</template>
					</van-cell>
					<van-cell title="OP备注">
						<template #label>
							{{ modelRef.companyRemark }}
						</template>
					</van-cell>
				</van-cell-group>
				<van-cell-group inset title="ー ツアー情報 ー">
					<van-cell v-if="modelRef.orderTypeName === EnumOrderType.Haiya" title="服务天数">{{ modelRef.orderDays + '天'
						}}</van-cell>
					<van-cell title="服务时间">{{ modelRef.startTime }}</van-cell>
					<van-cell v-if="modelRef.orderTypeName === EnumOrderType.Haiya" title="结束时间">{{ modelRef.endTime }}</van-cell>
					<van-cell title="出发地" :value="modelRef.orderFrom" :label="modelRef.orderFromDetails" />
					<van-cell title="目的地" :value="modelRef.orderTo" :label="modelRef.orderToDetails" />
					<van-cell v-if="modelRef.orderTypeName === EnumOrderType.Airport_S ||
			modelRef.orderTypeName === EnumOrderType.Airport_Y
			" title="航班号">{{ modelRef.flightNo }}</van-cell>
					<van-cell v-if="modelRef.orderTypeName === EnumOrderType.Airport_S ||
			modelRef.orderTypeName === EnumOrderType.Airport_Y
			" title="机场">{{ modelRef.airport }}</van-cell>
					<van-cell title="大人数">{{ modelRef.adultNum + '人' }}</van-cell>
					<van-cell title="小孩数">{{ modelRef.childrenNum + '人' }}</van-cell>
					<van-cell title="行李数">{{ modelRef.luggageNum + '件' }}</van-cell>
				</van-cell-group>
				<van-cell-group inset title="ー お客様情報 ー">
					<van-cell title="お客様">{{ modelRef.customerName }}</van-cell>
					<van-cell title="連絡方法①">{{
			`${modelRef.contactMethod1} ${modelRef.contactContent1}`
		}}</van-cell>
					<van-cell v-if="modelRef.contactContent2" title="連絡方法②">{{
			`${modelRef.contactContent2} ${modelRef.contactContent2}`
		}}</van-cell>
					<van-cell v-if="modelRef.contactContent3" title="連絡方法③">{{
			`${modelRef.contactContent3} ${modelRef.contactContent3}`
		}}</van-cell>
					<van-cell title="お客様要望">
						<template #label>
							{{ modelRef.customerRemark }}
						</template>
					</van-cell>
				</van-cell-group>
				<van-cell-group v-if="goodsRef && goodsRef.length != 0" inset title="ー 車備品 ー">
					<van-cell v-for="goods in goodsRef" :key="goods.id" :title="goods.goodsType">
						<template #value>
							{{ goods.amount + '件' }}
						</template>
						<template #label>
							{{ goods.remark ?? '' }}
						</template>
					</van-cell>
				</van-cell-group>
				<van-cell-group v-if="payRef && payRef.length != 0" inset title="ー 收现 ー">
					<van-cell v-for="pay in payRef" :key="pay.id" :title="pay.payItem ? pay.payItem : pay.financeTypeName">
						<template #value>
							{{
			pay.amount +
			`${pay.currencyCode === 'usd'
				? '美元'
				: pay.currencyCode === 'cny'
					? '元'
					: '円'
			}`
		}}
						</template>
						<template #label>
							{{ pay.remark ?? '' }}
						</template>
					</van-cell>
				</van-cell-group>
				<van-cell-group v-if="fileRef && fileRef.length != 0" inset title="ー 添付資料 ー">
					<van-cell v-for="file in fileRef" :key="file.id" :title="file.fileName" is-link :url="file.fileUrl" />
				</van-cell-group>
			</van-form>
		</van-skeleton>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import { EnumOrderStatus, EnumOrderType } from '@/enum';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';

const modelRef = ref<MyModel.Order>({
	id: '',
	orderType: 'Single',
	orderStatus: 'Booked',
});
const goodsRef = ref<MyModel.OrderGoods[]>([
	{
		id: '',
	},
]);

const payRef = ref<MyModel.PayRecord[]>([
	{
		id: '',
	},
]);
const fileRef = ref<MyModel.OrderFile[]>([
	{
		id: '',
	},
]);

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const route = useRouter();

const urls = {
	details: '/driver/order/details/',
	confirm: '/driver/order/confirm',
	work: '/driver/order/work',
	complete: '/driver/order/complete',
	setETC: '/driver/order/setETC',
};
const onload = () => {
	loading.value = true;
	const { id } = route.currentRoute.value.query;
	request
		.get<MyModel.OrderDetails>(`${urls.details}${id}`)
		.then((res) => {
			if (res.data) {
				const { data } = res;
				modelRef.value = data.orderVO;
				goodsRef.value = data.orderGoodsVOs;
				payRef.value = data.payRecordVOs;
				fileRef.value = data.orderFileVOs;
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
const onConfirm = () => {
	// 确认注文
	const promise = request.put<Boolean>(`${urls.confirm}/${modelRef.value.id}`);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				onload();
				showNotify({
					type: 'success',
					message: '注文確認された',
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
const onWork = () => {
	// 开始注文
	const promise = request.put<Boolean>(`${urls.work}/${modelRef.value.id}`);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				onload();
				showNotify({
					type: 'success',
					message: '注文開始された',
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

const onComplete = () => {
	// 完成注文
	const promise = request.put<Boolean>(`${urls.complete}/${modelRef.value.id}`);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				onload();
				showNotify({
					type: 'success',
					message: '注文完了',
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

const setETC = () => {
	// 设置ETC料金有无
	const promise = request.put<Boolean>(`${urls.setETC}/${modelRef.value.id}`);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				onload();
				showNotify({
					type: 'success',
					message: '設定完了',
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

const toAdvance = () => {
	const jsonStr = JSON.stringify(modelRef.value);
	routerPush({
		path: '/order/advance',
		query: { item: encodeURIComponent(jsonStr) },
	});
};
onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
