<template>
  <div>
    <n-spin :show="loading">
      <n-form ref="formRef" label-placement="left" :label-width="150" :model="formValue" :rules="rules" :size="size">
        <n-grid :cols="2">
          <n-gi>
            <n-form-item label="注文元" path="orderSource">
              <n-input
                v-model:value="formValue.orderSource"
                placeholder="クリック分類を選択"
                readonly
                @click="showDict('order_source', '')"
              />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="第三者&nbsp;&nbsp;" path="orderKey">
              <n-input v-model:value="formValue.orderKey" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="注文番号&nbsp;&nbsp;" path="orderNo">
              <n-input
                v-model:value="formValue.orderNo"
                placeholder="入力なしの場合、自動発行(選択した订单来源＋7桁自動発行 例：Web_0000001)"
              />
            </n-form-item>
          </n-gi>
          <n-gi>
          </n-gi>
          <n-gi>
            <n-form-item label="都道府県&nbsp;&nbsp;" path="city">
              <n-input
                v-model:value="formValue.city"
                placeholder="クリック都道府県を選択"
                readonly
                @click="showDict('order_city', '')"
              /> </n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="责任者" path="sellerName">
              <n-input-group>
                <n-input
                  v-model:value="formValue.sellerName"
                  readonly
                  placeholder="クリック责任者を選択"
                  @click="showStaff()"
                ></n-input>
              </n-input-group> </n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="お客様名" path="customerName">
              <n-input v-model:value="formValue.customerName" /></n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="請求先&nbsp;&nbsp;" path="billingAddress">
              <n-input v-model:value="formValue.billingAddress" /></n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡方法①" path="contactMethod1">
              <n-input
                v-model:value="formValue.contactMethod1"
                placeholder="クリック連絡方法を選択"
                readonly
                @click="showDict('order_contact_method', '1')"
              /> </n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡内容" path="contactContent1">
              <n-input
                v-model:value="formValue.contactContent1"
                @change="
                  value => {
                    onChange(value, '1');
                  }
                " /></n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡方法②&nbsp;&nbsp;">
              <n-input
                v-model:value="formValue.contactMethod2"
                placeholder="クリック連絡方法を選択"
                readonly
                @click="showDict('order_contact_method', '2')"
              /> </n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡内容&nbsp;&nbsp;">
              <n-input
              v-model:value="formValue.contactContent2"
                @change="
                  value => {
                    onChange(value, '2');
                  }
                " /></n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡方法③&nbsp;&nbsp;">
              <n-input
                v-model:value="formValue.contactMethod3"
                placeholder="クリック連絡方法を選択"
                readonly
                @click="showDict('order_contact_method', '3')"
              /> </n-form-item
          ></n-gi>
          <n-gi>
            <n-form-item label="連絡内容&nbsp;&nbsp;">
              <n-input
                v-model:value="formValue.contactContent3"
                @change="
                  value => {
                    onChange(value, '3');
                  }
                " /></n-form-item
          ></n-gi>
        </n-grid>
        <dict-select-modal ref="dictModal" @click="selectDict" />
        <staff-select-modal ref="staffModal" @click="selectStaff" />
      </n-form>
      <n-divider />
      <n-space justify="center">
        <n-button type="primary" @click="next">次へ</n-button>
      </n-space>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { useMyCommon } from '@/composables';
import { request } from '@/service/request';

const emits = defineEmits(['next', 'close']);
const props = defineProps({
  model: {
    type: Object as PropType<MyModel.Order | null>,
    default: () => {
      return {};
    }
  }
});
const { toCDB } = useMyCommon();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const formValue = ref<MyModel.Order>({
  id: props.model?.id ?? '',
  orderNo: props.model?.orderNo,
  orderSource: props.model?.orderSource,
  orderSourceCode: props.model?.orderSourceCode,
  orderKey: props.model?.orderKey,
  city: props.model?.city,
  orderType: props.model?.orderType,
  orderDays: props.model?.orderDays,
  startTime: props.model?.startTime,
  endTime: props.model?.endTime,
  adultNum: props.model?.adultNum,
  childrenNum: props.model?.childrenNum,
  orderStatus: props.model?.orderStatus,
  orderPrice: props.model?.orderPrice,
  orderFromDetails: props.model?.orderFromDetails,
  orderToDetails: props.model?.orderToDetails,
  flightNo: props.model?.flightNo,
  airport: props.model?.airport,

  customerName: props.model?.customerName,
  billingAddress: props.model?.billingAddress,
  customerRemark: props.model?.customerRemark,
  companyRemark: props.model?.companyRemark,
  contactMethod1: props.model?.contactMethod1,
  contactMethod2: props.model?.contactMethod2,
  contactMethod3: props.model?.contactMethod3,
  contactContent1: props.model?.contactContent1,
  contactContent2: props.model?.contactContent2,
  contactContent3: props.model?.contactContent3,

  carId: props.model?.carId,
  carNo: props.model?.carNo,
  carName: props.model?.carName,
  carType: props.model?.carType,
  specifyCarType: props.model?.specifyCarType,
  carSeat: props.model?.carSeat,

  driverId: props.model?.driverId,
  driverNo: props.model?.driverNo,
  driverName: props.model?.driverName,
  driverPhoto: props.model?.driverPhoto,

  sellerId: props.model?.sellerId,
  sellerNo: props.model?.sellerNo,
  sellerName: props.model?.sellerName,
  sellerPhoto: props.model?.sellerPhoto
});

