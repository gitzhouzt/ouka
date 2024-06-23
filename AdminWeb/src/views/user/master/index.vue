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
              placeholder="番号/名前"
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
        <n-button type="primary" @click="handleAdd('スタッフ登録')">新規登録</n-button>
        <n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
      </n-space>
      <loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
        <n-data-table
          remote
          bordered
          :columns="columns"
          :data="dataSource"
          :pagination="pagination"
          :scroll-x="2500"
          :single-line="false"
          :flex-height="true"
          :style="{ height: hightRef + 'px' }"
          @update:page="handlePageChange"
          @update:sorter="handleSorter"
        />
      </loading-empty-wrapper>
    </n-space>
    <add-or-edit ref="aoeModal" @close="searchQuery" />
    <del-action ref="delModal" @close="searchQuery" />
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NTag } from 'naive-ui';
import { EnumUserRole, EnumUserStatus } from '@/enum';
import { useMyOptions, useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit, DelAction } from './components';

const module = 'user';
const moduleParams: MySearch.UserSearchParams = {
  keyword: '',
  userRole: ''
};

const {
  delModal,
  aoeModal,
  handleAdd,
  handleEdit,
  handleAudit,
  handleDelete,
  handleSorter,
  searchQuery,
  searchReset,
  handlePageChange,
  resetParams,
  handleExport,
  searchParams,
  pagination,
  dataSource,
  loading,
  empty
} = useDataTable<MyModel.User>(module, moduleParams);
resetParams();

const { userRoleOptions } = useMyOptions();
const { userRoleTagType, userStatusTagType } = useMyTags();
const { isMobile, isWrap } = useMyCommon();

const columns: DataTableColumn<MyModel.User>[] = [
  {
    title: '番号',
    key: 'userNo',
    align: 'center',
    sorter: true,
    width: 100
  },
  {
    title: '名前',
    key: 'userName',
    align: 'center',
    width: 200
  },
  {
    title: '電話',
    key: 'userPhone',
    align: 'center',
    width: 200,
    render(row) {
      const phone = `${row.userPhone ? `+${row.countryNum} ${row.userPhone}` : '-'}`;
      return phone;
    }
  },
  {
    title: 'メール',
    key: 'userEmail',
    width: 200,
    align: 'center',
    render(row) {
      return [h('div', {}, { default: () => row.userEmail ?? '-' })];
    }
  },
  {
    title: 'Wechat',
    key: 'userWechat',
    width: 100,
    align: 'center',
    render(row) {
      return [
        h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.userWechat ? row.userWechat : '-') })
      ];
    }
  },
  {
    title: 'WhatsApp',
    key: 'userWhatsApp',
    width: 100,
    align: 'center',
    render(row) {
      return [
        h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.userWhatsApp ? row.userWhatsApp : '-') })
      ];
    }
  },
  {
    title: 'Line',
    key: 'userLine',
    width: 100,
    align: 'center',
    render(row) {
      return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => (row.userLine ? row.userLine : '-') })];
    }
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
    title: 'ステータス',
    width: 100,
    key: 'status',
    align: 'center',
    sorter: true,
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
    title: 'アカウント',
    key: 'audit',
    width: 80,
    align: 'center',
    render(row) {
      const type = row.isAudit === true ? 'warning' : 'default';
      const audit = row.isAudit === true ? '停止' : '停止解除';
      const tipsOption = h(
        NTag,
        {
          type: row.isAudit ? 'success' : 'error',
          round: true
        },
        {
          default: () => (row.isAudit ? '有効' : '無効')
        }
      );
      const auditOption = h(
        NButton,
        {
          quaternary: true,
          size: 'small',
          type,
          onClick: () => handleAudit(row, row.userName)
        },
        { default: () => audit }
      );
      return [
        h(
          'div',
          {
            class: 'flex flex-row items-center justify-between'
          },
          {
            default: () => [tipsOption, auditOption]
          }
        )
      ];
    }
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
          onClick: () => handleEdit('スタッフ編集', row)
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
          onClick: () => handleDelete('スタッフ', row)
        },
        { default: () => '削除' }
      );
      return delOption;
    }
  }
];
const fileUploadRef = ref<any>();
const onFinish = (files: any) => {
  fileUploadRef.value.cleanFiles();
  searchQuery();
};
const hightRef = ref();

onMounted(() => {
  hightRef.value = document.documentElement.offsetHeight - 240;

  searchQuery();
});
</script>
<style scoped></style>
