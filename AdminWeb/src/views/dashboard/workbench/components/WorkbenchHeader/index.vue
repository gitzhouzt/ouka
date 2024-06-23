<template>
  <n-card :bordered="false" class="rounded-16px shadow-sm">
    <div class="flex-y-center justify-between">
      <div class="flex-y-center">
        <icon-custom-avatar class="text-70px" />
        <div class="pl-12px">
          <h3 class="text-18px font-semibold">おはよう{{ auth.userInfo.userName }}, 今日も元気そうです！</h3>
          <p class="leading-30px text-[#999]">今日は曇りから晴れ、20°C〜25°C！</p>
        </div>
      </div>
      <n-space :size="24" :wrap="false">
        <n-statistic v-for="item in statisticData" :key="item.id" class="whitespace-nowrap" v-bind="item"></n-statistic>
      </n-space>
    </div>
  </n-card>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/store';

interface StatisticData {
  id: number;
  label: string;
  value: string;
}

const auth = useAuthStore();

const statisticData: StatisticData[] = [
  {
    id: 0,
    label: 'アイテム',
    value: '25'
  },
  {
    id: 1,
    label: '作業待ち',
    value: '4/16'
  },
  {
    id: 2,
    label: 'メッセージ',
    value: '12'
  }
];
</script>
<style scoped></style>
