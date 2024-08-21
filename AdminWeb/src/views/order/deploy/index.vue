<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" label-placement="left">
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="キーワード">
							<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="注文番号/連絡先"
								clearable />
						</n-form-item>
						<n-form-item label="運行内容">
							<n-select v-model:value="searchParams.orderType" class="w-50" :options="orderTypeOptions"
								:consistent-menu-width="false" />
						</n-form-item>
						<n-form-item label="ドライバー" path="driverName">
							<n-input-group>
								<n-input v-model:value="searchParams.driverName" readonly placeholder="クリックドライバーを選択"
									@click="showDriver()"></n-input>
							</n-input-group>
						</n-form-item>
					</n-form>
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="車両" path="carName">
							<n-input-group>
								<n-input v-model:value="searchParams.carNo" readonly placeholder="クリック車両を選択"
									@click="showCar()"></n-input>
							</n-input-group>
						</n-form-item>
						<n-form-item label="サービス時間" path="selTime">
							<n-date-picker v-model:value="searchParams.selTime" type="datetimerange" format="yyyy/MM/dd HH:mm:ss"
								clearable @update:value="onUpdate" />
						</n-form-item>
					</n-form>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="searchReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<n-space justify="space-between">
				<div>
					<n-button type="primary" @click="handleSend">配車情報発送</n-button>
					<n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
				</div>
				<div>
					<n-button class="ml-2" @click="handleQuery">詳細検索</n-button>
					<n-button class="ml-2" @click="handleCols">表示項目設定</n-button>
				</div>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="cols" :data="dataSource" :pagination="pagination" :scroll-x="scrollX"
					:single-line="false" :flex-height="true" :row-class-name="rowClassName" :style="{ height: hightRef + 'px' }"
					@update:page="handlePageChange" @update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<order-details ref="detailsModal" />
		<deploy-action ref="deployModal" @close="searchQuery" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<car-select-modal ref="carModal" @click="selectCar" />
		<set-cols-drawer ref="colsModal" @click="setCols" />
		<query-drawer ref="queryModal" @click="onQuery" @reset="searchReset" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NTag } from 'naive-ui';
import moment from 'moment';
import { EnumOrderStatus, EnumOrderType } from '@/enum';
import { useMyTags, useMyCommon, useMyOptions } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { OrderDetails } from '../master/components';
import { DeployAction, QueryDrawer } from './components';

const module = 'order';
const moduleParams: MySearch.OrderSearchParams = {
	keyword: '',
	orderSource: '',
	orderKey: '',
	driverName: '',
	sellerName: '',
	carNo: '',
	city: '',
	orderStatus: ['Assigning', 'Booked', 'Working', 'Sending', 'Check'],
	selTime: [
		moment().add(1, 'd').set('hours', 0).set('minutes', 0).set('seconds', 0).valueOf(),
		moment().add(2, 'd').set('hours', 3).set('minutes', 0).set('seconds', 0).valueOf()
	],
	startBeginTime: moment().add(1, 'd').set('hours', 0).set('minutes', 0).set('seconds', 0).valueOf(),
	startEndTime: moment().add(2, 'd').set('hours', 3).set('minutes', 0).set('seconds', 0).valueOf()
};

