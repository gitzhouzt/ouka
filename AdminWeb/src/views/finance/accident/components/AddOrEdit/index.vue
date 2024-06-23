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
          <n-form
            ref="formRef"
            label-placement="left"
            :label-width="100"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-grid :cols="2">
              <n-gi>
                <n-form-item label="総額" path="amount">
                  <n-input-number v-model:value="formValue.amount" :precision="0" :show-button="false" /> </n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="保険負担金額" path="insuranceAmount">
                  <n-input-number
                    v-model:value="formValue.insuranceAmount"
                    :precision="0"
                    :show-button="false"
                  /> </n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="会社負担金額" path="companyAmount">
                  <n-input-number
                    v-model:value="formValue.companyAmount"
                    :precision="0"
                    :show-button="false"
                  /> </n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="ドライバー負担金額" path="driverAmount">
                  <n-input-number
                    v-model:value="formValue.driverAmount"
                    :precision="0"
                    :show-button="false"
                  /> </n-form-item
              ></n-gi>
              <n-gi :span="2">
                <n-form-item label="備考" path="remark">
                  <n-input-group>
                    <n-input v-model:value="formValue.remark" placeholder="備考" />
                  </n-input-group> </n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="ステータス" path="status">
                  <n-select
                    v-model:value="formValue.status"
                    :options="selectAccidentStatusOptions"
                    :consistent-menu-width="false"
                  /> </n-form-item
              ></n-gi> </n-grid
          ></n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';
import { useMyOptions } from '@/composables/options';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});

const { selectAccidentStatusOptions } = useMyOptions();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.Accident>({
  id: '',
  insuranceAmount: 0,
  driverAmount: 0,
  amount: 0,
  companyAmount: 0,
  financeTime: new Date().getTime(),
  financeBy: '',
  financeByName: '',
  status: 'WAITING',
  remark: ''
});
const rules = {
  carName: {
    required: true,
    message: '事故車両を選択してください',
    trigger: 'change'
  },
  driverName: {
    required: true,
    message: 'ドライバーを選択してください',
    trigger: 'change'
  },
  type: {
    required: true,
    message: 'タイプを選択してください',
    trigger: 'change'
  },
  details: {
    required: true,
    message: '1~500まで入力してください',
    trigger: 'input'
  },
  confirmBy: {
    required: true,
    message: '1~50まで入力してください',
    trigger: 'input'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Accident | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    formValue.value.carId = row?.carId;
    formValue.value.carNo = row?.carNo;
    formValue.value.carName = row?.carName;
    formValue.value.driverId = row?.driverId;
    formValue.value.driverName = row?.driverName;
    formValue.value.driverNo = row?.driverNo;

    formValue.value.accidentType = row?.accidentType;
    formValue.value.accidentTypeCode = row?.accidentTypeCode;
    formValue.value.details = row?.details;
    formValue.value.confirmBy = row?.confirmBy;
    formValue.value.accidentTime = row?.accidentTime;
    formValue.value.images = row?.images;

    formValue.value.repairTime = row?.repairTime;
    formValue.value.repairBy = row?.repairBy;
    formValue.value.insuranceAmount = row?.insuranceAmount;
    formValue.value.driverAmount = row?.driverAmount;
    formValue.value.amount = row?.amount;
    formValue.value.companyAmount = row?.companyAmount;
    formValue.value.noticeTime = row?.noticeTime;

    formValue.value.status = row?.status;
    formValue.value.remark = row?.remark;
  } else {
    formValue.value.id = '';
    formValue.value.carId = '';
    formValue.value.carNo = '';
    formValue.value.carName = '';
    formValue.value.driverId = '';
    formValue.value.driverName = '';
    formValue.value.driverNo = '';

    formValue.value.accidentType = '';
    formValue.value.accidentTypeCode = '';
    formValue.value.details = '';
    formValue.value.confirmBy = '';
    formValue.value.accidentTime = new Date().getTime();
    formValue.value.images = '';

    formValue.value.repairTime = new Date().getTime();
    formValue.value.repairBy = '';
    formValue.value.insuranceAmount = 0;
    formValue.value.driverAmount = 0;
    formValue.value.amount = 0;
    formValue.value.companyAmount = 0;
    formValue.value.noticeTime = new Date().getTime();

    formValue.value.status = 'WAITING';
    formValue.value.remark = '';
  }
};

const close = () => {
  emits('close');
  showModalRef.value = false;
};
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
  setAmount: `operate/accident/setAmount`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      if (!formValue.value.images) {
        message.warning('写真をアップロードしてください');
        return;
      }
      const params: MyModel.Accident = {
        id: formValue.value.id,

        amount: formValue.value.amount,
        insuranceAmount: formValue.value.insuranceAmount,
        driverAmount: formValue.value.driverAmount,
        companyAmount: formValue.value.companyAmount,

        status: formValue.value.status
      };
      const promise = request.post<Boolean>(`${urls.setAmount}`, params);
      loadingBar.start();
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('保存しました');
            close();
          }
        })
        .catch(err => {
          message.warning(err);
        })
        .finally(() => {
          loading.value = false;
          loadingBar.finish();
        });
    }
  });
};

onMounted(() => {
  nextTick(() => {});
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 300);
});
defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
