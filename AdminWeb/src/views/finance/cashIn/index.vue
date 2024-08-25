<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" :label-width="100" label-placement="left">
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="キーワード">
							<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="注文番号"
								clearable />
						</n-form-item>
						<n-form-item label="運行内容">
							<n-select v-model:value="searchParams.orderType" :options="orderTypeOptions"
								:consistent-menu-width="false" />
						</n-form-item>
						<n-form-item label="支払方法">
							<n-input v-model:value="searchParams.payMethod" placeholder="クリック方法を選択" readonly
								@click="showDict('pay_method')" />
						</n-form-item>
					</n-form>
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="責任人" path="sellerName">
							<n-input-group>
								<n-input v-model:value="searchParams.sellerName" readonly placeholder="クリックド責任人を選択"
									@click="showStaff()"></n-input>
							</n-input-group>
						</n-form-item>
						<n-form-item label="ドライバー" path="driverName">
							<n-input-group>
								<n-input v-model:value="searchParams.driverName" readonly placeholder="クリックドライバーを選択"
									@click="showDriver()"></n-input>
							</n-input-group>
						</n-form-item>
						<n-form-item label="ツアー日" path="selTime">
							<n-date-picker v-model:value="searchParams.selTime" type="daterange" clearable @update:value="onUpdate" />
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
			<n-space>
				<n-button type="primary" @click="handleBatchFinance(0)">一括確認</n-button>
				<n-button type="primary" @click="handleBatchFinance(1)">一括決算</n-button>
				<n-button type="primary" @click="handleExport">ダウンロード</n-button>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table size="small" :row-key="rowKey" :summary="summary" remote bordered
					:v-model:checked-row-keys="checkedRowKeys" :columns="columns" :data="dataSource" :pagination="pagination"
					:scroll-x="2000" :single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }"
					@update:checked-row-keys="handleChecked" @update:page="handlePageChange" @update:sorter="handleSorter"
					@update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<staff-select-modal ref="staffModal" @click="selectStaff" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, DataTableCreateSummary, NButton, NEllipsis, NTag, useMessage, useLoadingBar } from 'naive-ui';
import moment from 'moment';
import { RowData } from 'naive-ui/es/data-table/src/interface';
import { EnumOrderType, EnumFinanceType, EnumFinanceStatus } from '@/enum';
import { useMyTags, useMyCommon, useMyOptions } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const { orderTypeOptions } = useMyOptions();

const module = 'finance/payRecord/cash';
const moduleParams: MySearch.PaySearchParams = {
	keyword: '',
	financeType: ['InEarnings', 'InTemp', 'InAdvance'],
	driverName: '',
	sellerName: '',
	orderType: '',
	selTime: [Date.now(), Date.now()],
	startBeginTime: Date.now(),
	startEndTime: Date.now(),
	isAudit: true
};

const {
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	handleExport,
	handleSorter,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.PayRecord>(module, moduleParams);
resetParams();

const { financeStatusTagType } = useMyTags();
const { isMobile, isWrap, addSeparator } = useMyCommon();
const message = useMessage();
const rowKey = (row: RowData) => row.id;
const checkedRowKeys = ref<Array<string | number>>([]);
const loadingBar = useLoadingBar();

const urls = {
	cashPaid: `/order/cashPaid`,
	settlement: `/order/settlement`
};

const handelPaid = (ids: Array<string | number>) => {
	const params = {
		ids
	};
	const promise = request.post<Boolean>(`${urls.cashPaid}`, params);
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				message.success('確認しました');
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
		});
};
const handleSettlement = (ids: Array<string | number>) => {
	const params = {
		ids
	};
	const promise = request.post<Boolean>(`${urls.settlement}`, params);
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				message.success('決算しました');
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
		});
};
const handleBatchFinance = (flag: number) => {
	if (checkedRowKeys.value.length === 0) {
		message.warning('データ選択してください');
		return;
	}
	if (flag === 0) {
		handelPaid(checkedRowKeys.value);
	} else if (flag === 1) {
		handleSettlement(checkedRowKeys.value);
	}
};

const handleChecked = (
	keys: Array<string | number>,
	rows: object[],
	meta: { row: object | undefined; action: 'check' | 'uncheck' | 'checkAll' | 'uncheckAll' }
) => {
	checkedRowKeys.value = keys;
};

