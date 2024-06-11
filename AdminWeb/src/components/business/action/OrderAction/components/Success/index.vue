<template>
  <div>
    <n-spin :show="loading">
      <n-result status="success" size="small" description="保存しました">
        <template #footer>
          <n-space justify="center" class="pt-20px">
            <n-button type="primary" @click="next">OK</n-button>
          </n-space>
        </template>
      </n-result>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emits = defineEmits(['next', 'prev']);
const loading = ref<boolean>(false);
const next = () => {
  emits('next', { key: 'success' });
};
</script>

<style scoped></style>
