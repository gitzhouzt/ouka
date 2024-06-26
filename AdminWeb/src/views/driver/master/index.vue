<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="番号/名前/運転免許"
							clearable />
					</n-form-item>
					<n-form-item label="タイプ">
						<n-input v-model:value="searchParams.driverType" placeholder="クリックタイプを選択" readonly
							@click="showDict('driver_type')" />
					</n-form-item>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="searchReset">リセット</n-button>
						<n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="1000"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" :row-class-name="rowClassName"
					@update:page="handlePageChange" @update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<add-or-edit ref="aoeModal" @close="searchQuery" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NTag } from 'naive-ui';
import moment from 'moment';
import { EnumUserStatus } from '@/enum';
import { useMyOptions, useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit } from './components';

const module = 'driver';
const moduleParams: MySearch.DriveSearchParams = {
	keyword: '',
	userRole: 'Driver',
	driverType: ''
};

const {
	aoeModal,
	handleEdit,
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
} = useDataTable<MyModel.Driver>(module, moduleParams);
resetParams();

const { userRoleOptions } = useMyOptions();
const { userStatusTagType } = useMyTags();
const { isMobile } = useMyCommon();
const rowClassName = (row: MyModel.Driver) => {
	if (
		moment(row.expiryDate).diff(moment(), 'days') <= 7 ||
		moment(row.healthyDate).add(1, 'years').diff(moment(), 'days') <= 30 ||
		!row.expiryDate ||
		!row.healthyDate
	) {
		return 'expired';
	}
	return '';
};
const columns: DataTableColumn<MyModel.Driver>[] = [
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
		title: 'ドライバータイプ',
		key: 'driverType',
		width: 150,
		align: 'center',
		render(row) {
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.driverType ?? '-' })];
		}
	},
	{
		title: '電話',
		key: 'userPhone',
		align: 'center',
		width: 150,
		render(row) {
			const { userVO } = row;
			const phone = `${userVO?.userPhone ? `+${userVO?.countryNum} ${userVO?.userPhone}` : '-'}`;
			return phone;
		}
	},
	{
		title: 'メール',
		key: 'userEmail',
		width: 150,
		align: 'center',
		render(row) {
			const { userVO } = row;
			return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => userVO?.userEmail ?? '-' })];
		}
	},
	{
		title: '運転免許番号',
		key: 'driverLicense',
		width: 150,
		align: 'center',
		render(row) {
			const option = [
				h(
					'div',
					{
						class: `${row.driverLicense ?? 'text-red'}`
					},
					{ default: () => row.driverLicense ?? '未記入' }
				)
			];
			return option;
		}
	},
	{
		title: '免許有効期限',
		key: 'expiryDate',
		sorter: true,
		width: 100,
		align: 'center',
		render(row) {
			const option = [
				h(
					'div',
					{
						class: `${!row.expiryDate || moment(row.expiryDate).diff(moment(), 'days') <= 7 ? 'text-red' : ''}`
					},
					{ default: () => (row.expiryDate ? moment(row.expiryDate).format('yyyy/MM/DD') : '未記入') }
				)
			];
			return option;
		}
	},
	{
		title: '前回健康診断日',
		key: 'healthyDate',
		sorter: true,
		width: 100,
		align: 'center',
		render(row) {
			const option = [
				h(
					'div',
					{
						class: `${!row.healthyDate || moment(row.healthyDate).add(1, 'years').diff(moment(), 'days') <= 30 ? 'text-red' : ''
							}`
					},
					{ default: () => (row.healthyDate ? moment(row.healthyDate).format('yyyy/MM/DD') : '未診断') }
				)
			];
			return option;
		}
	},
	{
		title: '免許タイプ',
		key: 'licenseType',
		width: 150,
		align: 'center',
		render(row) {
			const option = [
				h(
					'div',
					{
						class: `${row.licenseType ?? 'text-red'}`
					},
					{ default: () => row.licenseType ?? '未記入' }
				)
			];
			return option;
		}
	},

	{
		title: 'ステータス',
		width: 100,
		key: 'status',
		sorter: true,
		align: 'center',
		render(row) {
			const { userVO } = row;
			const userStatus = userVO?.status as MyEnumType.EnumUserStatusKey;
			const status = EnumUserStatus[userStatus];
			const tagType = userStatusTagType(userStatus);
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
		title: 'アカウント',
		key: 'audit',
		width: 80,
		align: 'center',
		render(row) {
			const { userVO } = row;
			const tipsOption = h(
				NTag,
				{
					type: userVO?.isAudit ? 'success' : 'error',
					round: true
				},
				{
					default: () => (userVO?.isAudit ? '有効' : '無効')
				}
			);
			return tipsOption;
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
					onClick: () => handleEdit('ドライバー編集', row)
				},
				{ default: () => '編集' }
			);
			return editOption;
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
		case 'driver_type':
			searchParams.driverType = result.text;
			break;
		case 'order_city':
			searchParams.city = result.text;
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
<style scoped>
:deep(.expired td) {
	background: rgba(250, 199, 199, 0.75) !important;
}
</style>
