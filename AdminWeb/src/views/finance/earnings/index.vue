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
              placeholder="番号/連絡先/ワード"
              clearable
            />
          </n-form-item>
          <n-form-item label="订单来源">
            <n-input
              v-model:value="searchParams.orderSource"
              placeholder="クリック分類を選択"
              readonly
              @click="showDict('order_source')"
            />
          </n-form-item>
          <n-form-item label="ドライバー" path="driverName">
            <n-input-group>
              <n-input
                v-model:value="searchParams.driverName"
                readonly
                placeholder="クリックドライバーを選択"
                @click="showDriver()"
              ></n-input>
            </n-input-group>
          </n-form-item>
          <n-form-item label="車両" path="carName">
            <n-input-group>
              <n-input
                v-model:value="searchParams.carName"
                readonly
                placeholder="クリック車両を選択"
                @click="showCar()"
              ></n-input>
            </n-input-group>
          </n-form-item>
          <!-- <n-form-item label="サービス時間" path="selTime">
            <n-date-picker v-model:value="searchParams.selTime" type="daterange" clearable @update:value="onUpdate" />
          </n-form-item> -->
          <n-form-item>
            <n-button
              type="primary"
              @click="
                () => {
                  searchQuery();
                }
              "
              >検索</n-button
            >
            <n-button class="ml-2" @click="searchReset">リセット</n-button>
          </n-form-item>
        </n-form>
      </n-space>
      <!-- <n-space>
        <n-button type="primary" @click="temp">一括決算</n-button>
        <n-button type="primary" @click="tempE">ダウンロード</n-button>
      </n-space> -->
      <loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
        <n-data-table
          :row-key="rowKey"
          remote
          bordered
          :v-model:checked-row-keys="checkedRowKeys"
          :columns="columns"
          :data="dataSource"
          :pagination="pagination"
          :scroll-x="4000"
          :single-line="false"
          :flex-height="true"
          :style="{ height: hightRef + 'px' }"
          @update:checked-row-keys="handleChecked"
          @update:page="handlePageChange"
        />
      </loading-empty-wrapper>
    </n-space>
    <dict-select-modal ref="dictModal" @click="selectDict" />
    <driver-select-modal ref="driverModal" @click="selectDriver" />
    <car-select-modal ref="carModal" @click="selectCar" />
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NNumberAnimation, NTag, useLoadingBar, useMessage } from 'naive-ui';
import moment from 'moment';
import { RowData } from 'naive-ui/es/data-table/src/interface';
import { EnumFinanceStatus, EnumOrderType } from '@/enum';
import { useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const module = 'finance/earnings';
const moduleParams: MySearch.OrderSearchParams = {
  keyword: '',
  orderSource: '',
  driverName: '',
  carName: '',
  selTime: [Date.now(), Date.now()],
  startBeginTime: Date.now(),
  startEndTime: Date.now(),
  orderStatus: ['Booked', 'Working', 'Completed']
};

const {
  searchReset,
  handlePageChange,
  resetParams,
  handleExport,
  searchQuery,
  searchParams,
  pagination,
  dataSource,
  loading,
  empty
} = useDataTable<MyModel.Earnings>(module, moduleParams);
resetParams();

const envConfig = getEnvConfig(import.meta.env);
const { financeStatusTagType } = useMyTags();
const { isMobile, isWrap } = useMyCommon();

const urls = {
  finance: `/order/finance`,
  defaultPlacard: `/order/defaultPlacard`
};
const rowKey = (row: RowData) => row.id;
const checkedRowKeys = ref<Array<string | number>>([]);
const message = useMessage();
const loadingBar = useLoadingBar();

const columns: DataTableColumn<MyModel.Earnings>[] = [
  {
    title: 'ツアー開始日',
    key: 'startTime',
    align: 'center',
    sorter: true,
    width: 120,
    render(row) {
      return `${moment(row.orderVO?.startTime).format('yyyy/MM/DD')}`;
    }
  },
  {
    title: 'ツアー終了日',
    key: 'startTime',
    align: 'center',
    sorter: true,
    width: 120,
    render(row) {
      return `${moment(row.orderVO?.endTime).format('yyyy/MM/DD')}`;
    }
  },
  {
    title: 'ツアー内容',
    key: 'orderType',
    align: 'center',
    sorter: true,
    width: 150,
    render(row) {
      const orderType = row.orderVO?.orderType as MyEnumType.EnumOrderTypeKey;
      const status = EnumOrderType[orderType];
      return h(
        'span',
        {},
        {
          default: () => status
        }
      );
    }
  },
  {
    title: '第三方',
    key: 'orderSource',
    align: 'center',
    sorter: true,
    width: 150,
    render(row) {
      return h(
        'span',
        {},
        {
          default: () => row.orderVO?.orderSource
        }
      );
    }
  },
  {
    title: '基本料金',
    key: 'orderPrice',
    align: 'center',
    width: 150,
    render(row) {
      return h(
        'span',
        {},
        {
          default: () => row.orderVO?.orderPrice
        }
      );
    }
  },
  {
    title: '全包or别',
    key: 'feeType',
    align: 'center',
    width: 150,
    render(row) {
      return h(
        'span',
        {},
        {
          default: () => row.orderVO?.feeType
        }
      );
    }
  },
  {
    title: 'ドライバー',
    key: 'driverName',
    align: 'center',
    sorter: true,
    width: 150,
    render(row) {
      return [
        h(
          'div',
          {
            class: `${row.orderVO?.driverName ?? 'text-red'} `
          },
          { default: () => row.orderVO?.driverName ?? '未定' }
        )
      ];
    }
  },
  {
    title: '車両',
    key: 'carNo',
    align: 'center',
    sorter: true,
    width: 150,
    render(row) {
      return [
        h(
          'div',
          {
            class: `${row.orderVO?.carNo ?? 'text-red'} `
          },
          { default: () => row.orderVO?.carNo ?? '未定' }
        )
      ];
    }
  },
  // {
  //   title: '币种',
  //   key: '币种',
  //   align: 'center',
  //   sorter: true,
  //   width: 80,
  //   render(row) {
  //     return h(
  //       'span',
  //       {},
  //       {
  //         default: () => row.orderVO?.c
  //       }
  //     );
  //   }
  // },
  {
    title: '手续费',
    key: 'handlingFee',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.handlingFee,
          to: row.handlingFee,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '油费',
    key: 'oilFee',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.oilFee,
          to: row.oilFee,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: 'ETC',
    key: 'etc',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.etc,
          to: row.etc,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '司机工资(委托)',
    key: 'entrustSalary',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.entrustSalary,
          to: row.entrustSalary,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '司机工资',
    key: 'salary',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.salary,
          to: row.salary,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '外派金额',
    key: 'entrust',
    align: 'center',
    width: 120,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.entrust,
          to: row.entrust,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '停车场',
    key: 'parking',
    align: 'center',
    width: 100,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.parking,
          to: row.parking,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  // {
  //   title: '垫付款',
  //   key: 'mealAmount',
  //   align: 'center',
  //   width: 120,
  //   render(row) {
  //     const numberOption = h(
  //       NNumberAnimation,
  //       {
  //         showSeparator: true,
  //         from: row.admissionAmount,
  //         to: row.admissionAmount,
  //         precision: 0,
  //         active: false
  //       },
  //       {}
  //     );
  //     return [h('span', {}, { default: () => '¥' }), numberOption];
  //   }
  // },
  // {
  //   title: '加班费',
  //   key: 'parkingAmount',
  //   align: 'center',
  //   width: 100,
  //   render(row) {
  //     const numberOption = h(
  //       NNumberAnimation,
  //       {
  //         showSeparator: true,
  //         from: row.parkingAmount,
  //         to: row.parkingAmount,
  //         precision: 0,
  //         active: false
  //       },
  //       {}
  //     );
  //     return [h('span', {}, { default: () => '¥' }), numberOption];
  //   }
  // },
  // {
  //   title: '外宿',
  //   key: 'mealAmount',
  //   align: 'center',
  //   width: 120,
  //   render(row) {
  //     const numberOption = h(
  //       NNumberAnimation,
  //       {
  //         showSeparator: true,
  //         from: row.parkingAmount,
  //         to: row.parkingAmount,
  //         precision: 0,
  //         active: false
  //       },
  //       {}
  //     );
  //     return [h('span', {}, { default: () => '¥' }), numberOption];
  //   }
  // },
  {
    title: '粗利润',
    key: 'profit',
    align: 'center',
    width: 100,
    render(row) {
      const numberOption = h(
        NNumberAnimation,
        {
          showSeparator: true,
          from: row.profit,
          to: row.profit,
          precision: 0,
          active: false
        },
        {}
      );
      return [h('span', {}, { default: () => '¥' }), numberOption];
    }
  },
  {
    title: '粗利润率',
    key: 'profitRate',
    align: 'center',
    width: 120,
    render(row) {
      return `${row.profitRate / 100}%`;
    }
  },
  {
    title: '責任人',
    key: 'sellerName',
    align: 'center',
    sorter: true,
    width: 150,
    render(row) {
      return `${row.orderVO?.sellerName}`;
    }
  },
  {
    title: '注文番号',
    key: 'orderNo',
    align: 'center',
    sorter: true,
    width: 150
  }
];

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'order_source':
      searchParams.orderSource = result.text;
      break;
    case 'order_city':
      searchParams.city = result.text;
      break;
    default:
      break;
  }
};
const driverModal = ref<any>(null);
const showDriver = () => {
  // const params = {
  //   orderDays: formValue.value.orderDays,
  //   orderStartTime: formValue.value.orderStartTime
  // };
  driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
  searchParams.driverId = result.id;
  searchParams.driverName = result.userName;
  searchParams.driverNo = result.userNo;
  searchParams.driverPhoto = result.userAvatar;
};

const carModal = ref<any>(null);
const showCar = () => {
  // const params = {
  //   orderDays: formValue.value.orderDays,
  //   orderStartTime: formValue.value.orderStartTime
  // };
  carModal.value?.showModal();
};
const selectCar = (result: any) => {
  searchParams.carId = result.value;
  searchParams.carName = result.text;
  searchParams.carNo = result.carNo;
  searchParams.carType = result.carType;
  searchParams.carSeat = result.carSeat;
};

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
  searchParams.startBeginTime = value ? value[0] : undefined;
  searchParams.startEndTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
  hightRef.value = document.documentElement.offsetHeight - 240;
  // initData();
  searchQuery();
});
</script>
<style scoped></style>
