<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="車両番号/事故者/確認者"
							clearable />
					</n-form-item>
					<n-form-item label="事故性質">
						<n-input v-model:value="searchParams.accidentType" placeholder="クリック性質を選択" readonly
							@click="showDict('operate_accident_type')" />
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
		title: '車両番号',
		key: 'carNo',
		width: 150,
		align: 'center'
	},
	{
		title: '事故性質',
		key: 'accidentType',
		width: 150,
		align: 'center'
	},
	{
		title: '確認者',
		key: 'confirmBy',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.confirmBy ?? '-' })];
		}
	},
	{
		title: '主责任方',
		key: 'responsible',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.responsible ?? '-' })];
		}
	},
	{
		title: '事故比例',
		key: 'proportion',
		width: 100,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.proportion ?? '-' })];
		}
	},
	{
		title: '修理業者',
		key: 'repairBy',
		width: 150,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.repairBy ?? '-' })];
		}
	},
	{
		title: '修理終了日時',
		key: 'repairTime',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.repairTime ?? '-' })];
		}
	},
	{
		title: '総額',
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
	},
	{
		title: '会社負担',
		width: 150,
		key: 'companyAmount',
		align: 'center',
		render(row) {
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.companyAmount,
					to: row.companyAmount,
					precision: 0,
					active: false
				},
				{}
			);
			return [h('span', {}, { default: () => '¥' }), numberOption];
		}
	},
	{
		title: '保険負担',
		key: 'insuranceAmount',
		width: 150,
		align: 'center',
		render(row) {
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.insuranceAmount,
					to: row.insuranceAmount,
					precision: 0,
					active: false
				},
				{}
			);
			return [h('span', {}, { default: () => '¥' }), numberOption];
		}
	},
	{
		title: '個人負担',
		width: 150,
		key: 'driverAmount',
		align: 'center',
		render(row) {
			const numberOption = h(
				NNumberAnimation,
				{
					showSeparator: true,
					from: row.driverAmount,
					to: row.driverAmount,
					precision: 0,
					active: false
				},
				{}
			);
			return [h('span', {}, { default: () => '¥' }), numberOption];
		}
	},
	{
		title: '事故通知日時',
		key: 'noticeTime',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.noticeTime ?? '-' })];
		}
	},
	{
		title: '財務通知日時',
		key: 'financeNoticeTime',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.financeNoticeTime ?? '-' })];
		}
	},
	{
		title: '精算日時',
		key: 'financeTime',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.financeTime ?? '-' })];
		}
	},
	{
		title: '精算者',
		key: 'financeByName',
		width: 120,
		align: 'center',
		render(row) {
			return [h('div', {}, { default: () => row.financeTime ?? '-' })];
		}
	},
	{
		title: 'ステータス',
		width: 100,
		key: 'status',
		align: 'center',
		render(row) {
			const status = row.status as MyEnumType.EnumStatusKey;
			const tagType = statusTagType(status);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => EnumStatus[status]
				}
			);
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

	searchQuery();
});
</script>
<style scoped></style>
