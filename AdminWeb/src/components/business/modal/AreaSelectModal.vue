<template>
  <div>
    <n-modal
      v-model:show="showModalRef"
      :style="bodyStyleRef"
      transform-origin="center"
      :mask-closable="false"
      preset="card"
      :title="titleRef"
      closable
    >
      <n-card class="h-full shadow-sm rounded-16px">
        <n-space :vertical="true">
          <n-form inline :label-width="100" label-placement="left">
            <n-form-item label="キーワード">
              <n-input v-model:value="searchParams.keyword" type="text" placeholder="コード/地域名" clearable />
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
          <loading-empty-wrapper class="h-620px" :loading="loading" :empty="empty" size="small">
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
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { DataTableColumn, NButton } from 'naive-ui';
import { useDataTable } from '@/hooks';

const emits = defineEmits(['click']);
const module = 'sys/area/business';
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
const bodyStyleRef = ref({
  width: '1000px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = () => {
  showModalRef.value = true;
  searchQuery();
};
const titleRef = ref<string | undefined>('地域一覧');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const close = () => {
  showModalRef.value = false;
};

const handleSelect = (row: any) => {
  emits('click', row);
  close();
};

const columns: DataTableColumn<MyModel.Area>[] = [
  {
    title: '地域コード',
    key: 'areaCode',
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
    title: '操作',
    key: 'actions',
    width: '120px',
    align: 'center',
    render(row) {
      return [
        h(
          NButton,
          {
            quaternary: true,
            size: 'small',
            type: 'info',
            onClick: () => handleSelect(row)
          },
          { default: () => '選択' }
        )
      ];
    }
  }
];

defineExpose({
  showModal,
  setTitle,
  close
});
</script>

<style scoped></style>
