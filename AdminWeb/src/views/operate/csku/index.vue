<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="番号/車両名"
							clearable />
					</n-form-item>
					<n-form-item label="時間">
						<n-date-picker v-model:value="datePicker.date" :type="datePicker.type" clearable />
					</n-form-item>
					<n-form-item>
						<n-radio-group v-model:value="searchParams.dateType" name="dateTypeRdo" @update:value="onUpdate">
							<n-radio-button key="day" value="day" default-checked label="日" />
							<n-radio-button key="month" value="month" label="月" />
							<n-radio-button key="year" value="year" label="年" />
						</n-radio-group>
					</n-form-item>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery(beforeFun, afterFun);
							}
							">検索</n-button>
						<n-button class="ml-2" @click="onReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="schedule" :pagination="pagination" :scroll-x="scrollX"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton } from 'naive-ui';
import moment from 'moment';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const { isMobile } = useMyCommon();

const module = 'operate/schedule';
const moduleParams: MySearch.SkuSearchParams = {
	keyword: '',
	targetType: 'Car',
	dateType: 'day',
	workDate: ''
};

const {
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	handleSorter,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Schedule>(module, moduleParams);
resetParams();

const columns = ref<DataTableColumn<any>[]>([
	{
		title: '番号',
		key: 'targetNo',
		align: 'center',
		sorter: true,
		width: 150
	}
]);

const schedule = ref<any[]>([]);
const scrollX = ref<number>(2000);

const restColumns = () => {
	columns.value = [
		{
			title: '番号',
			key: 'targetNo',
			align: 'center',
			sorter: true,
			width: 150
		}
	];
};

const datePicker = ref({
	type: 'date',
	date: new Date().valueOf()
});
const initColumns = () => {
	restColumns();
	switch (searchParams.dateType) {
		case 'day':
			datePicker.value.type = 'date';
			searchParams.workDate = `${moment(datePicker.value.date).format('yyyy/MM/DD')}`;
			for (let index = 0; index < 24; index += 3) {
				const col = {
					title: `${index}~${index + 2}時`,
					key: `${index}h`,
					align: 'left',
					width: 100,
					render: (rowData, rowIndex) => {
						const details = h('div', { innerHTML: rowData[`${index}h`] }, {});
						return details;
					}
				} as DataTableColumn;
				columns.value.push(col);
			}
			scrollX.value = 2000;
			break;
		case 'month':
			datePicker.value.type = 'month';
			searchParams.workDate = `${moment(datePicker.value.date).format('yyyy-MM')}`;
			for (let index = 1; index < 32; index += 1) {
				if (moment().date(index).month() !== moment().month()) {
					return;
				}
				const col = {
					title: `${index}日`,
					key: `${index}md`,
					align: 'left',
					width: 100,
					render: (rowData, rowIndex) => {
						const details = h('div', { innerHTML: rowData[`${index}md`] }, {});
						return details;
					}
				} as DataTableColumn;
				columns.value.push(col);
			}
			scrollX.value = 4000;
			break;
		case 'year':
			datePicker.value.type = 'year';
			searchParams.workDate = `${moment(datePicker.value.date).format('yyyy')}`;
			for (let index = 1; index < 13; index += 1) {
				const col = {
					title: `${index}月`,
					key: `${index}m`,
					align: 'left',
					width: 100,
					render: (rowData, rowIndex) => {
						const details = h('div', { innerHTML: rowData[`${index - 1}m`] }, {});
						return details;
					}
				} as DataTableColumn;
				columns.value.push(col);
			}
			scrollX.value = 3000;
			break;
		default:
			break;
	}
};

function beforeFun() {
	initColumns();
}

function afterFun() {
	if (dataSource.value.length < 0) return;
	schedule.value = [];
	let curTarget = { targetId: '', targetNo: '', targetName: '' };
	dataSource.value.forEach(data => {
		curTarget = {
			targetId: data.targetId,
			targetNo: data.targetNo,
			targetName: data.targetName
		};
		dataSource.value.forEach(d => {
			if (d.targetId !== curTarget.targetId) {
				return;
			}
			if (searchParams.dateType === 'day') {
				for (let index = 0; index < 24;) {
					const startTime = moment(d.workDate).hours(index).minutes(0).seconds(0);
					const endTime = moment(d.workDate)
						.hours(index + 2)
						.minutes(59)
						.seconds(59);
					if (data.actionType !== 0) {
						const modo = curTarget[`${index}h`];
						curTarget[`${index}h`] =
							modo !== undefined
								? `${modo}<br/><span class=text-red>${d.remark}<span/>`
								: `<span class=text-red>${d.remark}<span/>`;
					} else if (moment(d.workTime).isBetween(startTime, endTime, undefined, '[]')) {
						const time = `${moment(d.workTime).format('HH:mm')} `;
						const modo = curTarget[`${index}h`];
						curTarget[`${index}h`] = modo !== undefined ? `${modo}<br/>${time} ${d.remark}` : `${time} ${d.remark}`;
					}
					index += 3;
				}
			} else if (searchParams.dateType === 'month') {
				for (let index = 1; index < 32;) {
					if (moment(d.workDate).date(index).month() !== moment().month()) {
						return;
					}
					const startTime = moment(d.workDate).date(index).hours(0).minutes(0).seconds(0);
					const endTime = moment(d.workDate).date(index).hours(23).minutes(59).seconds(59);
					if (moment(d.workTime).isBetween(startTime, endTime, undefined, '[]')) {
						if (data.actionType !== 0) {
							const modo = curTarget[`${index}md`];
							curTarget[`${index}md`] =
								modo !== undefined
									? `${modo}<br/><span class=text-red>${d.remark}<span/>`
									: `<span class=text-red>${d.remark}<span/>`;
						} else {
							const time = `${moment(d.workTime).format('HH:mm')} `;
							const modo = curTarget[`${index}md`];
							curTarget[`${index}md`] = modo !== undefined ? `${modo}<br/>${time} ${d.remark}` : `${time} ${d.remark}`;
						}
					}
					index += 1;
				}
			} else if (searchParams.dateType === 'year') {
				for (let index = 0; index < 12;) {
					const startTime = moment(d.workDate).month(index).date(1).hours(0).minutes(0).seconds(0);
					const endTime = moment(d.workDate)
						.month(index + 1)
						.date(1)
						.hours(0)
						.minutes(0)
						.seconds(0);
					if (moment(d.workTime).isBetween(startTime, endTime, undefined, '[)')) {
						const time = `${moment(d.workTime).format('MM-DD')} `;
						if (data.actionType !== 0) {
							const modo = curTarget[`${index}m`];
							curTarget[`${index}m`] =
								modo !== undefined
									? `${modo}<br/><span class=text-red>${time} ${d.remark}<span/>`
									: `<span class=text-red>${time} ${d.remark}<span/>`;
						} else {
							const modo = curTarget[`${index}m`];
							curTarget[`${index}m`] = modo !== undefined ? `${modo}<br/>${time} ${d.remark}` : `${time} ${d.remark}`;
						}
					}
					index += 1;
				}
			}
		});
		const arr = schedule.value.find(item => item.targetId === curTarget.targetId);
		if (arr === undefined || arr === null || arr.length === 0) {
			schedule.value.push(curTarget);
		}
		console.log(schedule.value);
	});
}

const onReset = () => {
	// datePicker.value.date = new Date().valueOf();
	datePicker.value.date = null;
	searchReset();
};

const onUpdate = (value: string | number | boolean) => {
	initColumns();
	searchQuery(beforeFun, afterFun);
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 200;

	initColumns();
	searchQuery(beforeFun, afterFun);
});
</script>
<style scoped></style>
