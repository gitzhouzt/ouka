<template>
  <n-grid :x-gap="16" :y-gap="16" :item-responsive="true">
    <!-- <n-grid-item span="0:24 640:24 1024:8">
      <n-card title="タイムスケジュール" :bordered="false" class="rounded-16px shadow-sm">
        <div class="h-360px">
          <n-timeline>
            <n-timeline-item v-for="item in timelines" :key="item.type" v-bind="item" />
          </n-timeline>
        </div>
      </n-card>
    </n-grid-item> -->
    <n-grid-item span="0:24 640:24 1024:24">
      <n-card title="履歴" :bordered="false" class="rounded-16px shadow-sm">
        <div class="h-360px">
          <n-data-table size="small" :columns="columns" :data="tableData" />
        </div>
      </n-card>
    </n-grid-item>
  </n-grid>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NTag } from 'naive-ui';

interface TimelineData {
  type: 'default' | 'info' | 'success' | 'warning' | 'error';
  title: string;
  content: string;
  time: string;
}

interface TableData {
  key: number;
  name: string;
  content: string;
  date: string;
  tags?: string[];
  device: string;
}

const timelines: TimelineData[] = [
  { type: 'default', title: 'データ', content: '', time: '2022-10-10 20:46' },
  { type: 'success', title: 'データ', content: 'データ', time: '2022-10-10 20:46' },
  { type: 'error', title: 'データ', content: 'データ', time: '2022-10-10 20:46' },
  { type: 'warning', title: 'データ', content: 'データ', time: '2022-10-10 20:46' },
  { type: 'info', title: 'データ', content: 'データ', time: '2022-10-10 20:46' }
];

const columns = [
  {
    title: '名前',
    key: 'name'
  },
  {
    title: '操作内容',
    key: 'content'
  },
  {
    title: '操作端末',
    key: 'device'
  },
  {
    title: '時間',
    key: 'date'
  }
  // {
  //   title: 'Tags',
  //   key: 'tags',
  //   render(row: TableData) {
  //     const tags = row.tags.map(tagKey => {
  //       return h(
  //         NTag,
  //         {
  //           style: {
  //             marginRight: '6px'
  //           },
  //           type: 'info'
  //         },
  //         {
  //           default: () => tagKey
  //         }
  //       );
  //     });
  //     return tags;
  //   }
  // }
];

const tableData: TableData[] = [
  {
    key: 0,
    name: '鈴木',
    content: 'ログインしました。',
    date: '2023-11-02 07:33:45',
    device: 'ドライバーアプリ'
  },
  {
    key: 1,
    name: '山田',
    content: 'ログインしました。',
    date: '2023-11-01 17:33:45',
    device: 'ドライバーアプリ'
  },
  {
    key: 2,
    name: '李',
    content: 'ログインしました。',
    date: '2023-11-01 07:33:45',
    device: 'ドライバーアプリ'
  },
  {
    key: 3,
    name: '姜',
    content: 'ログインしました。',
    date: '2023-11-01 07:33:45',
    device: '管理システム'
  },
  {
    key: 4,
    name: '王',
    content: 'ログインしました。',
    date: '2023-10-31 19:45:12',
    device: '管理システム'
  },
  {
    key: 5,
    name: '山田',
    content: 'ログインしました。',
    date: '2023-10-31 18:55:22',
    device: 'ドライバーアプリ'
  },
  {
    key: 6,
    name: '姜',
    content: 'ログインしました。',
    date: '2023-10-31 17:27:22',
    device: 'ドライバーアプリ'
  }
];
</script>
<style scoped></style>
