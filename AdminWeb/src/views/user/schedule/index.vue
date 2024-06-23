<template>
  <div class="h-max">
    <n-space :vertical="true">
      <n-space>
        <n-form :inline="!isMobile && !isWrap" :label-width="100" label-placement="left">
          <n-form-item label="キーワード">
            <n-input
              v-model:value="searchParams.keyword"
              style="min-width: 30%"
              type="text"
              placeholder="番号/ドライバー名"
              clearable
            />
          </n-form-item>
          <n-form-item label="時間">
            <n-date-picker v-model:value="datePicker.date" format="yyyy/MM/DD" :type="datePicker.type" clearable />
          </n-form-item>
          <!-- <n-form-item>
            <n-radio-group v-model:value="searchParams.dateType" name="dateTypeRdo" @update:value="onUpdate">
              <n-radio-button key="month" value="month" default-checked label="月" />
              <n-radio-button key="week" value="week" label="本週" />
              <n-radio-button key="week_prev" value="week_prev" label="先週" />
              <n-radio-button key="week_next" value="week_next" label="来週" />
            </n-radio-group>
          </n-form-item> -->
          <n-form-item>
            <n-button
              type="primary"
              @click="
                () => {
                  searchQuery(beforeFun, afterFun);
                }
              "
              >検索</n-button
            >
            <n-button class="ml-2" @click="onReset">リセット</n-button>
          </n-form-item>
        </n-form>
      </n-space>
      <loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
        <n-data-table
          remote
          bordered
          :columns="columns"
          :data="schedule"
          :pagination="pagination"
          :scroll-x="scrollX"
          :single-line="false"
          :flex-height="true"
          :style="{ height: hightRef + 'px' }"
          @update:page="handlePageChange"
          @update:sorter="handleSorter"
        />
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

const { isMobile, isWrap } = useMyCommon();

const module = 'operate/schedule';
const moduleParams: MySearch.SkuSearchParams = {
  keyword: '',
  targetType: 'Driver',
  dateType: 'month',
  workDate: ''
};

const {
  searchQuery,
  searchReset,
  handlePageChange,
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
    width: 100
  },
  {
    title: '名前',
    key: 'targetName',
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
      title: '番号',
      key: 'targetNo',
      align: 'center',
      sorter: true,
      width: 100
    },
    {
      title: '名前',
      key: 'targetName',
      align: 'center',
      sorter: true,
      width: 100
    }
  ];
};

const datePicker = ref({
  type: 'month',
  date: new Date().valueOf()
});
const initColumns = () => {
  restColumns();
  let start = moment().date();
  switch (searchParams.dateType) {
    case 'month':
      for (let index = 1; index < 32; index += 1) {
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
      scrollX.value = 2000;
      break;
    case 'week':
      start = moment().date();
      for (let index = start; index < start + 8; index += 1) {
        const col = {
          title: `${index}日`,
          key: `${index}wn`,
          align: 'left',
          width: 100,
          render: (rowData, rowIndex) => {
            const details = h('div', { innerHTML: rowData[`${index}wn`] }, {});
            return details;
          }
        } as DataTableColumn;
        columns.value.push(col);
      }
      scrollX.value = 2000;
      break;
    case 'week_prev':
      start = moment().add(-8, 'days').date();
      for (let index = start; index < start + 8; index += 1) {
        const col = {
          title: `${index}日`,
          key: `${index}wn`,
          align: 'left',
          width: 100,
          render: (rowData, rowIndex) => {
            const details = h('div', { innerHTML: rowData[`${index}wn`] }, {});
            return details;
          }
        } as DataTableColumn;
        columns.value.push(col);
      }
      scrollX.value = 2000;
      break;
    case 'week_next':
      start = moment().add(7, 'days').date();
      for (let index = start; index < start + 8; index += 1) {
        const col = {
          title: `${index}日`,
          key: `${index}wn`,
          align: 'left',
          width: 100,
          render: (rowData, rowIndex) => {
            const details = h('div', { innerHTML: rowData[`${index}wn`] }, {});
            return details;
          }
        } as DataTableColumn;
        columns.value.push(col);
      }
      scrollX.value = 2000;
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
      if (searchParams.dateType === 'week') {
        for (let index = 0; index < 24; ) {
          const startTime = moment(d.workDate).hours(index).minutes(0).seconds(0);
          const endTime = moment(d.workDate)
            .hours(index + 2)
            .minutes(59)
            .seconds(59);
          if (moment(d.workTime).isBetween(startTime, endTime, undefined, '[]')) {
            const modo = curTarget[`${index}h`];
            curTarget[`${index}h`] =
              modo !== undefined
                ? `${modo}<br/>${moment(d.workTime).format('HH:mm')} ${d.remark}`
                : `${moment(d.workTime).format('HH:mm')} ${d.remark}`;
          }
          index += 3;
        }
      } else if (searchParams.dateType === 'month') {
        for (let index = 1; index < 32; ) {
          const startTime = moment(d.workDate).date(index).hours(0).minutes(0).seconds(0);
          const endTime = moment(d.workDate).date(index).hours(23).minutes(59).seconds(59);
          if (moment(d.workTime).isBetween(startTime, endTime, undefined, '[]')) {
            const modo = curTarget[`${index}md`];
            curTarget[`${index}md`] =
              modo !== undefined
                ? `${modo}<br/>${moment(d.workTime).format('HH:mm')} ${d.remark}`
                : `${moment(d.workTime).format('HH:mm')} ${d.remark}`;
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
