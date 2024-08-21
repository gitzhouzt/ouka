<template>
  <n-descriptions :size="size" label-placement="left" bordered :column="3" :label-style="labelStyle">
    <n-descriptions-item label="車両番号">
      <n-space>
        <span>{{ props.model?.carNo }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="事故原因">
      <n-space>
        {{ `${props.model?.accidentType}` }}
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="ステータス">
      <n-space>
        <n-tag :bordered="false" :type="statusTagType(props.model?.status ?? 'Filling')">{{
          EnumStatus[props.model?.status ?? 'Filling']
          }}</n-tag>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="事故者">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.driverName}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="事故日時">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.accidentTime}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="事故通知日時">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.noticeTime}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="事故詳細" :span="3">
      <n-space>
        <span>{{ props.model?.details }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="責任側">
      <n-space>
        <span> {{ props.model?.responsible }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="事故比例">
      <n-space>
        <span> {{ props.model?.proportion }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="確認者">
      <n-space>
        <span> {{ props.model?.confirmBy }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="修理業者">
      <n-space>
        <span> {{ props.model?.repairBy }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="修理終了日時">
      <n-space>
        <span> {{ props.model?.repairTime }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="総額">
      <n-space>
        <span> {{ props.model?.amount }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="会社負担">
      <n-space>
        <span> {{ props.model?.companyAmount }}</span>
      </n-space></n-descriptions-item>

    <n-descriptions-item label="保険負担">
      <n-space>
        <span> {{ props.model?.insuranceAmount }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="個人負担">
      <n-space>
        <span> {{ props.model?.driverAmount }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="経理通知日時">
      <n-space>
        <span> {{ props.model?.financeNoticeTime }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="決算日時">
      <n-space>
        <span> {{ props.model?.financeTime ?? '-' }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="決算者">
      <n-space>
        <span> {{ props.model?.financeByName ?? '-' }}</span>
      </n-space></n-descriptions-item>
    <n-descriptions-item label="備考" :span="3">
      <n-space>
        <div class="flex items-center">
          <span class="">{{ `${props.model?.remark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
  </n-descriptions>
</template>
<script setup lang="ts">
import { PropType, ref } from 'vue';
import { EnumStatus } from '@/enum';
import { useMyTags } from '@/composables';

const size = ref<'small' | 'medium' | 'large'>('small');
const { statusTagType } = useMyTags();
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Accident | null>,
    default: () => {
      return null;
    }
  },
  labelStyle: {
    type: Object,
    default: () => {
      return { width: '10%' };
    }
  }
});
</script>
<style></style>
