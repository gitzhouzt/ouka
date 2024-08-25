<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-space>
				<n-button type="primary" @click="handleAdd('添付資料')">新規記録</n-button>
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
import { useMessage, useLoadingBar, DataTableColumn, NEllipsis, NButton, NSwitch } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { AddOrEdit, DelAction } from './components';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
	width: '1200px'
});
const module = 'order/file';
const moduleParams: MySearch.SearchParams = {
	keyword: ''
};

const {
	handlePageChange,
	searchQuery,
	resetParams,
	handleAdd,
	searchParams,
	pagination,
	aoeModal,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.OrderFile>(module, moduleParams);

resetParams();
const envConfig = getEnvConfig(import.meta.env);
const message = useMessage();
const loadingBar = useLoadingBar();
const modelRef = ref<MyModel.Order>();
const showModalRef = ref<boolean | undefined>(false);

const urls = {
	deletePhysics: `/order/file/deletePhysics`,
	setShare: `/order/file/setShare`
};

const showModal = (row: MyModel.Order) => {
	modelRef.value = row;
	showModalRef.value = true;
	searchParams.orderId = row.id;
	searchQuery();
};

const handleShare = (row: MyModel.OrderFile) => {
	loading.value = true;
	const promise = request.put<boolean>(`${urls.setShare}/${row.id}`);
	const { share } = row;
	const objStr = row.fileName ? ` ${row.fileName} ` : '';
	promise
		.then(res => {
			if (res.data) {
				window.$message?.success(`${!share ? `${objStr}を公開` : `${objStr}を非公開`}にしました`);
			}
		})
		.finally(() => {
			loading.value = false;
			searchQuery();
		});
};

const handleDelete = (row: MyModel.OrderFile) => {
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

const columns: DataTableColumn<MyModel.OrderFile>[] = [
	{
		title: '',
		key: 'share',
		align: 'center',
		width: 50,
		render(row) {
			return h(
				NSwitch,
				{ onClick: () => handleShare(row), value: row.share },
				{ checked: () => '公開', unchecked: () => '非公開' }
			);
		}
	},
	{
		title: 'ファイル名',
		key: 'fileName',
		align: 'center',
		width: 100,
		render(row) {
			const link = h(
				NEllipsis,
				{ lineClamp: 1, tooltip: true },
				{
					default: () =>
						h(
							'a',
							{ target: '_blank', href: `${envConfig.static}${row.fileUrl}`, class: 'cursor-pointer text-blue' },
							{ default: () => row.fileName }
						)
				}
			);
			return link;
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

const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
	titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
	if (!show) {
		emits('close');
	}
};

defineExpose({
	showModal,
	setTitle
});
</script>

<style scoped></style>
