<template>
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
    <accident-action :model="modelRef" :current="currentRef" :is-modal="true" @close="close" />
  </n-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emits = defineEmits(['close']);
const modelRef = ref<MyModel.Accident | null>(null);
const showModalRef = ref<boolean | undefined>(false);
const currentRef = ref<number | undefined>(1);
const bodyStyleRef = ref({
  width: '1200px'
});
const widthRef = ref(0);
const setCurrent = (current: number) => {
  currentRef.value = current;
};
const showModal = (row: MyModel.Accident | null, current: number) => {
  showModalRef.value = true;
  modelRef.value = row;
  widthRef.value = document.body.offsetWidth - 60;
  setCurrent(current);
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};
const close = () => {
  emits('close');
  showModalRef.value = false;
};
const titleRef = ref<string | undefined>('事故情報');
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
