<template>
  <div class="flex-center w-20px h-20px rounded-2px shadow cursor-pointer" :style="{ backgroundColor: color }">
    <icon-ic-outline-check v-if="checked" :class="[iconClass, isWhite ? 'text-gray-700' : 'text-white']" />
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';

interface Props {
  /** color */
  color: string;
  /** checked */
  checked: boolean;
  /** iconClass */
  iconClass?: string;
}

const props = withDefaults(defineProps<Props>(), {
  iconClass: 'text-14px'
});

const whiteColors = ['#ffffff', '#fff', 'rgb(255,255,255)'];
const isWhite = computed(() => whiteColors.includes(props.color));
</script>
<style scoped></style>
