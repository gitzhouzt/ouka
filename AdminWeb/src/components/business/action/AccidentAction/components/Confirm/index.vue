<template>
  <div>
    <n-spin :show="loading">
      <n-result status="info" size="small" description="以下の内容で保存を行います。">
        <template #footer>
          <accident-details :model="props.model" />
          <n-space justify="center" class="pt-20px">
            <n-button type="default" @click="prev">前へ</n-button>
            <n-button type="primary" @click="next">保存</n-button>
          </n-space>
        </template>
      </n-result>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { onMounted, PropType, ref } from 'vue';

const emits = defineEmits(['next', 'prev']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Accident | null>,
    default: () => {
      return null;
    }
  }
});
const loading = ref<boolean>(false);
const next = () => {
  emits('next', { key: 'confirm' });
};

const prev = () => {
  emits('prev', { key: 'confirm' });
};

onMounted(() => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 200);
});
</script>

<style scoped></style>
