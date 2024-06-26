<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="番号/車名/ナンバー"
							clearable />
					</n-form-item>
					<n-form-item label="車両タイプ">
						<n-input v-model:value="searchParams.carType" placeholder="クリックタイプを選択" readonly
							@click="showDict('car_type')" />
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
				<n-button type="primary" @click="handleAdd('車両登録')">車両登録</n-button>
				<n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="2000"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<add-or-edit ref="aoeModal" @close="searchQuery" />
		<del-action ref="delModal" @close="searchQuery" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NImage, NNumberAnimation, NStatistic, NTag } from 'naive-ui';
import { Icon } from '@iconify/vue';
import { useMyCommon, useMyTags } from '@/composables';
import { useDataTable } from '@/hooks';
import { EnumCarStatus } from '@/enum/business';
import { AddOrEdit, DelAction } from './components';
import { getEnvConfig } from '~/.env-config';

const module = 'car';
const moduleParams: MySearch.CarSearchParams = {
	keyword: '',
	carType: ''
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
	handleSorter,
	resetParams,
	handleExport,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Car>(module, moduleParams);
resetParams();
const { carStatusTagType } = useMyTags();
const { isMobile } = useMyCommon();
const envConfig = getEnvConfig(import.meta.env);

const columns: DataTableColumn<MyModel.Car>[] = [
	{
		title: '車両番号',
		key: 'carNo',
		sorter: true,
		width: 80,
		align: 'center'
	},
	{
		title: '車両写真',
		key: 'carPhoto',
		align: 'center',
		width: 100,
		render(row) {
			return h(
				NImage,
				{
					width: 80,
					src: `${envConfig.static}${row.carPhoto}`
				},
				{
					default: () => {
						//
					}
				}
			);
		}
	},
	{
		title: '車両名',
		key: 'carName',
		sorter: true,
		align: 'center',
		width: 100,
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.carName ?? '-' })];
		}
	},
	{
		title: 'タイプ',
		key: 'carType',
		sorter: true,
		align: 'center',
		width: 100
	},
	{
		title: 'ナンバー',
		key: 'plateNum',
		sorter: true,
		align: 'center',
		width: 100
	},
	{
		title: '座席数',
		key: 'carSeat',
		align: 'center',
		width: 60,
		render(row) {
			return [h('div', {}, { default: () => (row.carSeat ? `${row.carSeat}座` : '-') })];
		}
	},
	{
		title: '駐車場',
		key: 'carPark',
		sorter: true,
		align: 'center',
		width: 100
	},
	{
		title: '駐車料金',
		key: 'parkingFee',
		width: 100,
		align: 'center',
		render(row) {
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.parkingFee,
					to: row.parkingFee,
					precision: 0,
					active: false
				},
				{}
			);

			return row.parkingFee
				? [h('span', {}, { default: () => '¥' }), numberOption, h('span', {}, { default: () => ' /月' })]
				: '-';
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
		title: 'ステータス',
		width: 100,
		key: 'status',
		sorter: true,
		align: 'center',
		render(row) {
			const carStatus = row.status as MyEnumType.EnumCarStatusKey;
			const status = EnumCarStatus[carStatus];
			const tagType = carStatusTagType(carStatus);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => status
				}
			);
		}
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
					onClick: () => handleEdit('車両編集', row)
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
					onClick: () => handleDelete('車両', row)
				},
				{ default: () => '削除' }
			);

			return delOption;
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
		case 'car_type':
			searchParams.carType = result.text;
			break;
		default:
			break;
	}
};
const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;

	searchQuery();
});
</script>
<style scoped></style>
