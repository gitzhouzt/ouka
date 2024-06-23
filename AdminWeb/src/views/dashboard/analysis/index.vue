<template>
  <n-space :vertical="true" :size="16">
    <top-chart v-if="false" />
    <data-card />
    <bottom-part />
    <ad />
  </n-space>
</template>

<script lang="ts" setup>
import { TopChart, DataCard, BottomPart, Ad } from './components';
</script>
<style scoped></style>
