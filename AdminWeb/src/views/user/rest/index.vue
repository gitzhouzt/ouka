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
              placeholder="スタッフ番号/名前/備考"
              clearable
            />
          </n-form-item>
          <n-form-item label="役職">
            <n-select
              v-model:value="searchParams.userRole"
              :options="userRoleOptions"
              :consistent-menu-width="true"
              style="min-width: 30%"
            />
          </n-form-item>
          <n-form-item label="休暇タイプ">
            <n-input
              v-model:value="searchParams.restType"
              placeholder="クリックタイプを選択"
              readonly
              @click="showDict('user_rest_type')"
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
        <n-button type="primary" @click="handleAdd('休暇登録')">新規登録</n-button>
        <file-upload
          ref="fileUploadRef"
          class="ml-2"
          accept=".csv,.xls,.xlsx"
          action="/api/user/batchRest/"
          list-type="text"
          file-key="files/user"
          btn-text="一括登録"
          :max="999"
          :show-file-list="false"
          @finish="onFinish"
        />
      </n-space>
      <loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
        <n-data-table
          remote
          bordered
          :columns="columns"
          :data="dataSource"
          :pagination="pagination"
          :scroll-x="1000"
          :single-line="false"
          :flex-height="true"
          :style="{ height: hightRef + 'px' }"
          @update:page="handlePageChange"
        />
      </loading-empty-wrapper>
    </n-space>
    <add-or-edit ref="aoeModal" @close="searchQuery" />
    <del-action ref="delModal" @close="searchQuery" />
    <dict-select-modal ref="dictModal" @click="selectDict" />
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NTag } from 'naive-ui';
import { EnumUserRole } from '@/enum';
import { useMyOptions, useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit, DelAction } from './components';

const module = 'user/rest';
const moduleParams: MySearch.UserSearchParams = {
  keyword: '',
  userRole: '',
  restType: ''
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
} = useDataTable<MyModel.UserRest>(module, moduleParams);
resetParams();

const { userRoleOptions } = useMyOptions();
const { userRoleTagType } = useMyTags();
const { isMobile, isWrap } = useMyCommon();

const fileUploadRef = ref<any>();
const onFinish = (files: any) => {
  fileUploadRef.value.cleanFiles();
  searchQuery();
};

const columns: DataTableColumn<MyModel.UserRest>[] = [
  {
    title: 'スタッフ番号',
    key: 'userNo',
    width: 100,
    align: 'center'
  },
  {
    title: 'スタッフ名',
    key: 'userName',
    width: 100,
    align: 'center'
  },
  {
    title: '役職',
    key: 'userRoles',
    width: 200,
    align: 'center',
    render(row) {
      const { userRoles } = row;
      const options: any = [];
      if (userRoles) {
        const roles = userRoles.split(',') as MyEnumType.EnumUserRoleKey[];
        roles.forEach(role => {
          const tagType = userRoleTagType(role);
          options.push(
            h(
              NTag,
              {
                type: tagType,
                round: true
              },
              { default: () => EnumUserRole[role] }
            )
          );
        });
      }
      return options;
    }
  },
  {
    title: '休暇タイプ',
    key: 'restType',
    width: 100,
    align: 'center'
  },
  {
    title: '開始日時',
    key: 'startTime',
    width: 120,
    align: 'center'
  },
  {
    title: '終了日時',
    key: 'endTime',
    width: 120,
    align: 'center'
  },
  {
    title: '編集',
    key: 'edit',
    width: 80,
    align: 'center',
    render(row) {
      const editOption = h(
        NButton,
        {
          quaternary: true,
          size: 'small',
          type: 'info',
          onClick: () => handleEdit('休暇編集', row)
        },
        { default: () => '編集' }
      );
      return editOption;
    }
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
          onClick: () => handleDelete('休暇', row)
        },
        { default: () => '削除' }
      );
      return delOption;
    }
  }
];
const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'user_rest_type':
      searchParams.restType = result.text;
      break;
    default:
      break;
  }
};
const hightRef = ref();

onMounted(() => {
  hightRef.value = document.documentElement.offsetHeight - 240;

  searchQuery();
});
</script>
<style scoped></style>
