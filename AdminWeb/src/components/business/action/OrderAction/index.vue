<template>
  <div>
    <n-card size="small">
      <n-steps :current="currentRef">
        <n-step :title="EnumOrderStepModule['action-customer']" />
        <n-step :title="EnumOrderStepModule['action-order']" />
        <n-step :title="EnumOrderStepModule['action-confirmed']" />
        <n-step :title="EnumOrderStepModule['action-success']" />
      </n-steps>
    </n-card>
    <n-card size="small" class="mt-10px">
      <transition name="fade-slide" mode="out-in" appear>
        <div>
          <component :is="activeModule.component" :model="modelRef" @next="next" @prev="prev" @close="close" />
        </div>
      </transition>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import type { Component, PropType } from 'vue';
import { ref, computed } from 'vue';
import { EnumOrderStepModule } from '@/enum';
import { Customer, Order, Confirm, Success } from './components';

const emits = defineEmits(['close']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
    default: () => {
      return null;
    }
  },
  current: {
    type: Number,
    default: () => {
      return 1;
    }
  }
});

const modelRef = ref<MyModel.Order | null>(props.model);
const currentRef = ref<number | undefined>(props.current);

interface StepModule {
  key: number;
  label: EnumOrderStepModule;
  component: Component;
}

const modules: StepModule[] = [
  { key: 1, label: EnumOrderStepModule['action-customer'], component: Customer },
  { key: 2, label: EnumOrderStepModule['action-order'], component: Order },
  { key: 3, label: EnumOrderStepModule['action-confirmed'], component: Confirm },
  { key: 4, label: EnumOrderStepModule['action-success'], component: Success }
];

const activeModule = computed(() => {
  const active: StepModule = { ...modules[0] };
  const findItem = modules.find(item => item.key === currentRef.value);
  if (findItem) {
    Object.assign(active, findItem);
  }
  return active;
});

const close = () => {
  emits('close');
  modelRef.value = null;
};
const next = (data: any) => {
  switch (data.key) {
    case 'customer':
      currentRef.value = 2;
      modelRef.value = data.params;
      break;
    case 'order':
      currentRef.value = 3;
      modelRef.value = data.params;
      break;
    case 'confirm':
      currentRef.value = 4;
      break;
    case 'success':
      close();
      break;
    default:
      break;
  }
};
const prev = (data: any) => {
  switch (data.key) {
    case 'order':
      currentRef.value = 1;
      break;
    case 'confirm':
      currentRef.value = 2;
      break;
    default:
      break;
  }
};
</script>

<style scoped></style>
