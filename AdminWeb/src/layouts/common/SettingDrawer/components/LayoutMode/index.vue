<template>
  <n-divider title-placement="center">Layout Mode</n-divider>
  <n-space justify="space-between">
    <layout-checkbox
      v-for="item in theme.layout.modeList"
      :key="item.value"
      :mode="item.value"
      :label="item.label"
      :checked="item.value === theme.layout.mode"
      @click="theme.setLayoutMode(item.value)"
    />
  </n-space>
</template>

<script setup lang="ts">
import { useThemeStore } from '@/store';
import { LayoutCheckbox } from './components';

const theme = useThemeStore();
</script>
<style scoped></style>
