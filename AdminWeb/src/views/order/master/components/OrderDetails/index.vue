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
      <order-details :label-style="labelStyle" :model="modelRef" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1400px'
});
const labelStyle = ref({ width: '15%' });
const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};

const modelRef = ref<MyModel.Order | null>(null);
const showModalRef = ref<boolean>(false);
const showModal = (row: MyModel.Order | null) => {
  showModalRef.value = true;
  modelRef.value = row;
};

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
