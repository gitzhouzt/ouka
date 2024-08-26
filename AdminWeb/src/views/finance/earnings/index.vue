<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" :label-width="100" label-placement="left">
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="キーワード">
							<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="番号/連絡先/ワード"
								clearable />
						</n-form-item>
						<n-form-item label="订单来源">
							<n-input v-model:value="searchParams.orderSource" placeholder="クリック来源を選択" readonly
								@click="showDict('order_source')" />
						</n-form-item>
						<n-form-item label="責任人">
							<n-input v-model:value="searchParams.sellerName" placeholder="クリック責任人を選択" readonly @click="showStaff()" />
						</n-form-item>
					</n-form>
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="ドライバー" path="driverName">
							<n-input-group>
								<n-input v-model:value="searchParams.driverName" readonly placeholder="クリックドライバーを選択"
									@click="showDriver()"></n-input>
							</n-input-group>
						</n-form-item>
						<n-form-item label="車両" path="carName">
							<n-input-group>
								<n-input v-model:value="searchParams.carName" readonly placeholder="クリック車両を選択"
									@click="showCar()"></n-input>
							</n-input-group>
						</n-form-item>
					</n-form>
					<!-- <n-form-item label="サービス時間" path="selTime">
            <n-date-picker v-model:value="searchParams.selTime" type="daterange" clearable @update:value="onUpdate" />
          </n-form-item> -->
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="searchReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<n-space>
				<!-- <n-button type="primary" @click="temp">一括決算</n-button> -->
				<n-button type="primary" @click="tempE">ダウンロード</n-button>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table size="small" :row-key="rowKey" remote bordered :v-model:checked-row-keys="checkedRowKeys"
					:columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="4000" :single-line="false"
					:flex-height="true" :style="{ height: hightRef + 'px' }" @update:checked-row-keys="handleChecked"
					@update:page-size="handleUpdatePageSize" @update:page="handlePageChange" />
			</loading-empty-wrapper>
		</n-space>
		<dict-select-modal ref="dictModal" @click="selectDict" />
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<car-select-modal ref="carModal" @click="selectCar" />
		<staff-select-modal ref="staffModal" @click="selectStaff" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NNumberAnimation, NTag, useLoadingBar, useMessage } from 'naive-ui';
