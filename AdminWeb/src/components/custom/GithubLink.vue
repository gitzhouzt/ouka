<template>
  <web-site-link label="github link：" :link="link" />
</template>

<script setup lang="ts">
import WebSiteLink from './WebSiteLink.vue';

interface Props {
  /** github link */
  link: string;
}

defineProps<Props>();
</script>
<style scoped></style>
