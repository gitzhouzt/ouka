<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-space>
				<n-button type="primary" @click="handleAdd('車備品')">新規記録</n-button>
			</n-space>
			<n-space class="mt-4">
				<loading-empty-wrapper :loading="loading" class="h-620px" :empty="empty">
					<n-data-table striped remote :columns="columns" :data="dataSource" :pagination="pagination"
						:flex-height="true" class="h-620px" @update:page="handlePageChange" />
				</loading-empty-wrapper>
			</n-space>
			<add-or-edit ref="aoeModal" :model="modelRef" @close="searchQuery" />
		</n-modal>
	</div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { useMessage, useLoadingBar, DataTableColumn, NEllipsis, NButton } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { AddOrEdit, DelAction } from './components';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
	width: '1200px'
});
const module = 'order/goods';
const moduleParams: MySearch.SearchParams = {
	keyword: ''
};

const {
	handlePageChange,
	searchQuery,
	handleAdd,
	resetParams,
	aoeModal,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.OrderGoods>(module, moduleParams);

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
const message = useMessage();
const loadingBar = useLoadingBar();

const showModal = (row: MyModel.OrderGoods) => {
	showModalRef.value = true;
	modelRef.value = row;
	searchParams.orderId = row.id;
	searchQuery();
};

const urls = {
	deletePhysics: `/order/goods/deletePhysics`
};
const handleDelete = (row: MyModel.OrderGoods) => {
	const promise = request.delete<Boolean>(`${urls.deletePhysics}/${row.id}`);
	loadingBar.start();
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				message.success('削除しました');
				searchQuery();
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

const columns: DataTableColumn<MyModel.OrderGoods>[] = [
	{
		title: '備品',
		key: 'goodsType',
		align: 'center',
		width: 100
	},
	{
		title: '数量',
		key: 'amount',
		align: 'center',
		width: 30,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.amount ? row.amount : '-') }
				)
			];
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
					onClick: () => handleDelete(row)
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