const summary: DataTableCreateSummary = pageData => {
	const a = (pageData as RowData[]).reduce((prevValue, row) => prevValue + row.amount, 0);
	const ea = (pageData as RowData[]).reduce((prevValue, row) => prevValue + row.expectedAmount, 0);
	return {
		currencyAmount: {
			value: h('div', { class: 'text-red font-bold text-right' }, { default: () => '総額' }),
			colSpan: 1
		},
		amount: {
			value: h('div', { class: 'text-right' }, { default: () => `¥${a}` }),
			colSpan: 1
		},
		expectedAmount: {
			value: h('div', { class: 'text-right' }, { default: () => `¥${ea}` }),
			colSpan: 1
		}
	};
};
const columns: DataTableColumn<MyModel.PayRecord>[] = [
	{
		type: 'selection',
		disabled(row) {
			return EnumFinanceStatus[row.status ?? 'Waiting'] === EnumFinanceStatus.Completed;
		}
	},
	{
		title: 'タイプ',
		key: 'financeType',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return `${EnumFinanceType[row.financeType ?? '']}`;
		}
	},
	{
		title: 'ツアー日',
		key: 'startTime',
		align: 'center',
		sorter: true,
		width: 100,
		render(row) {
			return row.orderVO?.startTime ? `${moment(row.orderVO?.startTime).format('yyyy/MM/DD')}` : '未定';
		}
	},
	{
		title: '責任人',
		key: 'sellerName',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return `${row.orderVO?.sellerName ? row.orderVO?.sellerName : '未定'}`;
		}
	},
	{
		title: '注文番号',
		key: 'orderNo',
		align: 'center',
		sorter: true,
		width: 150,
		render(row) {
			return `${row.orderVO?.orderNo}`;
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
				'div',
				{},
				{
					default: () => status ?? '未定'
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
			return [h('div', {}, { default: () => (row.orderVO?.driverName ? row.orderVO?.driverName : '未定') })];
		}
	},
	{
		title: '名目',
		key: 'payItem',
		align: 'center',
		width: 100
	},
	{
		title: '通貨',
		key: 'currency',
		align: 'center',
		width: 100
	},
	{
		title: '通貨金額',
		key: 'currencyAmount',
		align: 'center',
		width: 150,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.currencyAmount ?? 0)}` })];
		}
	},
	{
		title: '换算日元',
		key: 'amount',
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', { class: 'text-right' }, { default: () => `¥${addSeparator(row.amount ?? 0)}` })];
		}
	},
	{
		title: '支払方法',
		key: 'payMethod',
		sorter: true,
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', {}, { default: () => (row.payMethod ? row.payMethod : '未定') })];
		}
	},
	// {
	//   title: '金融機関',
	//   key: 'bank',
	//   sorter: true,
	//   align: 'center',
	//   width: 100,
	//   render(row) {
	//     return [h('div', {}, { default: () => (row.bank ? row.bank : '-') })];
	//   }
	// },
	{
		title: '備考',
		key: 'remark',
		align: 'center',
		width: 300,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.remark ?? '-' })];
		}
	},
	{
		title: 'ステータス',
		width: 100,
		key: 'status',
		align: 'center',
		render(row) {
			const status = row?.status as MyEnumType.EnumFinanceStatusKey;
			const tagType = financeStatusTagType(status);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => row.statusName
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
			const paidOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handelPaid([row.id])
				},
				{ default: () => '確認' }
			);
			const setOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleSettlement([row.id])
				},
				{ default: () => '決算' }
			);
			let option;
			switch (EnumFinanceStatus[row.status ?? 'Waiting']) {
				case EnumFinanceStatus.Waiting:
					option = paidOption;
					break;
				case EnumFinanceStatus.Paid:
					option = setOption;
					break;
				case EnumFinanceStatus.Completed:
					option = '-';
					break;
				default:
					break;
			}
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
		case 'pay_method':
			searchParams.payMethod = result.text;
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

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	searchParams.startBeginTime = value ? value[0] : undefined;
	searchParams.startEndTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;
	searchQuery();
});
</script>
<style scoped></style>
