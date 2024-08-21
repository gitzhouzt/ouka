<template>
  <div>
    <n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
      preset="card" :title="titleRef" closable @update-show="onUpdateShow">
      <n-spin :show="loading">
        <n-form ref="formRef" label-placement="left" :label-width="100" :model="formValue" :rules="rules" :size="size">
          <n-grid :cols="2">
            <n-gi>
              <n-form-item label="決算タイプ" path="financeType">
                <n-select v-model:value="formValue.financeType" :options="selectFinanceTypeOptions"
                  :consistent-menu-width="false" @update:value="onSelect" />
              </n-form-item>
            </n-gi>
            <n-gi v-if="EnumFinanceType[formValue.financeType ?? ''] !== '前受金'">
              <n-form-item label="決算名目" path="payItem">
                <n-input v-model:value="formValue.payItem" placeholder="クリック名目を選択" readonly
                  @click="showDict(payItemCode)" />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="通貨" path="currency">
                <n-input v-model:value="formValue.currency" placeholder="クリック通貨を選択" readonly
                  @click="showDict('pay_currency')" /> </n-form-item></n-gi><n-gi>
              <n-form-item label="通貨金額" path="currencyAmount">
                <n-input-number v-model:value="formValue.currencyAmount" :min="0" :precision="0" :show-button="false">
                  <template #prefix> {{ formValue.currencyCode === 'usd' ? '$' : '￥' }} </template>
                </n-input-number>
              </n-form-item></n-gi>
            <n-gi>
              <n-form-item label="是否收现">
                <n-radio-group v-model:value="isCash" name="radCash">
                  <n-radio-button key="是" :value="true" label="是" />
                  <n-radio-button key="否" :value="false" label="否" />
                </n-radio-group> </n-form-item></n-gi>
            <n-gi :span="2">
              <n-form-item label="備考" path="remark">
                <n-input v-model:value="formValue.remark" type="textarea" :autosize="{
                  minRows: 2,
                  maxRows: 2
                }" placeholder="備考を入力してください" />
              </n-form-item> </n-gi></n-grid>
        </n-form>
        <n-divider />
        <n-space justify="center">
          <n-button type="primary" ghost @click="close">Cancel</n-button>
          <n-button type="primary" @click="handleValidateClick">保存</n-button>
        </n-space>
      </n-spin>
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { PropType, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { EnumFinanceType } from '@/enum';
import { useMyOptions } from '@/composables';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
    default: () => {
      return {};
    }
  }
});
const bodyStyleRef = ref({
  width: '800px'
});

const { selectFinanceTypeOptions } = useMyOptions();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.PayRecord>({
  id: '',
  orderId: props.model?.id,
  orderVO: props.model ?? undefined,
  payMethod: '',
  payMethodCode: '',
  payItem: '',
  payItemCode: '',
  financeType: 'InEarnings',
  amount: 0,
  currencyAmount: 0,
  currency: '',
  currencyCode: '',
  remark: ''
});

const isCash = ref(false);
const rules = {
  payItem: {
    required: true,
    message: '選択してください'
  },
  financeType: {
    required: true,
    message: '選択してください'
  },
  currency: {
    required: true,
    message: '選択してください'
  },
  currencyAmount: {
    required: true,
    message: '入力してください'
  },
  amount: {
    required: true,
    message: '入力してください'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = async (row: MyModel.PayRecord | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    formValue.value.orderId = row?.orderId;
    formValue.value.orderVO = row?.orderVO;
    formValue.value.payMethod = row?.payMethod;
    formValue.value.payMethodCode = row?.payMethodCode;
    formValue.value.payItem = row?.payItem;
    formValue.value.payItemCode = row?.payItemCode;
    formValue.value.financeType = row?.financeType;
    formValue.value.amount = row?.amount ?? 0;
    formValue.value.currencyAmount = row?.currencyAmount;
    formValue.value.currency = row?.currency;
    formValue.value.currencyCode = row?.currencyCode;
    formValue.value.remark = row?.remark;
    isCash.value = formValue.value.payMethodCode === 'cash';
  } else {
    formValue.value = {
      id: '',
      orderId: props.model?.id,
      orderVO: props.model ?? undefined,
      payMethod: '',
      payMethodCode: '',
      payItem: '',
      payItemCode: '',
      financeType: 'InEarnings',
      amount: 0,
      currencyAmount: 0,
      currency: '',
      currencyCode: '',
      remark: ''
    };
    isCash.value = false;
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
  adminPay: `/order/adminPay`,
  deletePhysics: `/order/pay/deletePhysics`,
  getByCode: `/order/getByCode`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.PayRecord = {
        id: formValue.value.id,
        orderId: props.model?.id,
        payMethod: isCash.value ? '現金' : formValue.value.payMethod,
        payMethodCode: isCash.value ? 'cash' : formValue.value.payMethodCode,
        payItem: formValue.value.payItem,
        payItemCode: formValue.value.payItemCode,
        financeType: formValue.value.financeType,
        amount: formValue.value.amount,
        currency: formValue.value.currency,
        currencyCode: formValue.value.currencyCode,
        currencyAmount: formValue.value.currencyAmount,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.adminPay}`, params);
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
const payItemCode = ref<string>('pay_earnings_item');

const onSelect = (value: any, option: any) => {
  formValue.value.payItem = '';
  formValue.value.payItemCode = '';

  switch (value) {
    case 'InEarnings':
      payItemCode.value = 'pay_earnings_item';
      break;
    case 'OutCost':
      payItemCode.value = 'pay_cost_item';
      break;
    case 'InAdvance':
    case 'OutAdvance':
      payItemCode.value = 'pay_advance_item';
      break;

    default:
      break;
  }
};
const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'pay_method':
      formValue.value.payMethod = result.text;
      formValue.value.payMethodCode = result.value;
      break;
    case 'pay_currency':
      formValue.value.currency = result.text;
      formValue.value.currencyCode = result.value;
      break;
    case 'pay_earnings_item':
    case 'pay_advance_item':
    case 'pay_cost_item':
    case 'pay_cash_item':
      formValue.value.payItem = result.text;
      formValue.value.payItemCode = result.value;
      break;
    default:
      break;
  }
};
defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
