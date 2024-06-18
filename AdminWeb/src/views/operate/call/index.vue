<template>
  <div class="h-max">
    <n-space :vertical="true">
      <n-space>
        <n-form :inline="!isMobile" :label-width="100" label-placement="left">
          <n-form-item label="キーワード">
            <n-input
              v-model:value="searchParams.keyword"
              style="min-width: 30%"
              type="text"
              placeholder="車両/ドライバー/管理者"
              clearable
            />
          </n-form-item>
          <n-form-item label="点呼時間" path="selTime">
            <n-date-picker v-model:value="searchParams.selTime" type="daterange" clearable @update:value="onUpdate" />
          </n-form-item>
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
      <n-space>
        <n-button type="primary" @click="handleAmCall('点呼登録', undefined)">点呼登録</n-button>
      </n-space>
      <loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
        <n-data-table
          remote
          bordered
          :columns="columns"
          :data="dataSource"
          :pagination="pagination"
          :row-class-name="rowClassName"
          :scroll-x="2000"
          :single-line="false"
          :flex-height="true"
          :style="{ height: hightRef + 'px' }"
          @update:page="handlePageChange"
        />
      </loading-empty-wrapper>
    </n-space>
    <am-call-action ref="amCallModal" @close="searchQuery" />
    <pm-call-action ref="pmCallModal" @close="searchQuery" />
    <del-action ref="delModal" @close="searchQuery" />
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NTag } from 'naive-ui';
import { useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AmCallAction, PmCallAction, DelAction } from './components';

const module = 'operate/call';
const moduleParams: MySearch.SearchParams = {
  keyword: '',
  selTime: [Date.now(), Date.now()],
  beginTime: Date.now(),
  endTime: Date.now()
};

const {
  delModal,
  handleDelete,
  searchQuery,
  searchReset,
  handlePageChange,
  resetParams,
  searchParams,
  dataSource,
  pagination,
  loading,
  empty
} = useDataTable<MyModel.Order>(module, moduleParams);
resetParams();

const { isMobile } = useMyCommon();

const amCallModal = ref<any>(null);
const handleAmCall = (title: string, row: MyModel.Call | undefined) => {
  amCallModal.value.setTitle(title);
  amCallModal.value.showModal(row);
};
const pmCallModal = ref<any>(null);
const handlePmCall = (title: string, row: MyModel.Call | undefined) => {
  pmCallModal.value.setTitle(title);
  pmCallModal.value.showModal(row);
};

