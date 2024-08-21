<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="日期">
						<n-date-picker v-model:value="datePicker.date" type="date" clearable />
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
				<n-data-table remote bordered :columns="columns" :data="schedule" :scroll-x="scrollX" :row-props="rowProps"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
	</div>
</template>

<script setup lang="ts">
import { log } from 'console';
import { defineComponent, h, nextTick, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NSelect } from 'naive-ui';
import moment from 'moment';
import { RowData } from 'naive-ui/es/data-table/src/interface';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const { isMobile } = useMyCommon();

const module = 'operate/schedule/workByMonth';
const moduleParams: MySearch.SkuSearchParams = {
	keyword: '',
	targetType: 'All',
	dateType: 'month',
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
		title: '名前/番号',
		key: 'targetNo',
		align: 'center',
		sorter: true,
		width: 100
	}
]);

const schedule = ref<any[]>([]);
const scrollX = ref<number>(2000);

const restColumns = () => {
	columns.value = [
		{
			title: '名前/番号',
			key: 'targetNo',
			align: 'center',
			sorter: true,
			width: 100
		}
	];
};

const currentId = ref(-1);
const rowProps = (row: RowData, rowIndex: number) => {
	return {
		style: 'cursor: pointer;',
		onClick: () => {
			currentId.value = rowIndex;
		}
	};
};

const options = [
	{
		label: 'R',
		value: 'R'
	},
	{
		label: 'Didi',
		value: 'Didi'
	},
	{
		label: '接送机',
		value: '接送机'
	},
	{
		label: '有休',
		value: '有休'
	}
];
const ShowOrEdit = defineComponent({
	props: {
		value: [String, Number],
		onUpdateValue: [Function, Array]
	},
	setup(props) {
		const isEdit = ref(false);
		const inputRef = ref(null);
		const inputValue = ref(props.value);
		function handleOnClick() {
			isEdit.value = true;
			nextTick(() => {
				inputRef.value.focus();
			});
		}
		function handleChange() {
			props.onUpdateValue(inputValue.value);
			isEdit.value = false;
		}
		return () =>
			h(
				'div',
				{
					style: 'min-height: 22px',
					onClick: handleOnClick
				},
				isEdit.value
					? h(NSelect, {
						ref: inputRef,
						value: inputValue.value,
						onUpdateValue: v => {
							inputValue.value = v;
						},
						onBlur: handleChange,
						options
					})
					: { default: () => props.value }
			);
	}
});

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
			searchParams.workDate = `${moment(datePicker.value.date).format('yyyy/MM/DD')}`;
			for (let index = 1; index < 32; index += 1) {
				if (moment().date(index).month() !== moment().month()) {
					return;
				}
				const col = {
					title: `${index}日`,
					key: `${index}md`,
					align: 'left',
					width: 100,
					render(rowData, rowIndex) {
						return h(ShowOrEdit, {
							value: rowData[`${index}md`] as string,
							onUpdateValue(v: any) {
								rowData[`${index}md`] = v;
								console.log('test', dataSource.value[currentId.value].remark);
							}
						});
					}
				} as DataTableColumn;
				columns.value.push(col);
			}
			scrollX.value = 4000;
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
			}
		});
		const arr = schedule.value.find(item => item.targetId === curTarget.targetId);
		if (arr === undefined || arr === null || arr.length === 0) {
			schedule.value.push(curTarget);
		}
	});
}

const onReset = () => {
	datePicker.value.date = new Date().valueOf();
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
