<template>
  <n-grid cols="s:1 m:2 l:3" responsive="screen" :x-gap="16" :y-gap="16">
    <n-grid-item v-for="item in cardData" :key="item.id">
      <gradient-bg class="h-100px" :start-color="item.colors[0]" :end-color="item.colors[1]">
        <h3 class="text-16px">{{ item.title }}</h3>
        <div class="flex justify-between pt-12px">
          <Icon :icon="item.icon" class="text-32px" />
          <count-to
            :prefix="item.unit"
            :start-value="1"
            :end-value="item.value"
            class="text-30px text-white dark:text-dark"
          />
        </div>
      </gradient-bg>
    </n-grid-item>
  </n-grid>
</template>

<script setup lang="ts">
import { Icon } from '@iconify/vue';
import { GradientBg } from './components';

interface CardData {
  id: string;
  title: string;
  value: number;
  unit: string;
  colors: [string, string];
  icon: string;
}

const cardData: CardData[] = [
  {
    id: 'visit',
    title: '車両数',
    value: 23,
    unit: '',
    colors: ['#ec4786', '#b955a4'],
    icon: 'material-symbols:directions-car'
  },
  {
    id: 'amount',
    title: 'ドライバー人数',
    value: 58,
    unit: '',
    colors: ['#865ec0', '#5144b4'],
    icon: 'material-symbols:person'
  },
  {
    id: 'download',
    title: '受付',
    value: 1200,
    unit: '',
    colors: ['#56cdf3', '#719de3'],
    icon: 'material-symbols:important-devices-rounded'
  },
  {
    id: 'trade',
    title: 'キャンセル',
    value: 2,
    unit: '',
    colors: ['#010101', '#919191'],
    icon: 'ant-design:trademark-circle-outlined'
  },
  {
    id: 'trade',
    title: '事故',
    value: 2,
    unit: '',
    colors: ['#FF5555', '#FF2222'],
    icon: 'material-symbols:taxi-alert'
  },
  {
    id: 'trade',
    title: '実施数',
    value: 1000,
    unit: '',
    colors: ['#fcbc25', '#f68057'],
    icon: 'ant-design:trademark-circle-outlined'
  }
];
</script>
<style scoped></style>
