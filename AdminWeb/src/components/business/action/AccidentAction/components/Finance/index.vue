<template>
  <div>
    <n-spin :show="loading">
      <n-form ref="formRef" label-placement="left" :label-width="100" :model="formValue" :rules="rules" :size="size">
        <n-grid :cols="2">
          <n-gi>
            <n-form-item label="経理通知日時" path="financeNoticeTime">
              <n-date-picker v-model:value="formValue.financeNoticeTime" type="datetime" clearable
            /></n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="総額" path="amount" required>
              <n-input-number v-model:value="formValue.amount" :precision="0" :min="0" :show-button="false">
                <template #suffix> 円 </template>
              </n-input-number></n-form-item
            ></n-gi
          >
          <n-gi>
            <n-form-item label="保険負担" path="insuranceAmount" required>
              <n-input-number v-model:value="formValue.insuranceAmount" :precision="0" :min="0" :show-button="false">
                <template #suffix> 円 </template>
              </n-input-number></n-form-item
            ></n-gi
          >
          <n-gi>
            <n-form-item label="会社負担" path="companyAmount" required>
              <n-input-number v-model:value="formValue.companyAmount" :precision="0" :min="0" :show-button="false">
                <template #suffix> 円 </template>
              </n-input-number></n-form-item
            ></n-gi
          >
          <n-gi>
            <n-form-item label="個人負担" path="driverAmount" required>
              <n-input-number v-model:value="formValue.driverAmount" :precision="0" :min="0" :show-button="false">
                <template #suffix> 円 </template>
              </n-input-number></n-form-item
            ></n-gi
          >
        </n-grid></n-form
      >
      <n-divider />
      <n-space justify="center">
        <n-button @click="prev">前へ</n-button>
        <n-button type="primary" @click="next">次へ</n-button>
      </n-space>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['prev', 'next', 'close']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Accident | null>,
    default: () => {
      return {};
    }
  }
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const formValue = ref<MyModel.Accident>({
  id: props.model?.id ?? '',
  insuranceAmount: props.model?.insuranceAmount ?? 0,
  driverAmount: props.model?.driverAmount ?? 0,
  amount: props.model?.insuranceAmount ?? 0,
  companyAmount: props.model?.driverAmount ?? 0,
  financeAmount: props.model?.financeAmount ?? 0,

  financeNoticeTime: new Date(props.model?.financeNoticeTime ?? new Date()).getTime()
});

const rules = {
  orderType: {
    required: true,
    trigger: 'change',
    message: '選択してください',
    max: 20
  },
  orderFromDetails: {
    required: true,
    trigger: 'input',
    message: '200文字まで入力してください',
    max: 200
  },
  orderToDetails: {
    required: true,
    trigger: 'input',
    message: '200文字まで入力してください',
    max: 200
  },
  customerRemark: {
    required: false,
    trigger: 'input',
    message: '2000文字まで入力してください',
    max: 2000
  }
};

/** ************************ Save Form ************************ */

const urls = {
  setAmount: `operate/accident/setAmount`
};

const save = () => {
  const params: MyModel.Accident = {
    id: formValue.value.id,
    insuranceAmount: formValue.value.insuranceAmount,
    driverAmount: formValue.value.driverAmount,
    amount: formValue.value.amount,
    companyAmount: formValue.value.companyAmount,
    financeAmount: formValue.value.financeAmount,
    financeNoticeTime: formValue.value.financeNoticeTime,
    // financeTime: formValue.value.financeTime,
    // financeBy: formValue.value.financeBy,
    // financeByName: formValue.value.financeByName,
    status: formValue.value.status
  };
  const promise = request.post<MyModel.Accident>(`${urls.setAmount}`, params);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        emits('next', { key: 'finance', params: res.data });
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
  emits('prev', { key: 'finance', params: formValue.value });
};
const next = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      save();
    }
  });
};

/** ************************ Save Form ************************ */

onMounted(() => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 300);
});
</script>

<style scoped></style>
