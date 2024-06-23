<template>
  <n-divider title-placement="center">Interface Display</n-divider>
  <n-space vertical size="large">
    <setting-menu label="Bread crumbs">
      <n-switch :value="theme.header.crumb.visible" @update:value="theme.setHeaderCrumbVisible" />
    </setting-menu>
    <setting-menu label="Breadcrumb icon">
      <n-switch :value="theme.header.crumb.showIcon" @update:value="theme.setHeaderCrumbIconVisible" />
    </setting-menu>
    <setting-menu label="Multiple tabs">
      <n-switch :value="theme.tab.visible" @update:value="theme.setTabVisible" />
    </setting-menu>
    <setting-menu label="Multi-tab style">
      <n-select
        class="w-120px"
        size="small"
        :value="theme.tab.mode"
        :options="theme.tab.modeList"
        @update:value="theme.setTabMode"
      />
    </setting-menu>
    <setting-menu label="Page animation">
      <n-switch :value="theme.page.animate" @update:value="theme.setPageIsAnimate" />
    </setting-menu>
    <setting-menu label="Page animation type">
      <n-select
        class="w-120px"
        size="small"
        :value="theme.page.animateMode"
        :options="theme.page.animateModeList"
        @update:value="theme.setPageAnimateMode"
      />
    </setting-menu>
  </n-space>
</template>

<script lang="ts" setup>
import { useThemeStore } from '@/store';
import SettingMenu from '../SettingMenu/index.vue';

const theme = useThemeStore();
</script>
<style scoped></style>