import moment from 'moment';
import { RowData } from 'naive-ui/es/data-table/src/interface';
import { EnumFinanceStatus, EnumOrderType } from '@/enum';
import { useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const module = 'finance/earnings';
const moduleParams: MySearch.OrderSearchParams = {
	keyword: '',
	orderSource: '',
	driverName: '',
	sellerName: '',
	selTime: [Date.now(), Date.now()],
	startBeginTime: Date.now(),
	startEndTime: Date.now(),
	orderStatus: ['Booked', 'Working', 'Completed']
};

const {
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	handleExport,
	searchQuery,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Earnings>(module, moduleParams);
resetParams();

const envConfig = getEnvConfig(import.meta.env);
const { financeStatusTagType } = useMyTags();
const { isMobile, isWrap, addSeparator } = useMyCommon();

const urls = {
	finance: `/order/finance`,
	defaultPlacard: `/order/defaultPlacard`
};
const rowKey = (row: RowData) => row.id;
const checkedRowKeys = ref<Array<string | number>>([]);
const message = useMessage();
const loadingBar = useLoadingBar();

const columns: DataTableColumn<MyModel.Earnings>[] = [
	{
		title: 'ツアー開始日',
		key: 'startTime',
		align: 'center',
		sorter: true,
		width: 120,
		render(row) {
			return `${moment(row.orderVO?.startTime).format('yyyy/MM/DD')}`;
		}
	},
	{
		title: 'ツアー終了日',
		key: 'startTime',
		align: 'center',
		sorter: true,
		width: 120,
		render(row) {
			return `${moment(row.orderVO?.endTime).format('yyyy/MM/DD')}`;
		}
	},
	{
		title: 'ツアー内容',
		key: 'orderType',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			const orderType = row.orderVO?.orderType as MyEnumType.EnumOrderTypeKey;
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
		title: '第三者',
		key: 'orderSource',
		align: 'center',
		sorter: true,
		width: 100,
		render(row) {
			return h(
				'span',
				{},
				{
					default: () => row.orderVO?.orderSource
				}
			);
		}
	},
	{
		title: '基本料金',
		key: 'orderPrice',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.orderPrice ?? 0)}` })];
		}
	},
	{
		title: '全包or别',
		key: 'feeType',
		align: 'center',
		width: 150,
		render(row) {
			return h(
				'span',
				{},
				{
					default: () => row.orderVO?.feeType
				}
			);
		}
	},
	{
		title: 'ドライバー',
		key: 'driverName',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return [
				h(
					'div',
					{
						class: `${row.orderVO?.driverName ?? 'text-red'} `
					},
					{ default: () => row.orderVO?.driverName ?? '未定' }
				)
			];
		}
	},
	{
		title: '車両',
		key: 'carNo',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return [
				h(
					'div',
					{
						class: `${row.orderVO?.carNo ?? 'text-red'} `
					},
					{ default: () => row.orderVO?.carNo ?? '未定' }
				)
			];
		}
	},
	{
		title: '手续费',
		key: 'handlingFee',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.handlingFee ?? 0)}` })];
		}
	},
	{
		title: '油费',
		key: 'oilFee',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.oilFee ?? 0)}` })];
		}
	},
	{
		title: 'ETC',
		key: 'etc',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.etc ?? 0)}` })];
		}
	},
	{
		title: '司机工资(委托)',
		key: 'entrustSalary',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.entrustSalary ?? 0)}` })];
		}
	},
	{
		title: '司机工资',
		key: 'salary',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.salary ?? 0)}` })];
		}
	},
	{
		title: '外派金额',
		key: 'entrust',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.entrust ?? 0)}` })];
		}
	},
	{
		title: '停车场',
		key: 'parking',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.parking ?? 0)}` })];
		}
	},
	{
		title: '粗利润',
		key: 'profit',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.profit ?? 0)}` })];
		}
	},
	{
		title: '粗利润率',
		key: 'profitRate',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `${row.profitRate / 100}%` })];
		}
	},
	{
		title: '責任人',
		key: 'sellerName',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return `${row.orderVO?.sellerName}`;
		}
	},
	{
		title: '注文番号',
		key: 'orderNo',
		align: 'center',
		sorter: true,
		width: 150
	}
];

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
		default:
			break;
	}
};

const staffModal = ref<any>(null);
const showStaff = () => {
	staffModal.value?.showModal();
};
const selectStaff = (result: any) => {
	searchParams.sellerId = result.id;
	searchParams.sellerName = result.userName;
	searchParams.sellerNo = result.userNo;
	searchParams.sellerPhoto = result.userAvatar;
};

const driverModal = ref<any>(null);
const showDriver = () => {
	// const params = {
	//   orderDays: formValue.value.orderDays,
	//   orderStartTime: formValue.value.orderStartTime
	// };
	driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
	searchParams.driverId = result.id;
	searchParams.driverName = result.userName;
	searchParams.driverNo = result.userNo;
	searchParams.driverPhoto = result.userAvatar;
};

const carModal = ref<any>(null);
const showCar = () => {
	// const params = {
	//   orderDays: formValue.value.orderDays,
	//   orderStartTime: formValue.value.orderStartTime
	// };
	carModal.value?.showModal();
};
const selectCar = (result: any) => {
	searchParams.carId = result.value;
	searchParams.carName = result.text;
	searchParams.carNo = result.carNo;
	searchParams.carType = result.carType;
	searchParams.carSeat = result.carSeat;
};

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	searchParams.startBeginTime = value ? value[0] : undefined;
	searchParams.startEndTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;
	// initData();
	searchQuery();
});
</script>
<style scoped></style>
