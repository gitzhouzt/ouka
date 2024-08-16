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
					<n-form-item label="注文内容">
						<n-input v-model:value="searchParams.orderType" placeholder="クリック内容を選択" readonly
							@click="showDict('order_type')" />
					</n-form-item>
					<n-form-item label="責任人" path="sellerName">
						<n-input-group>
							<n-input v-model:value="searchParams.sellerName" readonly placeholder="クリックド責任人を選択"
								@click="showStaff()"></n-input>
						</n-input-group>
					</n-form-item>
				</n-form>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
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
			<!-- <n-space>
        <n-button type="primary" @click="handleBatchFinance">一括精算</n-button>
        <n-button type="primary" @click="handleExport">ダウンロード</n-button>
      </n-space> -->
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table :row-key="rowKey" :summary="summary" remote bordered :v-model:checked-row-keys="checkedRowKeys"
					:columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="2000" :single-line="false"
					:flex-height="true" :style="{ height: hightRef + 'px' }" @update:checked-row-keys="handleChecked"
					@update:page="handlePageChange" @update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<staff-select-modal ref="staffModal" @click="selectStaff" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
		<paid-action ref="paidModal" @close="searchQuery" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import {
	DataTableColumn,
	DataTableCreateSummary,
	NButton,
	NEllipsis,
	NNumberAnimation,
	NTag,
	useMessage
} from 'naive-ui';
import moment from 'moment';
import { RowData } from 'naive-ui/es/data-table/src/interface';
import { EnumOrderType, EnumFinanceType, EnumFinanceStatus } from '@/enum';
import { useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { PaidAction, SettlementAction } from './components';

const module = 'finance/payRecord';
const moduleParams: MySearch.PaySearchParams = {
	keyword: '',
	financeType: ['InTemp'],
	payItem: '',
	driverName: '',
	sellerName: '',
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
const { isMobile, isWrap } = useMyCommon();
const message = useMessage();
const rowKey = (row: RowData) => row.id;
const checkedRowKeys = ref<Array<string | number>>([]);

const paidModal = ref<any>(null);
const handlePaid = (title: string, ids: Array<string | number>) => {
	paidModal.value.setTitle(title);
	paidModal.value.showModal(ids);
};
const handleBatchFinance = () => {
	if (checkedRowKeys.value.length === 0) {
		message.warning('データ選択してください');
		return;
	}
	paidModal.value.setTitle('入金精算 - 一括精算');
	paidModal.value.showModal(checkedRowKeys.value);
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
	const aOption = h(
		NNumberAnimation,
		{
			showSeparator: true,
			from: a,
			to: a,
			precision: 0,
			active: false
		},
		{}
	);
	const eaOption = h(
		NNumberAnimation,
		{
			showSeparator: true,
			from: ea,
			to: ea,
			precision: 0,
			active: false
		},
		{}
	);
	return {
		currencyAmount: {
			value: [h('span', { class: 'text-red font-bold' }, { default: () => '総額' })],
			colSpan: 1
		},
		amount: {
			value: [h('span', {}, { default: () => '¥' }), aOption],
			colSpan: 1
		},
		expectedAmount: {
			value: [h('span', {}, { default: () => '¥' }), eaOption],
			colSpan: 1
		}
	};
};
const columns: DataTableColumn<MyModel.PayRecord>[] = [
	// {
	//   type: 'selection',
	//   disabled(row) {
	//     return EnumFinanceStatus[row.status ?? 'Waiting'] === EnumFinanceStatus.Completed;
	//   }
	// },
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
		title: '团号',
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
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.currencyAmount,
					to: row.currencyAmount,
					precision: 0,
					active: false
				},
				{}
			);
			return [h('span', {}, { default: () => '¥' }), numberOption];
		}
	},
	{
		title: '换算日元',
		key: 'amount',
		align: 'center',
		width: 100,
		render(row) {
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.amount,
					to: row.amount,
					precision: 0,
					active: false
				},
				{}
			);
			return row.amount ? [h('span', {}, { default: () => '¥' }), numberOption] : '-';
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
	{
		title: '金融機関',
		key: 'bank',
		sorter: true,
		align: 'center',
		width: 100,
		render(row) {
			return [h('div', {}, { default: () => (row.bank ? row.bank : '-') })];
		}
	},
	{
		title: '備考',
		key: 'remark',
		align: 'center',
		width: 300,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.remark ?? '-' })];
		}
	}
	// {
	//   title: 'ステータス',
	//   width: 100,
	//   key: 'status',
	//   align: 'center',
	//   render(row) {
	//     const status = row?.status as MyEnumType.EnumFinanceStatusKey;
	//     const tagType = financeStatusTagType(status);
	//     return h(
	//       NTag,
	//       {
	//         type: tagType,
	//         round: true
	//       },
	//       {
	//         default: () => EnumFinanceStatus[status]
	//       }
	//     );
	//   }
	// },
	// {
	//   title: '操作',
	//   key: 'actions',
	//   width: 80,
	//   align: 'center',
	//   render(row) {
	//     const paidOption = h(
	//       NButton,
	//       {
	//         quaternary: true,
	//         size: 'small',
	//         type: 'info',
	//         onClick: () => handlePaid(`入金確認 - ${row.orderVO?.orderNo}`, [row.id])
	//       },
	//       { default: () => '確認' }
	//     );
	//     const setOption = h(
	//       NButton,
	//       {
	//         quaternary: true,
	//         size: 'small',
	//         type: 'info',
	//         onClick: () => handlePaid(`入金決算 - ${row.orderVO?.orderNo}`, [row.id])
	//       },
	//       { default: () => '決算' }
	//     );
	//     let option;
	//     switch (EnumFinanceStatus[row.status ?? 'Waiting']) {
	//       case EnumFinanceStatus.Waiting:
	//         option = paidOption;
	//         break;
	//       case EnumFinanceStatus.Paid:
	//         option = setOption;
	//         break;
	//       case EnumFinanceStatus.Completed:
	//         option = '-';
	//         break;
	//       default:
	//         break;
	//     }
	//     return option;
	//   }
	// }
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