const rules = {
  orderSource: {
    required: true,
    trigger: 'input',
    message: '選択してください'
  },
  orderKey: {
    required: false,
    trigger: 'input',
    message: '1-20文字まで入力してください',
    max: 20
  },
  sellerName: {
    required: true,
    trigger: 'input',
    message: '選択してください'
  },
  orderNo: {
    required: false,
    trigger: 'input',
    message: '20文字まで入力してください',
    max: 20
  },
  customerName: {
    required: true,
    trigger: 'input',
    message: '20文字まで入力してください',
    max: 20
  },
  contactMethod1: {
    required: true,
    trigger: 'input',
    message: '選択してください'
  },
  contactContent1: {
    required: true,
    trigger: 'input',
    message: '200文字まで入力してください',
    max: 200
  },
};

/** ************************ Save Form ************************ */

const urls = {
  setCustomer: `/order/setCustomer`
};

const save = () => {
  const params: MyModel.Order = {
    id: formValue.value.id,
    orderNo: formValue.value.orderNo,
    orderSource: formValue.value.orderSource,
    orderSourceCode: formValue.value.orderSourceCode,
    orderKey: formValue.value.orderKey,
    city: formValue.value.city,

    customerName: formValue.value.customerName,
    contactContent1: formValue.value.contactContent1,
    contactMethod1: formValue.value.contactMethod1,
    contactContent2: formValue.value.contactContent2,
    contactMethod2: formValue.value.contactMethod2,
    contactContent3: formValue.value.contactContent3,
    contactMethod3: formValue.value.contactMethod3,
    billingAddress: formValue.value.billingAddress,

    sellerId: formValue.value.sellerId,
    sellerNo: formValue.value.sellerNo,
    sellerName: formValue.value.sellerName,
    sellerPhoto: formValue.value.sellerPhoto
  };
  const promise = request.post<MyModel.Order>(`${urls.setCustomer}`, params);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        emits('next', { key: 'customer', params: res.data });
      }
    })
    .catch(err => {
      message.warning(err);
    })
    .finally(() => {
      loadingBar.finish();
    });
};
const next = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      save();
    }
  });
};

const onChange = (value: string, flag: string) => {
  const c = `contactContent${flag}`;
  formValue.value[c] = toCDB(value);
};

/** ************************ Save Form ************************ */

const contactFlag = ref<string>('');
const dictModal = ref<any>(null);
const showDict = (dictCode: string, flag: string) => {
  contactFlag.value = flag;
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(dictCode);
};
const selectDict = (result: any) => {
  const m = `contactMethod${contactFlag.value}`;
  switch (result.type) {
    case 'order_source':
      formValue.value.orderSource = result.text;
      formValue.value.orderSourceCode = result.value;
      formValue.value.orderNo = result.value;
      break;
    case 'order_city':
      formValue.value.city = result.text;
      break;
    case 'order_contact_method':
      formValue.value[m] = result.text;
      break;
    case 'order_key':
      formValue.value.orderKey = result.text;
      break;
    default:
      break;
  }
};

const staffModal = ref<any>(null);
const showStaff = () => {
  staffModal.value?.showModal();
};

const selectStaff = (result: any) => {
  formValue.value.sellerId = result.id;
  formValue.value.sellerNo = result.userNo;
  formValue.value.sellerName = result.userName;
  formValue.value.sellerPhoto = result.userAvatar;
};

onMounted(() => {
  loading.value = true;
  setTimeout(() => {
    loading.value = false;
  }, 200);
});
</script>

<style scoped></style>
