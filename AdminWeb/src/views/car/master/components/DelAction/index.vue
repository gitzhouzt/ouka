<template>
  <div>
    <n-modal
      v-model:show="showModalRef"
      :style="bodyStyleRef"
      transform-origin="center"
      :mask-closable="false"
      preset="card"
      :title="titleRef"
      closable
      @update-show="onUpdateShow"
    >
      <n-card :bordered="false" size="huge" role="dialog" aria-modal="true">
        <n-card>
          <n-steps :current="currentRef">
            <n-step title="削除確認" />
            <n-step title="削除済" />
          </n-steps>
        </n-card>
        <n-card class="mt-10px">
          <transition name="fade-slide" mode="out-in" appear>
            <div>
              <component :is="activeModule.component" :model="modelRef" @next="next" @prev="prev" />
            </div>
          </transition>
        </n-card>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import type { Component } from 'vue';
import { ref, computed } from 'vue';
import { EnumDeleteStepModule } from '@/enum';
import { DelSuccess, DelConfirm } from './components';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '800px'
});

const modelRef = ref<MyModel.Car | null>(null);
const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Car | null) => {
  showModalRef.value = true;
  modelRef.value = row;
};
const close = () => {
  emits('close');
  showModalRef.value = false;
};
const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const currentRef = ref<number | undefined>(1);
const setCurrent = (current: number) => {
  currentRef.value = current;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};
interface StepModule {
  key: number;
  label: EnumDeleteStepModule;
  component: Component;
}

const modules: StepModule[] = [
  { key: 1, label: EnumDeleteStepModule['del-confirmed'], component: DelConfirm },
  { key: 2, label: EnumDeleteStepModule['del-success'], component: DelSuccess }
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
  switch (data.key) {
    case 'confirm':
      currentRef.value = 2;
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
    case 'success':
      currentRef.value = 1;
      break;
    default:
      break;
  }
};
defineExpose({
  showModal,
  setTitle,
  setCurrent
});
</script>

<style scoped></style>
