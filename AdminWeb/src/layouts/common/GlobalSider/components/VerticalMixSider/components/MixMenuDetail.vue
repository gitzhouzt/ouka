<template>
  <div class="mb-6px px-4px cursor-pointer" @mouseenter="setTrue" @mouseleave="setFalse">
    <div
      class="flex-center flex-col py-12px rounded-2px bg-transparent transition-colors duration-300 ease-in-out"
      :class="{ 'text-primary !bg-primary_active': isActive, 'text-primary': isHover }"
    >
      <component :is="icon" :class="[isMini ? 'text-16px' : 'text-20px']" />
      <p
        class="text-12px overflow-hidden transition-height duration-300 ease-in-out"
        :class="[isMini ? 'h-0 pt-0' : 'h-24px pt-4px']"
      >
        {{ label }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { VNodeChild } from 'vue';
import { useBoolean } from '@/hooks';

interface Props {
  /** route Name */
  routeName: string;
  /** route Name label */
  label: string;
  /** active Route Name */
  activeRouteName: string;
  /** route icon */
  icon?: () => VNodeChild;
  /** mini Route */
  isMini?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  icon: undefined,
  isMini: false
});

const { bool: isHover, setTrue, setFalse } = useBoolean();

const isActive = computed(() => props.routeName === props.activeRouteName);
</script>
<style scoped></style>
