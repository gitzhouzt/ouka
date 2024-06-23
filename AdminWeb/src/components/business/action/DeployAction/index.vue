<template>
  <div>
    <n-card size="small">
      <n-steps :current="currentRef">
        <n-step :title="EnumDeployStepModule['action-driver']" />
        <n-step :title="EnumDeployStepModule['action-car']" />
        <n-step :title="EnumDeployStepModule['action-remark']" />
        <n-step :title="EnumDeployStepModule['action-confirmed']" />
        <n-step :title="EnumDeployStepModule['action-success']" />
      </n-steps>
    </n-card>
    <n-card size="small" class="mt-10px">
      <transition name="fade-slide" mode="out-in" appear>
        <div>
          <component
            :is="activeModule.component"
            :model="modelRef"
            :deploy-model="deployModelRef"
            @next="next"
            @prev="prev"
            @close="close"
          />
        </div>
      </transition>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import type { Component, PropType } from 'vue';
import { ref, computed } from 'vue';
import { EnumDeployStepModule } from '@/enum';
import { DriverDeploy, CarDeploy, RemarkDeploy, Confirm, Success } from './components';

const emits = defineEmits(['close']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
    default: () => {
      return {};
    }
  },
  deployModel: {
    type: Object as PropType<MyModel.DeployDetails | null>,
    default: () => {
      return {};
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
const deployModelRef = ref<MyModel.DeployDetails | null>();
const currentRef = ref<number | undefined>(props.current);

interface StepModule {
  key: number;
  label: EnumDeployStepModule;
  component: Component;
}

const modules: StepModule[] = [
  { key: 1, label: EnumDeployStepModule['action-driver'], component: DriverDeploy },
  { key: 2, label: EnumDeployStepModule['action-car'], component: CarDeploy },
  { key: 3, label: EnumDeployStepModule['action-remark'], component: RemarkDeploy },
  { key: 4, label: EnumDeployStepModule['action-confirmed'], component: Confirm },
  { key: 5, label: EnumDeployStepModule['action-success'], component: Success }
];

const activeModule = computed(() => {
  const active: StepModule = { ...modules[0] };
  const findItem = modules.find(item => item.key === currentRef.value);
  if (findItem) {
    Object.assign(active, findItem);
  }
  return active;
});
const next = (data: any) => {
  modelRef.value = props.model;
  deployModelRef.value = data.params;
  switch (data.key) {
    case 'driver':
      currentRef.value = 2;
      break;
    case 'car':
      currentRef.value = 3;
      break;
    case 'remark':
      currentRef.value = 4;
      break;
    case 'confirm':
      currentRef.value = 5;
      break;
    case 'success':
      emits('close');
      modelRef.value = null;
      deployModelRef.value = null;
      break;
    default:
      break;
  }
};
const prev = (data: any) => {
  modelRef.value = props.model;
  deployModelRef.value = data.params;
  switch (data.key) {
    case 'car':
      currentRef.value = 1;
      break;
    case 'remark':
      currentRef.value = 2;
      break;
    case 'confirm':
      currentRef.value = 3;
      break;
    default:
      break;
  }
};
const close = () => {
  emits('close');
  currentRef.value = 1;
  modelRef.value = null;
};
</script>

<style scoped></style>
