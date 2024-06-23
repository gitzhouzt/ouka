<template>
  <n-descriptions :size="size" label-placement="left" bordered :column="3" :label-style="labelStyle">
    <n-descriptions-item label="注文番号">
      <n-space>
        <span>{{ props.model?.orderNo }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="タイプ">
      <n-space>
        {{ `${EnumOrderType[props.model?.orderType ?? 'Single']}` }}
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="ステータス">
      <n-space>
        <n-tag :bordered="false" :type="orderStatusTagType(props.model?.orderStatus ?? 'Assigning')">{{
          EnumOrderStatus[props.model?.orderStatus ?? 'Assigning']
        }}</n-tag>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="订单来源" :span="1">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.orderSource ?? 'SINGLE'}(${props.model?.orderSourceCode})` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="第三方" :span="2">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.orderKey ?? '-'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="ドライバー">
      <n-space
        ><span :class="`${props.model?.driverName ?? 'text-red'}`">{{ props.model?.driverName ?? '未定' }}</span>
      </n-space></n-descriptions-item
    >
    <n-descriptions-item label="車両番号">
      <n-space>
        <span :class="`${props.model?.carNo ?? 'text-red'}`">{{ props.model?.carNo ?? '未定' }}</span>
      </n-space></n-descriptions-item
    >

    <n-descriptions-item label="車両タイプ">
      <n-space>
        <span>{{ props.model?.carType ?? '-' }}</span>
      </n-space></n-descriptions-item
    >

    <n-descriptions-item label="座席数">
      <n-space>
        <span> {{ props.model?.carSeat ? props.model?.carSeat + ' 席' : '-' }}</span>
      </n-space></n-descriptions-item
    >
    <n-descriptions-item label="希望タイプ" :span="2">
      <n-space>
        <span class="">{{ props.model?.specifyCarType ?? '-' }}</span>
      </n-space></n-descriptions-item
    >
    <n-descriptions-item label="出発地" content-class="text-left">
      <n-space>
        <span>{{ `${props.model?.orderFrom ?? '-'}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="出発地詳細" :span="2" content-class="text-left">
      <n-space>
        <span>{{ `${props.model?.orderFromDetails ?? '-'}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="到着地" :span="1" content-class="text-left">
      <n-space>{{ `${props.model?.orderTo ?? '-'}` }} </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="到着地詳細" :span="2" content-class="text-left">
      <n-space>{{ `${props.model?.orderToDetails ?? '-'}` }} </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="都道府県">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.city ?? '-'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="空港">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.airport ?? '-'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="航空便">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.flightNo ?? '-'} ` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="お客様">
      <n-space>
        <span>{{ `${props.model?.customerName}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="請求先" :span="2">
      <n-space>
        <span>{{ `${props.model?.billingAddress ?? '-'}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="連絡方法①" :span="3">
      <n-space>
        <span>{{ `${props.model?.contactMethod1}【${props.model?.contactContent1}】` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item v-if="props.model?.contactMethod2" label="連絡方法②" :span="3">
      <n-space>
        <span>{{ `${props.model?.contactMethod2}【${props.model?.contactContent2}】` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item v-if="props.model?.contactMethod3" label="連絡方法③" :span="3">
      <n-space>
        <span>{{ `${props.model?.contactMethod3}【${props.model?.contactContent3}】` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="大人数">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.adultNum + ' 人'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="子供数">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.childrenNum + ' 人'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="荷物数">
      <n-space>
        <div class="flex items-center">
          {{ `${props.model?.luggageNum + ' 件'}` }}
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="ツアー開始日">
      <n-space>
        <span :class="`${props.model?.startTime ?? 'text-red'}`">{{ props.model?.startTime ?? '未定' }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="旅行日数">
      <n-space>
        {{ `${props.model?.orderDays + ' 日'}` }}
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="終了日時">
      <n-space
        ><span>{{ `${props.model?.endTime ?? '-'}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="注文日時">
      <n-space>
        <span>{{ `${props.model?.createTime}` }}</span>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="注文全額">
      <n-space>
        <div class="flex items-center">
          <span>{{ `${props.model?.orderPrice + ' 円'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>

    <n-descriptions-item label="車備品" :span="3">
      <n-space>
        <div class="flex items-center">
          <span class="">{{ `${props.model?.carAccessories ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="お客様要望" :span="3">
      <n-space>
        <div class="flex items-center">
          <span class="">{{ `${props.model?.customerRemark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
    <n-descriptions-item label="注文備考" :span="3">
      <n-space>
        <div class="flex items-center">
          <span class="">{{ `${props.model?.companyRemark ?? '-'}` }}</span>
        </div>
      </n-space>
    </n-descriptions-item>
  </n-descriptions>
</template>
<script setup lang="ts">
import { PropType, ref } from 'vue';
import { EnumOrderStatus, EnumOrderType } from '@/enum';
import { useMyTags } from '@/composables';

const size = ref<'small' | 'medium' | 'large'>('small');
const { orderStatusTagType } = useMyTags();
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
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
