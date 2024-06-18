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
              <n-input v-model:value="searchParams.keyword" type="text" placeholder="番号/名" clearable />
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
              :scroll-x="1000"
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
import { DataTableColumn, NButton, NEllipsis, NTag } from 'naive-ui';
import { EnumCarStatus } from '@/enum';
import { useMyTags } from '@/composables';
import { useDataTable } from '@/hooks';

const { carStatusTagType } = useMyTags();
const emits = defineEmits(['click']);
const module = 'car';
const moduleParams: MySearch.SearchParams = {
  keyword: '',
  isAudit: true
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
} = useDataTable<MyModel.Car>(module, moduleParams);
resetParams();
const bodyStyleRef = ref({
  width: '1200px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = () => {
  showModalRef.value = true;
  searchQuery();
};
const titleRef = ref<string | undefined>('車両');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const close = () => {
  showModalRef.value = false;
};

const handleSelect = (row: any) => {
  const result = {
    value: row.id,
    text: row.carName,
    carNo: row.carNo,
    carType: row.carType,
    carSeat: row.carSeat
  };
  emits('click', result);
  close();
};

const columns: DataTableColumn<MyModel.Car>[] = [
  {
    title: '車両番号',
    key: 'carNo',
    width: 150,
    align: 'center'
  },
  {
    title: '車両名',
    key: 'carName',
    align: 'center',
    width: 150,
    render(row) {
      return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.carName ?? '-' })];
    }
  },
  {
    title: 'タイプ',
    key: 'carType',
    align: 'center',
    width: 150
  },
  {
    title: 'ナンバー',
    key: 'plateNum',
    align: 'center',
    width: 200
  },
  {
    title: '座席数',
    key: 'carSeat',
    align: 'center',
    width: 80,
    render(row) {
      return [h('div', {}, { default: () => (row.carSeat ? `${row.carSeat}座` : '-') })];
    }
  },
  {
    title: '駐車場',
    key: 'carPark',
    align: 'center',
    width: 200
  },
  {
    title: 'ステータス',
    key: 'status',
    align: 'center',
    width: 100,
    render(row) {
      const { status } = row;
      const options: any = [];
      if (status) {
        const tagType = carStatusTagType(status as MyEnumType.EnumCarStatusKey);
        options.push(
          h(
            NTag,
            {
              type: tagType,
              round: true
            },
            { default: () => EnumCarStatus[status as MyEnumType.EnumCarStatusKey] }
          )
        );
      }
      return options;
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 80,
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
