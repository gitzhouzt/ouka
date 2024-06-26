<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="番号/名前"
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
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="1000"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" :row-class-name="rowClassName"
					@update:page="handlePageChange" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<add-or-edit ref="aoeModal" @close="searchQuery" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis } from 'naive-ui';
import moment from 'moment';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit } from './components';

const module = 'train';
const moduleParams: MySearch.TrainSearchParams = {
	keyword: '',
	trainYear: ''
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
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Train>(module, moduleParams);
resetParams();

const { isMobile } = useMyCommon();
const rowClassName = (row: MyModel.Train) => {
	// if (
	//   moment(row.expiryDate).diff(moment(), 'days') <= 7 ||
	//   moment(row.healthyDate).add(1, 'years').diff(moment(), 'days') <= 30
	// ) {
	//   return 'expired';
	// }
	return '';
};
const columns: DataTableColumn<MyModel.Train>[] = [
	{
		title: '番号',
		key: 'userNo',
		align: 'center',
		width: 100,
		render(row) {
			const { userVO } = row;
			return userVO?.userNo;
		}
	},
	{
		title: '名前',
		key: 'userName',
		align: 'center',
		width: 100,
		render(row) {
			const { userVO } = row;
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => userVO?.userName ?? '-' })];
		}
	},
	{
		title: '年度',
		key: 'trainYear',
		width: 100,
		align: 'center'
	},
	{
		title: '第一回',
		key: 'trainDate1',
		width: 100,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${row.trainDate1 ?? 'text-red'}`
					},
					{ default: () => (row.trainDate1 ? moment(row.trainDate1).format('yyyy/MM/DD') : '未培训') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.trainYear}年 ${row.userVO?.userName}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '第二回',
		key: 'trainDate2',
		width: 100,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${row.trainDate2 ?? 'text-red'}`
					},
					{ default: () => (row.trainDate2 ? moment(row.trainDate2).format('yyyy/MM/DD') : '未培训') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.trainYear}年 ${row.userVO?.userName}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},
	{
		title: '第三回',
		key: 'trainDate3',
		width: 100,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${row.trainDate3 ?? 'text-red'}`
					},
					{ default: () => (row.trainDate3 ? moment(row.trainDate3).format('yyyy/MM/DD') : '未培训') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.trainYear}年 ${row.userVO?.userName}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	},

	{
		title: '第四回',
		key: 'trainDate4',
		width: 100,
		align: 'center',
		render(row) {
			const tipsOption = [
				h(
					'div',
					{
						class: `${row.trainDate4 ?? 'text-red'}`
					},
					{ default: () => (row.trainDate4 ? moment(row.trainDate4).format('yyyy/MM/DD') : '未培训') }
				)
			];
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleEdit(`${row.trainYear}年 ${row.userVO?.userName}`, row)
				},
				{ default: () => tipsOption }
			);
			return editOption;
		}
	}
];

const onUpdate = (value: string | number | boolean) => {
	searchParams.trainYear = `${moment(datePicker.value.date).format('yyyy')}`;
};

const onReset = () => {
	datePicker.value.date = new Date().valueOf();
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
