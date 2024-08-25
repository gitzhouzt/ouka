<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-space>
				<n-button type="primary" @click="handleAdd('料金記録')">新規記録</n-button>
			</n-space>
			<n-space class="mt-4">
				<loading-empty-wrapper class="h-620px" :empty="empty" :loading="loading">
					<n-data-table striped remote :loading="loading" :columns="columns" :data="dataSource" :pagination="pagination"
						:flex-height="true" :scroll-x="1500" class="h-620px" @update:page="handlePageChange" />
				</loading-empty-wrapper>
			</n-space>
			<add-or-edit ref="aoeModal" :model="modelRef" @close="searchQuery" />
			<del-action ref="delModal" @close="searchQuery" />
		</n-modal>
	</div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { DataTableColumn, NEllipsis, NButton, NSwitch, NNumberAnimation } from 'naive-ui';
import { EnumFinanceType } from '@/enum';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { AddOrEdit, DelAction } from './components';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
	width: '1200px'
});
const module = 'finance/payRecord';
const moduleParams: MySearch.PaySearchParams = {
	keyword: '',
	orderId: ''
};

const {
	handlePageChange,
	resetParams,
	searchQuery,
	handleEdit,
	handleDelete,
	handleAdd,
	aoeModal,
	delModal,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Order>(module, moduleParams);

resetParams();

const modelRef = ref<MyModel.Order>();
const showModalRef = ref<boolean | undefined>(false);

const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
	titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
	if (!show) {
		emits('close');
	}
};
const urls = {
	confirmPay: `/order/confirmPay`
};

const showModal = (row: MyModel.Order) => {
	showModalRef.value = true;
	modelRef.value = row;
	searchParams.orderId = row.id;
	searchQuery();
};

const handleAudit = (row: any) => {
	loading.value = true;
	const promise = request.put<boolean>(`${urls.confirmPay}/${row.id}`);
	const { isAudit } = row;
	const objStr = row.payItem ? ` ${row.payItem} ` : '';
	promise
		.then(res => {
			if (res.data) {
				window.$message?.success(`${isAudit ? `${objStr}を未承認` : `${objStr}を承認`}しました`);
			}
		})
		.finally(() => {
			loading.value = false;
			searchQuery();
		});
};

const columns: DataTableColumn<MyModel.PayRecord>[] = [
	{
		title: '確認',
		key: 'audit',
		width: 80,
		align: 'center',
		render(row) {
			return [
				h(
					NSwitch,
					{
						onClick: () => handleAudit(row),
						value: row.isAudit
					},
					{
						// default: () => row.isAudit,
						checked: () => '承認',
						unchecked: () => '未承認'
					}
				)
			];
		}
	},
	{
		title: '責任人',
		key: 'sellerName',
		align: 'center',
		width: 100,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.sellerName ?? '-' })];
		}
	},
	{
		title: '記入者',
		key: 'createByName',
		align: 'center',
		width: 100,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.createByName ?? '-' })];
		}
	},
	{
		title: 'タイプ',
		key: 'financeType',
		align: 'center',
		width: 80,
		render(row) {
			const financeType = row.financeType as MyEnumType.EnumFinanceTypeKey;
			return EnumFinanceType[financeType];
		}
	},
	{
		title: '名目',
		key: 'payItem',
		align: 'center',
		width: 100
	},
	{
		title: '方法',
		key: 'payMethod',
		align: 'center',
		width: 100,
		render(row) {
			return [h('span', {}, { default: () => (row.payMethod ? row.payMethod : '-') })];
		}
	},
	{
		title: '通貨',
		key: 'currency',
		align: 'center',
		width: 50
	},
	{
		title: '金額',
		key: 'currencyAmount',
		align: 'center',
		width: 100,
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
			const flg = row.currencyCode === 'usd' ? '$' : '￥';
			return row.currencyAmount ? [h('span', {}, { default: () => flg }), numberOption] : '-';
		}
	},
	{
		title: '日元',
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
			return row.amount ? [h('span', {}, { default: () => '¥' }), numberOption] : '未設定';
		}
	},
	{
		title: '備考',
		key: 'remark',
		align: 'center',
		width: 100,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.remark ?? '-' })];
		}
	},
	{
		title: '編集',
		key: 'edit',
		width: 80,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit('料金記録', row)
				},
				{ default: () => '編集' }
			);
			return option;
		}
	},
	{
		title: '操作',
		key: 'actions',
		width: 80,
		align: 'center',
		render(row) {
			const deleteOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'error',
					onClick: () => handleDelete('料金記録', row)
				},
				{ default: () => '削除' }
			);
			return deleteOption;
		}
	}
];

defineExpose({
	showModal,
	setTitle
});
</script>

<style scoped></style>
