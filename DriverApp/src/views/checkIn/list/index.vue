<template>
	<div>
		<div style="margin: 10px">
			<van-button v-if="false" color="#fb8c00" size="large" block @click="handleExport">ダウンロード</van-button>
		</div>
		<van-search v-model="searchParams.keyword" :clearable="false" placeholder="キーワード：注文番号/営業/お客様" show-action
			shape="round">
			<template #action>
				<div @click="searchQuery">検索</div>
			</template>
		</van-search>
		<van-pull-refresh v-model="refreshing" class="min-h-full" @refresh="handleRefresh">
			<template v-if="!empty">
				<van-list v-model:loading="loading" offset="100" :finished="finished" finished-text="データなし"
					@load="handlePageChange">
					<div v-for="item in dataSource" :key="item.id" class="bg-white rounded-1 m-2 p-2" @click="onClick(item.id)">
						<div class="mt-4px text-16px">
							<div class="flex justify-between">
								<div>
									<span class="font-bold">订单类型：</span>
									<span class="ml-2"> {{ EnumOrderType[item.orderType] }}</span>
								</div>
								<div>
									<van-tag :type="orderStatusTagType(item.orderStatus as MyEnumType.EnumOrderStatusKey)">
										{{ EnumOrderStatus[item.orderStatus] }}</van-tag>
								</div>
							</div>
							<div class="mt-4px">
								<span class="font-bold">日期：</span>
								<span class="ml-2">
									{{ moment(item.startTime).format('yyyy-MM-DD') }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">配车时间：</span>
								<span class="ml-2">{{
									moment(item.startTime).format('HH:mm')
								}}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">配车地点：</span>
								<span class="ml-2 text-blue" @click.stop="showTips(item.orderFromDetails ?? '')">
									{{ item.orderFrom }}
								</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">目的地：</span><span class="ml-2 text-blue"
									@click.stop="showTips(item.orderToDetails ?? '')">
									{{ item.orderTo }}
								</span>
							</div>
							<div v-if="EnumOrderType[item.orderType ?? 'Haiya'] ===
								EnumOrderType.Airport_S ||
								EnumOrderType[item.orderType ?? 'Haiya'] ===
								EnumOrderType.Airport_Y
								">
								<span class="font-bold">航班号：</span>
								<span class="ml-2"> {{ item.flightNo ?? '-' }}</span>
							</div>
							<div v-if="EnumOrderType[item.orderType ?? 'Haiya'] ===
								EnumOrderType.Airport_S ||
								EnumOrderType[item.orderType ?? 'Haiya'] ===
								EnumOrderType.Airport_Y
								">
								<span class="font-bold">机场：</span>
								<span class="ml-2"> {{ item.airport ?? '-' }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">车号：</span><span class="ml-2">{{
									`${item.carNo}(${item.carType})`
								}}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">备注：</span><span class="ml-2">{{ item.companyRemark }}</span>
							</div>
						</div>
					</div>
				</van-list>
			</template>
			<template v-else>
				<van-empty image="search" description="データなし" />
			</template>
		</van-pull-refresh>
	</div>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue';
import { showNotify } from 'vant';
import moment from 'moment';
import { EnumOrderStatus, EnumOrderType } from '@/enum';
import { useMyTags, useRouterPush } from '@/composables';
import { useDataTable } from '@/hooks/common';

const { orderStatusTagType } = useMyTags();

const module = 'driver/order';
const moduleParams: MySearch.SearchParams = {
	keyword: '',
};

const {
	searchQuery,
	resetParams,
	handleExport,
	handleRefresh,
	handlePageChange,
	searchParams,
	empty,
	dataSource,
	loading,
	finished,
	refreshing,
} = useDataTable<MyModel.Order>(module, moduleParams);
resetParams();
const { routerPush } = useRouterPush();

const showTips = (message: string) => {
	showNotify({ type: 'primary', message, position: 'bottom' });
};
const onClick = (id: string) => {
	routerPush({
		path: '/order/details',
		query: { id },
	});
};
onMounted(() => {
	searchQuery();
});
</script>

<style lang="scss"></style>
