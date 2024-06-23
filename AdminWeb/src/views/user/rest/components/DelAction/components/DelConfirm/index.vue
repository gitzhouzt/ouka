<template>
  <div>
    <n-spin :show="loading">
      <n-result status="info" size="small" description="以下の情報削除を行います。">
        <template #footer>
          <n-descriptions size="small" label-placement="left" bordered :column="1" :label-style="{ width: '20%' }">
            <n-descriptions-item label="スタッフ番号">
              <n-space class="flex items-center">
                {{ props.model?.userNo }}
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="スタッフ名">
              <n-space class="flex items-center">
                {{ props.model?.userName }}
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="休暇タイプ">
              <n-space class="flex items-center">
                {{ props.model?.restType }}
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="休暇期間">
              <n-space class="flex items-center">
                {{ `${props.model?.startTime} ~ ${props.model?.endTime} ` }}
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="備考">
              <n-space class="flex items-center">
                {{ `${props.model?.remark ?? '-'}` }}
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
import { request } from '@/service/request';

const emits = defineEmits(['next', 'prev']);

const props = defineProps({
  model: {
    type: Object as PropType<MyModel.UserRest | null>,
    default: () => {
      return null;
    }
  }
});

const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const urls = {
  delete: 'user/restCancel'
};
const next = () => {
  const promise = request.post<Boolean>(`${urls.delete}/${props.model?.id}`);
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
