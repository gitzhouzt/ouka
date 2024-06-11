<template>
  <div>
    <n-spin :show="loading">
      <n-result status="info" size="small" description="以下の情報削除を行います。">
        <template #footer>
          <n-descriptions size="small" label-placement="left" bordered :column="1" :label-style="{ width: '20%' }">
            <n-descriptions-item label="車両番号">
              <n-space class="flex items-center">
                <span>{{ props.model?.carNo }}</span>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="車両名">
              <n-space class="flex items-center">
                <span>{{ props.model?.carName }}</span>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="車両タイプ">
              <n-space>
                <span>{{ props.model?.carType }}</span>
              </n-space></n-descriptions-item
            >
            <n-descriptions-item label="駐車場">
              <n-space>
                <span>{{ props.model?.carPark }}</span>
              </n-space></n-descriptions-item
            >
            <n-descriptions-item label="車両写真">
              <n-space>
                <span
                  ><n-image width="80" :src="`${envConfig.static}${props.model?.carPhoto}`"></n-image
                ></span> </n-space
            ></n-descriptions-item>
          </n-descriptions>
          <n-space justify="center" class="pt-20px">
            <n-button type="primary" @click="next">削除</n-button>
          </n-space>
        </template>
      </n-result>
    </n-spin>
  </div>
</template>
<script setup lang="ts">
import { PropType, ref } from 'vue';
import { useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['next', 'prev']);

const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Car | null>,
    default: () => {
      return null;
    }
  }
});
const envConfig = getEnvConfig(import.meta.env);
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const urls = {
  delete: 'car/delete'
};
const next = () => {
  const promise = request.delete<Boolean>(`${urls.delete}/${props.model?.id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        emits('next', { key: 'confirm' });
      }
    })
    .catch(err => {
      message.warning(err);
    })
    .finally(() => {
      loading.value = false;
      loadingBar.finish();
    });
};
</script>
<style></style>
