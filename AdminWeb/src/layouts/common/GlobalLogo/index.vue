<template>
  <router-link :to="routeHomePath" class="flex-center w-full nowrap-hidden">
    <!-- <system-logo class="text-32px text-primary" /> -->
    <h2 v-show="showTitle" class="text-24px font-bold text-primary transition duration-300 ease-in-out">
      {{ title }}
    </h2>
    <!-- pl-8px text-16px -->
  </router-link>
</template>

<script setup lang="ts">
import { routePath } from '@/router';
import { useAppInfo } from '@/composables';

interface Props {
  /** show Title */
  showTitle: boolean;
}

defineProps<Props>();

const { title } = useAppInfo();
const routeHomePath = routePath('root');
</script>
<style scoped></style>
