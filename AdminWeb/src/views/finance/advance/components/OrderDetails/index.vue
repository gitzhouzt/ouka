<template>
  <div>
    <n-modal
      v-model:show="showModalRef"
      :style="bodyStyleRef"
      transform-origin="center"
      :mask-closable="false"
      preset="card"
      :title="titleRef"
      closable
      @update-show="onUpdateShow"
    >
      <n-card size="huge" role="dialog" aria-modal="true">
        <n-spin :show="loading">
          <n-descriptions :size="size" label-placement="left" bordered :column="3" :label-style="labelStyle">
            <n-descriptions-item label="注文番号" :span="2">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:numbers" /> <span class="ml-4px">{{ formValue.orderVO?.orderNo }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="注文ステータス">
              <n-space>
                <div class="flex items-center">
                  <n-tag :bordered="false" :type="orderStatusTagType(formValue.orderVO?.orderStatus)">{{
                    EnumOrderStatus[formValue.orderVO?.orderStatus as MyEnumType.EnumOrderStatusKey]
                  }}</n-tag>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="ドライバー" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:person" /><span class="ml-4px">{{
                    formValue.driverVO?.staffName ?? '未定'
                  }}</span>
                </div>
              </n-space></n-descriptions-item
            >
            <n-descriptions-item label="合計金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${formValue.orderVO?.orderTotalPrice}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="割引金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${formValue.orderVO?.orderDiscountedPrice}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="払い金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${formValue.orderVO?.orderPrice}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item v-if="formValue.orderVO?.courseType === 'FREE'" label="出発地" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:pin-drop-outline" /><span class="ml-4px">{{
                    `${formValue.orderVO?.orderFromArea} ${formValue.orderVO?.orderFromCity}`
                  }}</span>
                  <span class="ml-4px">{{ `${formValue.orderVO?.orderFrom}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item v-if="formValue.orderVO?.orderToArea" label="到着地" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:pin-drop-outline" /><span class="ml-4px">{{
                    `${formValue.orderVO?.orderToArea} ${formValue.orderVO?.orderToCity}`
                  }}</span>
                  <span class="ml-4px">{{ `${formValue.orderVO?.orderTo}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="お客様連絡先" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:person" /><span class="ml-4px">{{
                    `${formValue.contactVO?.contactName} ${
                      formValue.contactVO?.contactNameKana ? '(' + formValue.contactVO?.contactNameKana + ')' : ''
                    }`
                  }}</span>
                </div>
                <div class="flex items-center">
                  <Icon icon="material-symbols:mail" /><span class="ml-4px">
                    {{ formValue.contactVO?.contactEmail }}</span
                  >
                </div>
                <div class="flex items-center">
                  <Icon icon="material-symbols:phone-enabled" /><span class="ml-4px">
                    {{ formValue.contactVO?.contactPhone }}</span
                  >
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="旅行日時" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:calendar-month" /><span class="ml-4px">{{
                    `${formValue.orderVO?.startTime} - ${formValue.orderVO?.endTime}`
                  }}</span>
                  <span class="ml-10px">{{ `合計 ${formValue.orderVO?.orderDays} 日` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="注文日時" :span="3">
              <n-space>
                <div class="flex items-center">
                  <Icon icon="material-symbols:calendar-month" /><span class="ml-4px">{{
                    `${formValue.orderVO?.createTime}`
                  }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item v-if="formValue.orderVO?.courseType === 'FREE'" label="路線説明" :span="3">
              <n-space>
                <div class="flex items-center">
                  <span class="ml-4px">{{ `${formValue.orderVO?.routeRemark}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="顧客備考" :span="3">
              <n-space>
                <div class="flex items-center">
                  <span class="ml-4px">{{ `${formValue.orderVO?.customerRemark ?? '-'}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
          </n-descriptions>
        </n-spin>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useMessage, useLoadingBar } from 'naive-ui';
import { Icon } from '@iconify/vue';
import { EnumOrderStatus, EnumCourseType } from '@/enum';
import { useMyTags } from '@/composables';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});
const size = ref<'small' | 'medium' | 'large'>('small');
const labelStyle = ref({ width: '15%' });
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.OrderDetails>({
  id: '',
  contactId: '',
  contactVO: undefined,
  driverId: '',
  driverVO: undefined,
  orderVO: undefined
});

const { orderStatusTagType, courseTypeTagType } = useMyTags();
const titleRef = ref<string | undefined>('');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const onUpdateShow = (show: Boolean) => {
  if (!show) {
    emits('close');
  }
};

const urls = {
  details: `order/details`
};
const loading = ref<boolean>(false);
const getDetails = () => {
  const promise = request.get<MyModel.OrderDetails>(`${urls.details}/${formValue.value.id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      const { data } = res;
      if (data) {
        formValue.value.contactId = data.contactId;
        formValue.value.contactVO = data.contactVO;
        formValue.value.driverId = data.driverId;
        formValue.value.driverVO = data.driverVO;
        formValue.value.orderVO = data.orderVO;
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

const showModalRef = ref<boolean>(false);
const showModal = (row: MyModel.OrderDetails | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    getDetails();
  }
};

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
