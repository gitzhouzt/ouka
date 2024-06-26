<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="データ分類/備考"
							clearable />
					</n-form-item>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="searchReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<n-grid :cols="2">
				<n-grid-item>
					<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
						<n-data-table remote bordered :columns="typeColumns" :data="dataSource" :pagination="pagination"
							:scroll-x="800" :single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }"
							:row-props="rowProps" @update:checked-row-keys="onChecked" @update:page="handlePageChange"
							@update:page-size="handleUpdatePageSize" />
					</loading-empty-wrapper>
				</n-grid-item>
				<n-grid-item>
					<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="itemLoading" :empty="empty">
						<n-data-table remote bordered :columns="dictColumns" :data="itemData" :pagination="itemPagination"
							:scroll-x="800" :single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }"
							@update:page="handleItemPageChange" @update:page-size="handleItemUpdatePageSize" />
					</loading-empty-wrapper></n-grid-item>
			</n-grid>
		</n-space>
		<item-add-or-edit ref="itemAoeModal" @close="getItem()" />
		<item-del-action ref="itemDelModal" @close="getItem()" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, useLoadingBar, useMessage } from 'naive-ui';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { ItemAddOrEdit, ItemDelAction } from './components';

const module = 'base/dict';
const moduleParams: MySearch.DictSearchParams = {
	keyword: 'user',
	dictName: ''
};

const {
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
} = useDataTable<MyModel.Dict>(module, moduleParams);
resetParams();

const itemLoading = ref(false);

const itemPagination = ref({
	page: 1,
	pageCount: 1,
	pageSize: 30,
	itemCount: 0,
	showSizePicker: true,
	pageSizes: [10, 30, 50, 100],
	prefix({ itemCount }: { itemCount: any }) {
		return `合計 ${itemCount} 件`;
	}
});
const itemSearchParams = ref({
	page: itemPagination.value.page,
	pageSize: itemPagination.value.pageSize,
	dictCode: ''
});

const { isMobile } = useMyCommon();

const itemAoeModal = ref<any>(null);
const handleItemAdd = (title: string, row?: MyModel.DictItem) => {
	itemAoeModal.value.setTitle(title);
	itemAoeModal.value.showModal(row);
};

const itemDelModal = ref<any>(null);
const handleItemDel = (title: string, row?: MyModel.DictItem) => {
	itemDelModal.value.setTitle(title);
	itemDelModal.value.showModal(row);
};

const typeColumns: DataTableColumn<MyModel.Dict>[] = [
	{
		title: '分類',
		key: 'dictName',
		align: 'center'
	},
	{
		title: 'コード',
		key: 'dictCode',
		align: 'center'
	},
	{
		title: '備考',
		key: 'remark',
		align: 'center'
	},
	{
		title: '追加データ',
		key: 'add',
		width: 100,
		align: 'center',
		render(row) {
			const newRow: MyModel.DictItem = {
				id: '',
				dictId: row?.id,
				dictCode: row?.dictCode,
				itemName: '',
				itemCode: '',
				remark: ''
			};
			const addOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleItemAdd('データ追加', newRow)
				},
				{ default: () => '追加' }
			);
			return addOption;
		}
	}
];

const dictColumns: DataTableColumn<MyModel.DictItem>[] = [
	{
		title: '表示',
		key: 'itemName',
		align: 'center'
	},
	{
		title: 'コード',
		key: 'itemCode',
		align: 'center'
	},
	{
		title: '備考',
		key: 'remark',
		align: 'center'
	},
	{
		title: '編集',
		key: 'edit',
		width: 80,
		align: 'center',
		render(row) {
			const editOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleItemAdd('データ編集', row)
				},
				{ default: () => '編集' }
			);
			return editOption;
		}
	},
	{
		title: '操作',
		key: 'actions',
		width: 80,
		align: 'center',
		render(row) {
			const delOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'error',
					onClick: () => handleItemDel('データ', row)
				},
				{ default: () => '削除' }
			);
			return delOption;
		}
	}
];

const onChecked = (
	keys: Array<string | number>,
	rows: object[],
	meta: { row: object | undefined; action: 'check' | 'uncheck' | 'checkAll' | 'uncheckAll' }
) => {
	console.debug(keys);
	searchParams.typeCode = keys[0] ?? '';
	searchQuery();
};
const urls = {
	getTree: `base/dictItem/list`
};
const itemData = ref();
const loadingBar = useLoadingBar();
const message = useMessage();
const getItem = () => {
	const promise = request.post<MyModel.MyResultArray<MyModel.DictItem>>(`${urls.getTree}`, itemSearchParams.value);
	loadingBar.start();
	itemLoading.value = true;
	promise
		.then(res => {
			if (res.data) {
				const { list, page, pageCount, pageSize, itemCount } = res.data as MyModel.MyResultArray<MyModel.DictItem>;
				itemData.value = list;
				itemPagination.value.page = page;
				itemPagination.value.pageSize = pageSize ?? itemPagination.value.pageSize;
				itemPagination.value.pageCount = pageCount ?? itemPagination.value.pageCount;
				itemPagination.value.itemCount = itemCount ?? itemPagination.value.itemCount;
			}
		})
		.catch(err => {
			message.warning(err);
		})
		.finally(() => {
			itemLoading.value = false;
			loadingBar.finish();
		});
};

function handleItemUpdatePageSize(pageSize: number) {
	itemSearchParams.value.pageSize = pageSize;
	itemSearchParams.value.page = 1;
	getItem();
}

const handleItemPageChange = (currentPage: number) => {
	searchParams.page = currentPage;
	getItem();
};

const dictCode = ref<string>('');
const rowProps = (row: MyModel.Dict) => {
	return {
		onClick: (e: MouseEvent) => {
			dictCode.value = row.dictCode;
			itemSearchParams.value.dictCode = row.dictCode;
			getItem();
		}
	};
};
const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;

	searchParams.pageSize = 30;
	searchQuery();
});
</script>
<style scoped></style>
