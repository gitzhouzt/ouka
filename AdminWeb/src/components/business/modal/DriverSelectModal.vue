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
              <n-input
                v-model:value="searchParams.keyword"
                style="min-width: 30%"
                type="text"
                placeholder="番号/名前"
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
import { DataTableColumn, NButton, NTag } from 'naive-ui';
import { EnumUserStatus } from '@/enum';
import { useMyTags } from '@/composables';
import { useDataTable } from '@/hooks';

const { userStatusTagType } = useMyTags();
const emits = defineEmits(['click']);
const module = 'user';
const moduleParams: MySearch.UserSearchParams = {
  keyword: '',
  userRole: 'Driver'
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
} = useDataTable<MyModel.User>(module, moduleParams);
resetParams();
const bodyStyleRef = ref({
  width: '1200px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = () => {
  showModalRef.value = true;
  searchQuery();
};
const titleRef = ref<string | undefined>('ドライバー');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const close = () => {
  showModalRef.value = false;
};

const handleSelect = (row: any) => {
  const result = {
    id: row.id,
    userName: row.userName,
    userNo: row.userNo,
    userAvatar: row.userAvatar
  };
  emits('click', result);
  close();
};

const columns: DataTableColumn<MyModel.User>[] = [
  {
    title: '番号',
    key: 'userNo',
    align: 'center',
    width: 100
  },
  {
    title: '名前',
    key: 'userName',
    align: 'center',
    width: 100
  },
  {
    title: 'ステータス',
    width: 100,
    key: 'status',
    align: 'center',
    render(row) {
      const userStatus = row.status as MyEnumType.EnumUserStatusKey;
      const status = EnumUserStatus[userStatus];
      const tagType = userStatusTagType(userStatus);
      return h(
        NTag,
        {
          type: tagType,
          round: true
        },
        {
          default: () => status
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
