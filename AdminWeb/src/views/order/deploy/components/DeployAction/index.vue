<template>
  <n-modal
    v-model:show="showModalRef"
    :style="bodyStyleRef"
    transform-origin="center"
    :mask-closable="false"
    preset="card"
    :title="titleRef"
    closable
  >
    <n-card>
      <deploy-action :model="modelRef" :current="currentRef" :is-modal="true" @close="close" />
    </n-card>
  </n-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const bodyStyleRef = ref({
  width: '1000px'
});
const emits = defineEmits(['close']);
const modelRef = ref<MyModel.Order | null>();
const showModalRef = ref<boolean | undefined>(false);
const currentRef = ref<number | undefined>(1);
const widthRef = ref(0);
const setCurrent = (current: number) => {
  currentRef.value = current;
};
const showModal = (model: MyModel.Order | null, current: number) => {
  showModalRef.value = true;
  modelRef.value = model;
  widthRef.value = document.body.offsetWidth - 60;
  setCurrent(current);
};
const close = () => {
  emits('close');
  showModalRef.value = false;
};
const titleRef = ref<string | undefined>('支配操作');
const setTitle = (title: string) => {
  titleRef.value = title;
};

defineExpose({
  showModal,
  setTitle,
  setCurrent,
  close
});
</script>

<style scoped></style>
