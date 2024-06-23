<template>
  <div>
    <n-drawer v-model:show="showModalRef" :style="bodyStyleRef" placement="right" :mask-closable="true">
      <n-card class="h-full shadow-sm rounded-16px">
        <n-space :vertical="true">
          <n-checkbox-group v-model:value="select" @update:value="onUpdate">
            <n-grid :y-gap="2" :cols="1">
              <n-gi v-for="col in columns" :key="col.key">
                <n-checkbox :value="col.key" :label="col.title" :checked="true" :disabled="disabled(col.key)" />
              </n-gi>
            </n-grid>
          </n-checkbox-group>
        </n-space>
      </n-card>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emits = defineEmits(['click']);
const columns = ref<any>();
const filter = ref<any>();
const select = ref<any>();
const bodyStyleRef = ref({
  width: '300px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = (cols: any, currentcols: any, fcols: any) => {
  columns.value = cols;
  filter.value = fcols;
  select.value = currentcols.map(i => i.key);
  showModalRef.value = true;
};
const disabled = (key: string) => {
  const f = filter.value.filter(i => i === key);
  return f.length > 0;
};
const titleRef = ref<string | undefined>('名目筛选');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const close = () => {
  showModalRef.value = false;
};
const onUpdate = (value: string | number, meta: { actionType: 'check' | 'uncheck'; value: string | number }) => {
  emits(
    'click',
    columns.value.filter(i => value.includes(i.key))
  );
};

defineExpose({
  showModal,
  setTitle,
  close
});
</script>

<style scoped></style>
