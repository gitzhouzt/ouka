<template>
  <n-descriptions :size="size" label-placement="left" bordered :column="1" :label-style="labelStyle">
    <n-descriptions-item label="ドライバー変更">
      <n-space>
        <div class="flex items-center">
          <span>{{ `${props.model?.driverName ?? '未定'}  →  ${props.deployModel?.driverName ?? '未定'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="変更理由">
      <n-space v-if="props.model?.driverName === props.deployModel?.driverName">
        <div class="flex items-center">
          <span class="">未変更</span>
        </div>
      </n-space>
      <n-space v-else>
        <div class="flex items-center">
          <span class="">{{ `${props.deployModel?.driverRemark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="車両変更">
      <n-space>
        <div class="flex items-center">
          <span>{{ `${props.model?.carName ?? '未定'}  →  ${props.deployModel?.carName ?? '未定'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="変更理由">
      <n-space v-if="props.model?.carName === props.deployModel?.carName">
        <div class="flex items-center">
          <span class="">未変更</span>
        </div>
      </n-space>
      <n-space v-else>
        <div class="flex items-center">
          <span class="">{{ `${props.deployModel?.carRemark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="注文備考">
      <n-space>
        <div class="flex items-center">
          <span class="">{{ `${props.deployModel?.companyRemark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
  </n-descriptions>
</template>
<script setup lang="ts">
import { PropType, ref } from 'vue';

const size = ref<'small' | 'medium' | 'large'>('small');
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
    default: () => {
      return {};
    }
  },
  deployModel: {
    type: Object as PropType<MyModel.DeployDetails | null>,
    default: () => {
      return {};
    }
  },
  labelStyle: {
    type: Object,
    default: () => {
      return { width: '20%' };
    }
  }
});
</script>
<style></style>
