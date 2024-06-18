<template>
  <div>
    <n-card title="地域一覧" class="h-full shadow-sm rounded-16px">
      <n-space :vertical="true">
        <n-space>
          <n-form inline :label-width="100" label-placement="left">
            <n-form-item label="キーワード">
              <n-input
                v-model:value="searchParams.keyword"
                style="min-width: 30%"
                type="text"
                placeholder="コード/地域名"
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
        <n-space> </n-space>
        <loading-empty-wrapper class="h-620px" :loading="loading" :empty="empty">
          <n-data-table
            striped
            remote
            :columns="columns"
            :data="dataSource"
            :pagination="pagination"
            :flex-height="true"
            class="h-620px"
            @update:page="handlePageChange"
          />
        </loading-empty-wrapper>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { DataTableColumn, NButton } from 'naive-ui';
import { useDataTable } from '@/hooks';

const module = 'sys/area';
const moduleParams: MySearch.SearchParams = {
  keyword: ''
};

const {
  searchQuery,
  searchReset,
  handlePageChange,
  resetParams,
  searchParams,
  pagination,
  dataSource,
  loading,
  empty
} = useDataTable<MyModel.Area>(module, moduleParams);
resetParams();

const columns: DataTableColumn<MyModel.Area>[] = [
  {
    title: '地域Id',
    key: 'areaId',
    width: 80,
    align: 'center'
  },
  {
    title: '地域名',
    key: 'areaName',
    width: 200,
    align: 'center'
  },
  {
    title: '市区町村名',
    key: 'cityName',
    width: 100,
    align: 'center'
  }
];

onMounted(() => {
  searchQuery();
});
</script>
<style scoped></style>