const {
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	handleSorter,
	handleExport,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Order>(module, moduleParams);
resetParams();

const { orderStatusTagType } = useMyTags();
const { orderTypeOptions } = useMyOptions();
const { isMobile, isWrap } = useMyCommon();

const deployModal = ref<any>(null);
const handleDeploy = (title: string, row: MyModel.Order) => {
	deployModal.value.setTitle(title);
	deployModal.value.showModal(row);
};

const detailsModal = ref<any>(null);
const handleDetails = (title: string, row: MyModel.Order) => {
	detailsModal.value.setTitle(title);
	detailsModal.value.showModal(row);
};

const urls = {
	send: `/order/send`
};
const handleSend = () => {
	if (dataSource.value.length === 0) {
		return;
	}
	loading.value = true;
	const promise = request.post(`${urls.send}`);
	promise
		.then((res: any) => {
			const { data } = res;
			if (data) {
				window.$message?.success(`発送しました`);
				searchQuery();
			}
		})
		.finally(() => {
			loading.value = false;
		});
};

const rowClassName = (row: MyModel.Order) => {
	if (!row.driverName || !row.carNo) {
		return 'deploy';
	}
	return '';
};
const columns: DataTableColumn<MyModel.Order>[] = [
	{
		title: '订单来源',
		key: 'orderSource',
		align: 'center',
		sorter: true,
		width: 80
	},
	{
		title: '注文番号',
		key: 'orderNo',
		align: 'center',
		sorter: true,
		width: 50
	},
	{
		title: 'ツアー開始日',
		key: 'startTime',
		align: 'center',
		sorter: true,
		width: 60,
		render(row) {
			return row?.startTime ? `${row?.startTime}` : '未定';
		}
	},
	{
		title: 'ツアー終了日',
		key: 'endTime',
		align: 'center',
		width: 60,
		render(row) {
			return row?.endTime ? `${row?.endTime}` : '未定';
		}
	},
	{
		title: '運行内容',
		key: 'orderType',
		align: 'center',
		width: 50,
		render(row) {
			const orderType = row.orderType as MyEnumType.EnumOrderTypeKey;
			const status = EnumOrderType[orderType];
			return h(
				'span',
				{},
				{
					default: () => status
				}
			);
		}
	},
	{
		title: 'ドライバー',
		key: 'driverName',
		sorter: true,
		align: 'center',
		width: 60,
		render(row) {
			const deployOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDeploy('支配操作', row)
				},
				{ default: () => '支配' }
			);
			return [
				h(
					'div',
					{
						class: `${row.driverName ?? 'text-red'} flex flex-row items-center justify-between`
					},
					{ default: () => [row.driverName ?? '未定', deployOption] }
				)
			];
		}
	},
	{
		title: '車両',
		key: 'carNo',
		align: 'center',
		sorter: true,
		width: 50,
		render(row) {
			const deployOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDeploy('支配操作', row)
				},
				{ default: () => '支配' }
			);
			return [
				h(
					'div',
					{
						class: `${row.carNo ?? 'text-red'} flex flex-row items-center justify-between`
					},
					{ default: () => [row.carNo ?? '未定', deployOption] }
				)
			];
		}
	},
	{
		title: '航空便',
		key: 'flightNo',
		align: 'center',
		width: 60,
		render(row) {
			return [h('div', {}, { default: () => row.flightNo ?? '-' })];
		}
	},
	{
		title: '出発地',
		key: 'orderFrom',
		align: 'center',
		width: 150,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.orderFrom ? row.orderFrom : '-') })];
		}
	},
	{
		title: '目的地',
		key: 'orderTo',
		align: 'center',
		width: 150,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.orderTo ? row.orderTo : '-') })];
		}
	},
	{
		title: 'お客様要望',
		key: 'customerRemark',
		align: 'center',
		width: 150,
		render(row) {
			return [
				h(
					NEllipsis,
					{ lineClamp: 1, tooltip: true },
					{ default: () => (row.customerRemark ? row.customerRemark : '-') }
				)
			];
		}
	},
	{
		title: '備考',
		key: 'companyRemark',
		align: 'center',
		width: 150,
		render(row) {
			return [
				h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.companyRemark ? row.companyRemark : '-') })
			];
		}
	},
	{
		title: 'お客様',
		key: 'customerName',
		align: 'center',
		width: 100,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					{ default: () => (row.customerName ? row.customerName : '-') }
				)
			];
		}
	},
	{
		title: '連絡方法①',
		key: 'contactMethod1',
		align: 'center',
		width: 80,
		resizable: true,
		render(row) {
			return h(
				'div',
				{
					style: { textAlign: 'left' }
				},
				[h('span', {}, { default: () => `${row.contactMethod1}：${row.contactContent1}` })]
			);
		}
	},
	{
		title: '子供数',
		key: 'childrenNum',
		align: 'center',
		width: 20
	},
	{
		title: '大人数',
		key: 'adultNum',
		align: 'center',
		width: 20
	},
	{
		title: '希望車両',
		key: 'specifyCarType',
		align: 'center',
		width: 50
	},
	{
		title: '第三者',
		key: 'orderKey',
		align: 'center',
		width: 100
	},
	{
		title: '荷物数',
		key: 'luggageNum',
		align: 'center',
		width: 20
	},
	{
		title: '座席数',
		key: 'carSeat',
		align: 'center',
		width: 20,
		render(row) {
			return [h('div', {}, { default: () => row.carSeat ?? '-' })];
		}
	},
	{
		title: '空港',
		key: 'airport',
		align: 'center',
		width: 50,
		render(row) {
			return [h('div', {}, { default: () => row.airport ?? '-' })];
		}
	},

	{
		title: '注文日時',
		key: 'createTime',
		sorter: true,
		align: 'center',
		width: 60
	},
	{
		title: 'ステータス',
		key: 'orderStatus',
		align: 'center',
		width: 30,
		render(row) {
			const orderStatus = row.orderStatus as MyEnumType.EnumOrderStatusKey;
			const status = EnumOrderStatus[orderStatus];
			const tagType = orderStatusTagType(orderStatus);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => status
				}
			);
		}
	},
	{
		title: '詳細',
		key: 'details',
		width: 20,
		align: 'center',
		render(row) {
			const detailsOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDetails('注文詳細', row)
				},
				{ default: () => '詳細' }
			);
			return detailsOption;
		}
	}
];

const cols = ref<any>(columns);
const scrollX = ref<number>(5250);
const colsModal = ref<any>(null);
const handleCols = () => {
	colsModal.value?.setTitle('表示項目設定');
	colsModal.value?.showModal(columns, cols.value, ['details', 'orderNo', 'edit']);
};
const setCols = (c: any) => {
	cols.value = c;
	const x = c.length === columns.length ? 5250 : 5250 - (columns.length - c.length) * 250;
	scrollX.value = x;
};

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
	dictModal.value?.showModal();
	dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
	switch (result.type) {
		case 'order_source':
			searchParams.orderSource = result.text;
			break;
		case 'order_city':
			searchParams.city = result.text;
			break;
		case 'order_key':
			searchParams.orderKey = result.text;
			break;
		default:
			break;
	}
};
const driverModal = ref<any>(null);
const showDriver = () => {
	driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
	searchParams.driverName = result.userName;
};

const staffModal = ref<any>(null);
const showStaff = () => {
	staffModal.value?.showModal();
};
const selectStaff = (result: any) => {
	searchParams.sellerName = result.userName;
};

const carModal = ref<any>(null);
const showCar = () => {
	carModal.value?.showModal();
};
const selectCar = (result: any) => {
	searchParams.carNo = result.carNo;
};

const queryModal = ref<any>(null);
const handleQuery = () => {
	queryModal.value?.showModal(searchParams);
};
const onQuery = (params: any) => {
	searchParams.driverName = params.driverName;
	searchParams.carNo = params.carNo;
	searchQuery();
};

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	console.log('111', value);

	searchParams.startBeginTime = value ? value[0] : undefined;
	searchParams.startEndTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 200;

	searchQuery();
});
</script>
<style scoped>
:deep(.deploy td) {
	background: rgba(250, 199, 199, 0.75) !important;
}
</style>
