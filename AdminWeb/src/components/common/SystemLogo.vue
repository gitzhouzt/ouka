<template>
  <icon-custom-logo-fill v-if="fill" />
  <icon-custom-logo v-else />
</template>

<script lang="ts" setup>
interface Props {
  /** logo is fill */
  fill?: boolean;
}

withDefaults(defineProps<Props>(), {
  fill: false
});
</script>
<style scoped></style>
