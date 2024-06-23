<template>
  <n-grid :item-responsive="true" :x-gap="16" :y-gap="16">
    <n-grid-item span="0:24 640:24 1024:16">
      <n-space :vertical="true" :size="16">
        <n-card title="データ情報" :bordered="false" size="small" class="shadow-sm rounded-16px">
          <template #header-extra>
            <a class="text-primary" href="javascript:;">データ情報</a>
          </template>
          <n-grid :item-responsive="true" responsive="screen" cols="m:2 l:3" :x-gap="8" :y-gap="8">
            <n-grid-item v-for="item in technology" :key="item.id">
              <technology-card v-bind="item" />
            </n-grid-item>
          </n-grid>
        </n-card>
        <n-card title="データ情報" :bordered="false" size="small" class="shadow-sm rounded-16px">
          <template #header-extra>
            <a class="text-primary" href="javascript:;">データ情報</a>
          </template>
          <n-list>
            <n-list-item v-for="item in activity" :key="item.id">
              <template #prefix>
                <icon-custom-avatar class="text-48px" />
              </template>
              <n-thing :title="item.content" :description="item.time" />
            </n-list-item>
          </n-list>
        </n-card>
      </n-space>
    </n-grid-item>
    <n-grid-item span="0:24 640:24 1024:8">
      <n-space :vertical="true" :size="16">
        <n-card title="データ情報" :bordered="false" size="small" class="shadow-sm rounded-16px">
          <n-grid :item-responsive="true" responsive="screen" cols="m:2 l:3" :x-gap="8" :y-gap="8">
            <n-grid-item v-for="item in shortcuts" :key="item.id">
              <shortcuts-card v-bind="item" />
            </n-grid-item>
          </n-grid>
        </n-card>
        <n-card title="データ情報" :bordered="false" size="small" class="shadow-sm rounded-16px">
          <icon-custom-banner class="text-400px text-primary" />
        </n-card>
      </n-space>
    </n-grid-item>
  </n-grid>
</template>

<script setup lang="ts">
import { TechnologyCard, ShortcutsCard } from './components';

interface Technology {
  id: number;
  name: string;
  description: string;
  author: string;
  site: string;
  icon: string;
  iconColor?: string;
}

interface Activity {
  id: number;
  content: string;
  time: string;
}

interface Shortcuts {
  id: number;
  label: string;
  icon: string;
  iconColor: string;
}

const technology: Technology[] = [
  {
    id: 0,
    name: 'Vue',
    description: 'テスト用',
    author: 'テスト用 - Evan You',
    site: 'https://v3.cn.vuejs.org/',
    icon: 'vscode-icons:file-type-vue'
  },
  {
    id: 1,
    name: 'TypeScript',
    description: 'テスト用',
    author: 'テスト用 - Microsoft',
    site: 'https://www.typescriptlang.org/',
    icon: 'vscode-icons:file-type-typescript-official'
  },
  {
    id: 2,
    name: 'Vite',
    description: 'テスト用',
    author: 'テスト用 - Evan You',
    site: 'https://vitejs.cn/',
    icon: 'vscode-icons:file-type-vite'
  },
  {
    id: 3,
    name: 'NaiveUI',
    description: 'テスト用',
    author: 'テスト用 - TuSimple',
    site: 'https://www.naiveui.com/zh-CN/os-theme',
    icon: 'mdi:alpha-n-box-outline',
    iconColor: '#5fbc22'
  },
  {
    id: 4,
    name: 'UnoCSS',
    description: 'テスト用',
    author: 'Anthony Fu',
    site: 'https://uno.antfu.me/?s=',
    icon: 'logos:unocss',
    iconColor: '#48b0f1'
  },
  {
    id: 5,
    name: 'Pinia',
    description: 'テスト用',
    author: 'Posva',
    site: 'https://pinia.esm.dev/',
    icon: 'mdi:fruit-pineapple',
    iconColor: '#fecf48'
  }
];

const activity: Activity[] = [
  { id: 4, content: 'テスト用', time: '2022-11-07 22:45:32' },
  { id: 3, content: 'テスト用', time: '2022-11-03 20:33:31' },
  { id: 2, content: 'テスト用', time: '2022-10-31 22:43:12' },
  { id: 1, content: 'テスト用', time: '2022-10-27 10:24:54' },
  { id: 0, content: 'テスト用', time: '2022-05-28 22:22:22' }
];

const shortcuts: Shortcuts[] = [
  { id: 0, label: 'テスト用', icon: 'mdi:desktop-mac-dashboard', iconColor: '#409eff' },
  { id: 1, label: 'テスト用', icon: 'ic:outline-settings', iconColor: '#7238d1' },
  { id: 2, label: 'テスト用', icon: 'mdi:family-tree', iconColor: '#f56c6c' },
  { id: 3, label: 'テスト用', icon: 'fluent:app-store-24-filled', iconColor: '#19a2f1' },
  { id: 4, label: 'テスト用', icon: 'mdi:table-large', iconColor: '#fab251' },
  { id: 5, label: 'テスト用', icon: 'mdi:chart-areaspline', iconColor: '#8aca6b' }
];
</script>
<style scoped></style>
