<template>
  <div
    class="p-4px border-1px border-[#efeff5] dark:border-[#ffffff17] rounded-4px hover:shadow-sm cursor-pointer"
    @click="handleOpenSite"
  >
    <header class="flex-y-center">
      <Icon :icon="icon" :style="{ color: iconColor }" class="text-30px" />
      <h3 class="pl-12px text-18px font-semibold">{{ name }}</h3>
    </header>
    <p class="py-8px h-56px text-[#999]">{{ description }}</p>
    <div class="flex justify-end">
      <span>{{ author }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Icon } from '@iconify/vue';

interface Props {
  /** name */
  name: string;
  /** description */
  description: string;
  /** author */
  author: string;
  /** site */
  site: string;
  /** icon */
  icon: string;
  /** iconColor */
  iconColor?: string;
}

const props = defineProps<Props>();

function handleOpenSite() {
  // window.open(props.site, '_blank');
}
</script>
<style scoped></style>
