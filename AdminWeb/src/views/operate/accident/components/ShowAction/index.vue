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
      <n-card size="huge" role="dialog" aria-modal="true">
        <n-carousel show-arrow>
          <n-image v-for="f in files" :key="f" :src="`${envConfig.static}${f}`" />
        </n-carousel>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { nextTick, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const envConfig = getEnvConfig(import.meta.env);
const files = ref();

const showModalRef = ref<boolean | undefined>(false);
const showModal = (images: string) => {
  showModalRef.value = true;
  if (images) {
    files.value = images.split(',');
  } else {
    files.value = '';
  }
};

const close = () => {
  emits('close');
  showModalRef.value = false;
};
const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
