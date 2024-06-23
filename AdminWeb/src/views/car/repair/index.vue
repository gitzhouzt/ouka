<template>
  <div>
    <n-card title="車両修理" class="h-full shadow-sm rounded-16px">
      <n-space :vertical="true">
        <n-space>
          <n-form :inline="!isMobile" :label-width="100" label-placement="left">
            <n-form-item label="キーワード">
              <n-input
                v-model:value="searchParams.keyword"
                style="min-width: 30%"
                type="text"
                placeholder="番号/名"
                clearable
              />
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
          <n-button type="primary" @click="handleAdd('車両修理')">車両修理</n-button>
        </n-space>
        <loading-empty-wrapper class="h-620px" :loading="loading" :empty="empty">
          <n-data-table
            striped
            remote
            :columns="columns"
            :data="dataSource"
            :pagination="pagination"
            :flex-height="true"
            :scroll-x="1000"
            class="h-620px"
            @update:page="handlePageChange"
          />
        </loading-empty-wrapper>
      </n-space>
    </n-card>
    <add-or-edit ref="aoeModal" @close="searchQuery" />
    <del-action ref="delModal" @close="searchQuery" />
  </div>
</template>

<script setup lang="ts">
import { h, onMounted } from 'vue';
import { DataTableColumn, NButton, NTag } from 'naive-ui';
import { useMyCommon, useMyTags } from '@/composables';
import { useDataTable } from '@/hooks';
import { EnumStatus } from '@/enum/business';
import { AddOrEdit, DelAction } from './components';

const module = 'car/repair';
const moduleParams: MySearch.SearchParams = {
  keyword: ''
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
  resetParams,
  searchParams,
  pagination,
  dataSource,
  loading,
  empty
} = useDataTable<MyModel.Car>(module, moduleParams);
resetParams();
const { statusTagType } = useMyTags();
const { isMobile } = useMyCommon();

const columns: DataTableColumn<MyModel.CarRepair>[] = [
  {
    title: '車両番号',
    key: 'carNo',
    width: 100,
    align: 'left'
  },
  {
    title: '車両名',
    key: 'carName',
    width: 200,
    align: 'left',
    render(row) {
      return [
        h(
          'div',
          {
            class: 'flex flex-row items-center'
          },
          {
            default: () => [h('span', { class: 'ml-4px' }, { default: () => row.carName })]
          }
        )
      ];
    }
  },
  {
    title: 'タイプ',
    width: 80,
    key: 'repairType',
    align: 'center'
  },
  {
    title: '備考',
    width: 80,
    key: 'remark',
    align: 'center'
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
  },
  {
    title: '操作',
    key: 'actions',
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
      const options: any = [];
      options.push(editOption);
      options.push(delOption);
      return h(
        'div',
        {
          class: 'flex flex-col items-center'
        },
        {
          default: () => options
        }
      );
    }
  }
];

onMounted(() => {
  searchQuery();
});
</script>
<style scoped></style>
