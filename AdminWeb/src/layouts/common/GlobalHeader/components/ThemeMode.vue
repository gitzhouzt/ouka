<template>
  <hover-container class="w-40px" :inverted="theme.header.inverted" tooltip-content="テーマモード">
    <dark-mode-switch :dark="theme.darkMode" class="wh-full" @update:dark="theme.setDarkMode" />
  </hover-container>
</template>

<script lang="ts" setup>
import { useThemeStore } from '@/store';

const theme = useThemeStore();
</script>
<style scoped></style>
