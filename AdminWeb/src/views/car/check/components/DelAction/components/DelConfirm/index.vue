<template>
  <div>
    <n-spin :show="loading">
      <n-result status="info" size="small" description="以下の情報削除を行います。">
        <template #footer>
          <n-descriptions size="small" label-placement="left" bordered :column="1" :label-style="{ width: '20%' }">
            <n-descriptions-item label="利用者">
              <n-space class="flex items-center">
                <div class="flex items-center">
                  <Icon icon="material-symbols:person" /><span class="ml-4px">{{ props.model?.staffName }}</span>
                </div>
                <div class="flex items-center">
                  <Icon icon="material-symbols:mail" /><span class="ml-4px">{{ props.model?.staffEmail }}</span>
                </div>
                <div class="flex items-center">
                  <Icon icon="material-symbols:phone-enabled" /><span class="ml-4px">{{
                    props.model?.staffPhone ?? '-'
                  }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
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
import { Icon } from '@iconify/vue';
import { request } from '@/service/request';

const emits = defineEmits(['next', 'prev']);

const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Car | null>,
    default: () => {
      return null;
    }
  }
});

const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const urls = {
  delete: 'user/delete'
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
