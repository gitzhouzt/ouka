<template>
  <n-config-provider
    :theme="theme.naiveTheme"
    :theme-overrides="theme.naiveThemeOverrides"
    :locale="jaJP"
    :date-locale="dateJaJP"
    class="h-full"
  >
    <naive-provider>
      <router-view />
    </naive-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { jaJP, dateJaJP } from 'naive-ui';
import { useThemeStore, subscribeStore } from '@/store';

const theme = useThemeStore();

subscribeStore();
</script>
<style scoped></style>