const rowClassName = (row: MyModel.Call) => {
  if (
    row.am1 === '無' ||
    row.am2 === '有' ||
    row.am3 === '有' ||
    row.am4 === '有' ||
    row.pm1 === '無' ||
    row.pm2 === '有' ||
    row.pm3 === '有' ||
    row.pm4 === '有'
  ) {
    return 'call';
  }
  return '';
};
const columns: DataTableColumn<MyModel.Call>[] = [
  {
    title: '日時',
    key: 'date',
    width: 100,
    align: 'center'
  },
  // {
  //   title: '天候',
  //   key: 'weather',
  //   width: 100,
  //   align: 'center',
  //   render(row) {
  //     return [h('div', {}, { default: () => row.weather ?? '-' })];
  //   }
  // },
  {
    title: 'ドライバー',
    key: 'driverName',
    width: 100,
    align: 'center'
  },
  {
    title: '車両',
    key: 'carNo',
    width: 100,
    align: 'center'
  },
  {
    title: '点呼方法(前)',
    key: 'amCallMethod',
    width: 100,
    align: 'center'
  },
  {
    title: '点呼日時(前)',
    key: 'amCallTime',
    width: 120,
    align: 'center'
  },
  {
    title: '点呼状態(前)',
    key: 'amCall',
    width: 100,
    align: 'center',
    render(row) {
      let flag = 0;
      if (row.am1) {
        if (row.am1 === '有' && row.am2 === '無' && row.am3 === '無' && row.am4 === '無') {
          flag = 1;
        } else {
          flag = 2;
        }
      }
      const tipsOption = h(
        NTag,
        {
          type: flag === 1 ? 'success' : flag === 0 ? 'warning' : 'error',
          round: true
        },
        {
          default: () => (flag === 1 ? '正常' : flag === 0 ? '未完成' : '異常')
        }
      );
      const atWorkCallOption = h(
        NButton,
        {
          quaternary: true,
          size: 'small',
          type: 'info',
          onClick: () => handleAmCall('乗務前点呼', row)
        },
        { default: () => '詳細' }
      );
      return [
        h(
          'div',
          {
            class: 'flex flex-row items-center justify-between'
          },
          {
            default: () => [tipsOption, atWorkCallOption]
          }
        )
      ];
    }
  },
  {
    title: '点呼方法(後)',
    key: 'pmCallMethod',
    width: 100,
    align: 'center',
    render(row) {
      return [h('div', {}, { default: () => row.pmCallMethod ?? '-' })];
    }
  },
  {
    title: '点呼日時(後)',
    key: 'pmCallTime',
    width: 120,
    align: 'center',
    render(row) {
      return [h('div', {}, { default: () => row.pmCallTime ?? '-' })];
    }
  },
  {
    title: '点呼状態(後)',
    key: 'pmCall',
    width: 100,
    align: 'center',
    render(row) {
      let flag = 0;
      if (row.pm1) {
        if (row.pm1 === '有' && row.pm2 === '無' && row.pm3 === '無' && row.pm4 === '無') {
          flag = 1;
        } else {
          flag = 2;
        }
      }
      const tipsOption = h(
        NTag,
        {
          type: flag === 1 ? 'success' : flag === 0 ? 'warning' : 'error',
          round: true
        },
        {
          default: () => (flag === 1 ? '正常' : flag === 0 ? '未完成' : '異常')
        }
      );
      const offWorkCallOption = h(
        NButton,
        {
          quaternary: true,
          size: 'small',
          type: 'info',
          onClick: () => handlePmCall('乗務後点呼', row)
        },
        { default: () => '詳細' }
      );
      return [
        h(
          'div',
          {
            class: 'flex flex-row items-center justify-between'
          },
          {
            default: () => [tipsOption, offWorkCallOption]
          }
        )
      ];
    }
  },
  // {
  //   title: '乗務前点呼',
  //   key: 'am',
  //   align: 'center',
  //   className: 'warning',
  //   width: 300,
  //   render(row) {
  //     const atWorkCallOption1 = h(
  //       NButton,
  //       {
  //         quaternary: true,
  //         size: 'small',
  //         type: 'info',
  //         onClick: () => handleAmCall('乗務前点呼', row)
  //       },
  //       { default: () => '乗務前点呼' }
  //     );
  //     const atWorkCallOption = [
  //       h(
  //         'div',
  //         { class: 'flex flex-row items-center' },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼方法：${row.amCallMethod}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼日時：${row.amCallTime}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `アルコール検知器：${row.am1}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `酒気帯び：${row.am2}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `異常：${row.am3}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `睡眠不足：${row.am4}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼担当者：${row.amConfirmBy}` })]
  //         }
  //       )
  //     ];
  //     return atWorkCallOption;
  //   }
  // },
  // {
  //   title: '乗務後点呼',
  //   key: 'pm',
  //   align: 'center',
  //   className: 'warning',
  //   width: 300,
  //   render(row) {
  //     const offWorkCallOption1 = h(
  //       NButton,
  //       {
  //         quaternary: true,
  //         size: 'small',
  //         type: 'info',
  //         onClick: () => handlePmCall('乗務後点呼', row)
  //       },
  //       { default: () => '乗務後点呼' }
  //     );
  //     const offWorkCallOption = [
  //       h(
  //         'div',
  //         { class: 'flex flex-row items-center' },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼方法：${row.pmCallMethod ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼日時：${row.pmCallTime ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `アルコール検知器：${row.pm1 ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `酒気帯び：${row.pm2 ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `異常：${row.pm3 ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `睡眠不足：${row.pm4 ?? ''}` })]
  //         }
  //       ),
  //       h(
  //         'div',
  //         {
  //           class: 'flex flex-row items-center'
  //         },
  //         {
  //           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点呼担当者：${row.pmConfirmBy ?? ''}` })]
  //         }
  //       )
  //     ];
  //     return offWorkCallOption;
  //   }
  // },
  {
    title: '運行管理者',
    key: 'manager',
    align: 'center',
    width: 100
  },
  {
    title: '運行補佐者',
    key: 'helper',
    align: 'center',
    width: 100
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
          onClick: () => handleDelete('運営点呼', row)
        },
        { default: () => '削除' }
      );
      return delOption;
    }
  }
];

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
  searchParams.beginTime = value ? value[0] : undefined;
  searchParams.endTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
  hightRef.value = document.documentElement.offsetHeight - 240;

  searchQuery();
});
</script>
<style scoped>
:deep(.call td) {
  background: rgba(250, 199, 199, 0.75) !important;
}
</style>
