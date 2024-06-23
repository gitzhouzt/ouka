<template>
  <n-divider title-placement="center">Theme Setting</n-divider>
  <textarea id="themeConfigCopyTarget" v-model="dataClipboardText" class="absolute opacity-0" />
  <n-space vertical>
    <div ref="copyRef" data-clipboard-target="#themeConfigCopyTarget">
      <n-button type="primary" :block="true">Copy Config</n-button>
    </div>
    <n-button type="warning" :block="true" @click="handleResetConfig">Reset Config</n-button>
  </n-space>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue';
import Clipboard from 'clipboard';
import { useThemeStore } from '@/store';

const theme = useThemeStore();

const copyRef = ref<HTMLElement>();

const dataClipboardText = ref(getClipboardText());

function getClipboardText() {
  return JSON.stringify(theme.$state);
}

function handleResetConfig() {
  theme.resetThemeStore();
  window.$message?.success('The configuration has been reset, please copy again！');
}

function clipboardEventListener() {
  if (!copyRef.value) return;
  const copy = new Clipboard(copyRef.value);
  copy.on('success', () => {
    window.$dialog?.success({
      title: '操作成功',
      content: 'コピーしました。src/settings/theme.jsonのコンテンツを置き換えてください。',
      positiveText: 'OK'
    });
  });
}

const stopHandle = watch(
  () => theme.$state,
  () => {
    dataClipboardText.value = getClipboardText();
  },
  { deep: true }
);

onMounted(() => {
  clipboardEventListener();
});
onUnmounted(() => {
  stopHandle();
});
</script>
<style scoped></style>
