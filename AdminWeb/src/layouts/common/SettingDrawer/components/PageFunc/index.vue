<template>
  <n-divider title-placement="center">Interface</n-divider>
  <n-space vertical size="large">
    <setting-menu label="Sidebar Invert Color">
      <n-switch :value="theme.sider.inverted" @update:value="theme.setSiderInverted" />
    </setting-menu>
    <setting-menu label="Head Inversion">
      <n-switch :value="theme.header.inverted" @update:value="theme.setHeaderInverted" />
    </setting-menu>
    <setting-menu label="Fixed header and Multi-tab">
      <n-switch :value="theme.fixedHeaderAndTab" @update:value="theme.setIsFixedHeaderAndTab" />
    </setting-menu>
    <setting-menu label="Top-menu position">
      <n-select
        class="w-120px"
        size="small"
        :value="theme.menu.horizontalPosition"
        :options="theme.menu.horizontalPositionList"
        @update:value="theme.setHorizontalMenuPosition"
      />
    </setting-menu>
    <setting-menu label="Head height">
      <n-input-number
        class="w-120px"
        size="small"
        :value="theme.header.height"
        :step="1"
        @update:value="theme.setHeaderHeight"
      />
    </setting-menu>
    <setting-menu label="Multi-tab height">
      <n-input-number
        class="w-120px"
        size="small"
        :value="theme.tab.height"
        :step="1"
        @update:value="theme.setTabHeight"
      />
    </setting-menu>
    <setting-menu label="Multi-tab cache">
      <n-switch :value="theme.tab.isCache" @update:value="theme.setTabIsCache" />
    </setting-menu>
    <setting-menu label="Sidebar width">
      <n-input-number
        class="w-120px"
        size="small"
        :value="theme.sider.width"
        :step="10"
        @update:value="theme.setSiderWidth"
      />
    </setting-menu>
    <setting-menu label="Left blend sidebar width">
      <n-input-number
        class="w-120px"
        size="small"
        :value="theme.sider.mixWidth"
        :step="5"
        @update:value="theme.setMixSiderWidth"
      />
    </setting-menu>
    <setting-menu label="Fixed bottom">
      <n-switch :value="theme.footer.fixed" @update:value="theme.setFooterIsFixed" />
    </setting-menu>
  </n-space>
</template>

<script lang="ts" setup>
import { useThemeStore } from '@/store';
import SettingMenu from '../SettingMenu/index.vue';

const theme = useThemeStore();
</script>
<style scoped></style>
