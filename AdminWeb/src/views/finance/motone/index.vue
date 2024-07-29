<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="注文番号"
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
			<n-space>
				<n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="2000"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<dict-select-modal ref="dictModal" @click="selectDict" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NNumberAnimation, NTag } from 'naive-ui';
import { useMyCommon, useMyTags } from '@/composables';
import { useDataTable } from '@/hooks';
import { EnumStatus } from '@/enum/business';

const module = 'operate/accident';
const moduleParams: MySearch.AccidentSearchParams = {
	keyword: '',
	accidentType: '',
	accidentTypeCode: '',
	status: ['Waiting']
};

const {
	delModal,
	aoeModal,
	handleAdd,
	handleEdit,
	handleDelete,
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	handleExport,
	searchParams,
	dataSource,
	pagination,
	loading,
	empty
} = useDataTable<MyModel.Accident>(module, moduleParams);
resetParams();

const { statusTagType } = useMyTags();
const { isMobile, isWrap } = useMyCommon();

const columns: DataTableColumn<MyModel.Accident>[] = [
	{
		title: '手数料',
		key: 'carNo',
		width: 150,
		align: 'center'
	},
	{
		title: '燃費',
		key: 'accidentType',
		width: 150,
		align: 'center'
	},
	{
		title: '業務委託',
		key: 'confirmBy',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.confirmBy ?? '-' })];
		}
	},
	{
		title: '給与',
		key: 'responsible',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.responsible ?? '-' })];
		}
	},
	{
		title: '消費税対象額',
		key: 'proportion',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.proportion ?? '-' })];
		}
	},
	{
		title: '消費税額',
		key: 'repairBy',
		width: 150,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.repairBy ?? '-' })];
		}
	},
	{
		title: '粗利益',
		key: 'repairTime',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.repairTime ?? '-' })];
		}
	},
	{
		title: '利益率（%）',
		width: 150,
		key: 'amount',
		align: 'center',
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
				{
					default: () => row.amount
				}
			);
			return [h('span', {}, { default: () => '¥' }), numberOption];
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
		case 'operate_accident_type':
			searchParams.accidentType = result.text;
			searchParams.accidentTypeCode = result.value;
			break;
		default:
			break;
	}
};
const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;

	// searchQuery();
});
</script>
<style scoped></style>
