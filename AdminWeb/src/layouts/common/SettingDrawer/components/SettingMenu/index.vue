<template>
  <div class="flex-y-center justify-between">
    <span>{{ label }}</span>
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
interface Props {
  /** label */
  label: string;
}

defineProps<Props>();
</script>
<style scoped></style>
