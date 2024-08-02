<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="車両番号"
							clearable />
					</n-form-item>
					<n-form-item label="年度">
						<n-date-picker v-model:value="datePicker.date" :type="datePicker.type" clearable @update:value="onUpdate" />
					</n-form-item>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="onReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" :row-class-name="rowClassName"
					@update:page="handlePageChange" @update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<add-or-edit ref="aoeModal" @close="searchQuery" />
		<last-check ref="lastModal" @close="searchQuery" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis } from 'naive-ui';
import moment from 'moment';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit, LastCheck } from './components';

const module = 'car/check';
const moduleParams: MySearch.CarSearchParams = {
	keyword: ''
};
const datePicker = ref({
	type: 'year',
	date: new Date().valueOf()
});
const {
	aoeModal,
	handleEdit,
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	searchParams,
	handleSorter,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.CarCheck>(module, moduleParams);
resetParams();

const lastModal = ref<any>(null);
const handleLastCheck = (title: string, row: MyModel.CarCheck) => {
	lastModal.value.setTitle(title);
	lastModal.value.showModal(row);
};

const { isMobile } = useMyCommon();
const rowClassName = (row: MyModel.CarCheck) => {
	// if (
	//   moment(row.expiryDate).diff(moment(), 'days') <= 7 ||
	//   moment(row.healthyDate).add(1, 'years').diff(moment(), 'days') <= 30
	// ) {
	//   return 'expired';
	// }
	return '';
};
const columns: DataTableColumn<MyModel.CarCheck>[] = [
	{
		title: '車両番号',
		key: 'carNo',
		align: 'center',
		sorter: true
	},
	{
		title: '年度',
		key: 'year',
		width: 80,
		align: 'center'
	},
	{
		title: '前回点検（年）',
		key: 'lastYearCheckDate',
		width: 116,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.lastYearCheckDate ? 'text-red' : ''}`
					},
					{ default: () => (row.lastYearCheckDate ? moment(row.lastYearCheckDate).format('yyyy/MM/DD') : '未設定') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleLastCheck(`前回年検 ー  ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '前回点検',
		key: 'lastMonthCheckDate',
		width: 100,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.lastMonthCheckDate ? 'text-red' : ''}`
					},
					{ default: () => (row.lastMonthCheckDate ? moment(row.lastMonthCheckDate).format('yyyy/MM/DD') : '未設定') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleLastCheck(`前回点検 ー ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '点検（年）',
		key: 'yearCheckDate',
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.yearCheckStatus ? 'text-red' : ''}`
					},
					{ default: () => (row.yearCheckDate ? moment(row.yearCheckDate).format('yyyy/MM/DD') : '-') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.year}年 ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '１回目点検',
		key: 'monthCheckDate1',
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.checkStatus1 ? 'text-red' : ''}`
					},
					{ default: () => (row.monthCheckDate1 ? moment(row.monthCheckDate1).format('yyyy/MM/DD') : '未設定') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.year}年 ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '２回目点検',
		key: 'monthCheckDate2',
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.checkStatus2 ? 'text-red' : ''}`
					},
					{ default: () => (row.monthCheckDate2 ? moment(row.monthCheckDate2).format('yyyy/MM/DD') : '未設定') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.year}年 ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '３回目点検',
		key: 'monthCheckDate3',
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${!row.checkStatus3 ? 'text-red' : ''}`
					},
					{ default: () => (row.monthCheckDate3 ? moment(row.monthCheckDate3).format('yyyy/MM/DD') : '未設定') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.year}年 ${row.carNo}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	}
	// {
	//   title: '第四回',
	//   key: 'monthCheckDate4',
	//   width: 100,
	//   align: 'center',
	//   render(row) {
	//     const tipsOption = [
	//       h(
	//         'div',
	//         {
	//           class: `${row.checkStatus4 !== 'Completed' ? 'text-red' : ''}`
	//         },
	//         { default: () => (row.monthCheckDate4 ? moment(row.monthCheckDate4).format('yyyy/MM/DD') : '未設定') }
	//       )
	//     ];
	//     const editOption = h(
	//       NButton,
	//       {
	//         quaternary: true,
	//         size: 'small',
	//         type: 'info',
	//         onClick: () => handleEdit(`${row.year}年 ${row.carNo}`, row)
	//       },
	//       { default: () => tipsOption }
	//     );
	//     return editOption;
	//   }
	// }
];

const onUpdate = (value: string | number | boolean) => {
	searchParams.trainYear = `${moment(datePicker.value.date).format('yyyy')}`;
};

const onReset = () => {
	// datePicker.value.date = new Date().valueOf();
	datePicker.value.date = null;
	searchReset();
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;

	searchQuery();
});
</script>
<style scoped>
:deep(.expired td) {
	background: rgba(250, 199, 199, 0.75) !important;
}
</style>
