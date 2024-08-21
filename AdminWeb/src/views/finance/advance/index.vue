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
				<n-button type="primary" @click="handleBatchFinance(1)">一括決算</n-button>
				<n-button type="primary" @click="handleExport">ダウンロード</n-button>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table :row-key="rowKey" remote bordered :v-model:checked-row-keys="checkedRowKeys" :columns="columns"
					:data="dataSource" :pagination="pagination" :scroll-x="4000" :single-line="false" :flex-height="true"
					:style="{ height: hightRef + 'px' }" @update:checked-row-keys="handleChecked"
					@update:page-size="handleUpdatePageSize" @update:page="handlePageChange" />
			</loading-empty-wrapper>
		</n-space>
		<dict-select-modal ref="dictModal" @click="selectDict" />
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<staff-select-modal ref="staffModal" @click="selectStaff" />
		<car-select-modal ref="carModal" @click="selectCar" />
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

const module = 'finance/advance';
const moduleParams: MySearch.OrderSearchParams = {
	keyword: '',
	orderSource: '',
	driverName: '',
	sellerName: '',
	carNo: '',
	selTime: [Date.now(), Date.now()],
	startBeginTime: Date.now(),
	startEndTime: Date.now(),
	orderStatus: ['Sending', 'Check', 'Booked', 'Working', 'Completed']
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
} = useDataTable<MyModel.Advance>(module, moduleParams);
resetParams();

const envConfig = getEnvConfig(import.meta.env);
const { financeStatusTagType } = useMyTags();
const { isMobile, isWrap, addSeparator } = useMyCommon();

const urls = {
	finance: `/finance/advance/settlement`,
	defaultPlacard: `/order/defaultPlacard`
};
const rowKey = (row: RowData) => row.id;
const checkedRowKeys = ref<Array<string | number>>([]);
const message = useMessage();
const loadingBar = useLoadingBar();

const handleSettlement = (ids: Array<string | number>) => {
	const params = {
		ids
	};
	const promise = request.post<Boolean>(urls.finance, params);
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				message.success('決済しました');
				checkedRowKeys.value = [];
				searchReset();
			}
		})
		.catch(err => {
			message.warning(err);
		})
		.finally(() => {
			loading.value = false;
			loadingBar.finish();
			searchQuery();
		});
};

const handleBatchFinance = (flag: number) => {
	if (checkedRowKeys.value.length === 0) {
		message.warning('データ選択してください');
		return;
	}
	if (flag === 0) {
		// handelPaid(checkedRowKeys.value);
	} else if (flag === 1) {
		handleSettlement(checkedRowKeys.value);
	}
};

const tempE = () => {
	const fileLink = document.createElement('a');
	fileLink.href = `${envConfig.static}/files/料金決算_20240226081002.xls`;
	fileLink.target = '_blank';
	fileLink.click();
	fileLink.remove();
};

const handleDefaultPlacard = (id: string) => {
	// loading.value = true;
	// const promise = request.post(`${urls.defaultPlacard}/${id}`);
	// promise
	//   .then((res: any) => {
	//     const { data } = res;
	//     const fileLink = document.createElement('a');
	//     fileLink.href = `${envConfig.static}${data}`;
	//     fileLink.target = '_blank';
	//     fileLink.click();
	//     fileLink.remove();
	//   })
	//   .finally(() => {
	//     loading.value = false;
	//   });
	// const fileLink = document.createElement('a');
	// fileLink.href = `${envConfig.static}/files/defaultInvoice_web314159.pdf`;
	// fileLink.target = '_blank';
	// fileLink.click();
	// fileLink.remove();
};

const handleChecked = (
	keys: Array<string | number>,
	rows: object[],
	meta: { row: object | undefined; action: 'check' | 'uncheck' | 'checkAll' | 'uncheckAll' }
) => {
	checkedRowKeys.value = keys;
};
const columns: DataTableColumn<MyModel.Advance>[] = [
	{
		type: 'selection',
		disabled(row) {
			return EnumFinanceStatus[row.status ?? 'Waiting'] === EnumFinanceStatus.Completed;
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
	},
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
		title: '売上総金額',
		key: 'profitAmount',
		align: 'center',
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.profitAmount ?? 0)}` })];
		}
	},
	{
		title: '別請求総金額',
		key: 'advanceAmount',
		align: 'center',
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.advanceAmount ?? 0)}` })];
		}
	},
	{
		title: '入金総金額',
		key: 'inAmount',
		align: 'center',
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.inAmount ?? 0)}` })];
		}
	},
	{
		title: '立替食事代',
		key: 'mealAmount',
		align: 'center',
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.mealAmount ?? 0)}` })];
		}
	},
	{
		title: '立替ホテル代',
		key: 'hotelAmount',
		align: 'center',
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.hotelAmount ?? 0)}` })];
		}
	},
	{
		title: '入湯税',
		key: 'admissionAmount',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.admissionAmount ?? 0)}` })];
		}
	},
	{
		title: '駐車代',
		key: 'parkingAmount',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.parkingAmount ?? 0)}` })];
		}
	},
	{
		title: 'ETC料金',
		key: 'etcAmount',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.etcAmount ?? 0)}` })];
		}
	},
	{
		title: '有料道路料金',
		key: 'roadAmount',
		align: 'center',
		sorter: true,
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.roadAmount ?? 0)}` })];
		}
	},
	{
		title: '入門料チケット',
		key: 'ticketAmount',
		align: 'center',
		sorter: true,
		width: 120,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.ticketAmount ?? 0)}` })];
		}
	},
	{
		title: '水代',
		key: 'waterAmount',
		align: 'center',
		sorter: true,
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.waterAmount ?? 0)}` })];
		}
	},
	{
		title: '超時料金',
		key: 'overtimeAmount',
		align: 'center',
		sorter: true,
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.overtimeAmount ?? 0)}` })];
		}
	},
	{
		title: 'その他立替',
		key: 'otherAmount',
		align: 'center',
		sorter: true,
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.otherAmount ?? 0)}` })];
		}
	},
	{
		title: 'ステータス',
		width: 100,
		key: 'status',
		align: 'center',
		render(row) {
			const status = row.status as MyEnumType.EnumFinanceStatusKey;
			const tagType = financeStatusTagType(status);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => EnumFinanceStatus[row.status as MyEnumType.EnumFinanceStatusKey]
				}
			);
		}
	},
	{
		title: '操作',
		key: 'actions',
		width: 80,
		align: 'center',
		render(row) {
			const confirmOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleSettlement([row.id])
				},
				{ default: () => '決算' }
			);
			return EnumFinanceStatus[row.status ?? 'Waiting'] !== EnumFinanceStatus.Completed ? confirmOption : '決算済';
		}
	},
	{
		title: '請求書',
		key: 'pdf',
		width: 100,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDefaultPlacard(row.id)
				},
				{ default: () => '請求書' }
			);
			return option;
		}
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
