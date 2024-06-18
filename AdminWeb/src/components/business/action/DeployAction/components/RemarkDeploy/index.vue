<template>
  <div>
    <n-form
      ref="formRef"
      class="mt-10"
      label-placement="left"
      :label-width="120"
      :model="formValue"
      :rules="rules"
      :size="size"
    >
      <n-form-item path="companyRemark" label="注文備考">
        <n-input
          v-model:value="formValue.companyRemark"
          :autosize="{
            minRows: 3,
            maxRows: 3
          }"
          placeholder="到着時間、出発時間などの注文備考を記入してください"
          type="textarea"
        />
      </n-form-item>
    </n-form>
    <n-divider />
    <n-space justify="center">
      <n-button @click="prev">前へ</n-button>
      <n-button type="primary" @click="next">次へ</n-button>
    </n-space>
  </div>
</template>

<script setup lang="ts">
import { PropType, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['prev', 'next', 'close']);
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
  }
});

const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.DeployDetails>({
  id: '',
  orderId: props.model?.id,
  orderNo: props.model?.orderNo,

  driverId: props.deployModel?.driverId,
  driverNo: props.deployModel?.driverNo,
  driverName: props.deployModel?.driverName,
  driverPhoto: props.deployModel?.driverPhoto,
  driverRemark: props.deployModel?.driverRemark,

  carId: props.deployModel?.carId,
  carNo: props.deployModel?.carNo,
  carName: props.deployModel?.carName,
  carType: props.deployModel?.carType,
  carSeat: props.deployModel?.carSeat,
  carRemark: props.deployModel?.carRemark,

  companyRemark: props.deployModel?.companyRemark
});
const rules = {
  companyRemark: {
    required: false,
    message: '注文備考を記入してください',
    max: 2000
  }
};

const urls = {
  setCompanyRemark: `/order/setCompanyRemark`
};

const setCompanyRemark = () => {
  const params = {
    id: formValue.value.orderId,
    companyRemark: formValue.value.companyRemark
  };
  const promise = request.post<Boolean>(`${urls.setCompanyRemark}`, params);
  loadingBar.start();
  promise
    .then(res => {
      if (res.data) {
        emits('next', { key: 'remark', params: formValue.value }); // 将司机与车辆信息传到下一步
      }
    })
    .catch(err => {
      message.warning(err);
    })
    .finally(() => {
      loadingBar.finish();
    });
};

const prev = () => {
  emits('prev', { key: 'remark', params: props.deployModel }); // 将司机与车辆信息传到上一步
};
const next = () => {
  setCompanyRemark();
};
</script>

<style scoped></style>
