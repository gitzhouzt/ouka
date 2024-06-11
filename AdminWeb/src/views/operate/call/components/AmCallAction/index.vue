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
            :label-width="150"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-grid :cols="2">
              <n-gi :span="2">
                <n-form-item label="ドライバー" path="driverName">
                  <n-input-group>
                    <n-input
                      v-model:value="formValue.driverName"
                      readonly
                      placeholder="クリックドライバーを選択"
                      :disabled="!!formValue.id"
                      @click="showDriver()"
                    ></n-input>
                    <n-input-group-label>番号</n-input-group-label>
                    <n-input
                      v-model:value="formValue.driverNo"
                      :disabled="true"
                      placeholder="ドライバーを選択して表示"
                    />
                  </n-input-group>
                </n-form-item>
              </n-gi>
              <n-gi :span="2">
                <n-form-item label="車両" path="carName">
                  <n-input-group>
                    <n-input
                      v-model:value="formValue.carName"
                      readonly
                      placeholder="クリック車両を選択"
                      :disabled="!!formValue.id"
                      @click="showCar()"
                    ></n-input>
                    <n-input-group-label>番号</n-input-group-label>
                    <n-input v-model:value="formValue.carNo" :disabled="true" placeholder="車両を選択して表示" />
                  </n-input-group>
                </n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="確認者" path="amConfirmBy">
                  <n-input
                    v-model:value="formValue.amConfirmBy"
                    readonly
                    placeholder="クリックスタッフを選択"
                    @click="showStaff('amConfirmBy')"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="点呼方法" path="amCallMethod">
                  <n-input
                    v-model:value="formValue.amCallMethod"
                    placeholder="クリック方法を選択"
                    readonly
                    @click="showDict('operate_call_method')"
                  /> </n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="点呼日付" path="date">
                  <n-input v-model:value="formValue.date" :disabled="true"
                /></n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="点呼時間" path="amCallTime">
                  <n-time-picker v-model:value="formValue.amCallTime" clearable
                /></n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="アルコール検知器" path="am1">
                  <n-radio-group v-model:value="formValue.am1" name="am1Rdo">
                    <n-radio-button key="am1_true" value="有" label="有" />
                    <n-radio-button key="am1_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="酒気帯びの有無" path="am2">
                  <n-radio-group v-model:value="formValue.am2" name="am2Rdo">
                    <n-radio-button key="am2_true" value="有" label="有" />
                    <n-radio-button key="am2_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="異常の有無" path="am3">
                  <n-radio-group v-model:value="formValue.am3" name="am3Rdo">
                    <n-radio-button key="am3_true" value="有" label="有" />
                    <n-radio-button key="am3_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="睡眠不足有無" path="am4">
                  <n-radio-group v-model:value="formValue.am4" name="am4Rdo">
                    <n-radio-button key="am4_true" value="有" label="有" />
                    <n-radio-button key="am4_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="運行管理者" path="manager">
                  <n-input
                    v-model:value="formValue.manager"
                    readonly
                    placeholder="クリックスタッフを選択"
                    @click="showStaff('manager')"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="運行補佐者" path="helper">
                  <n-input
                    v-model:value="formValue.helper"
                    readonly
                    placeholder="クリックスタッフを選択"
                    @click="showStaff('helper')"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi :span="2">
                <n-form-item label="備考" path="amRemark">
                  <n-input-group>
                    <n-input v-model:value="formValue.amRemark" placeholder="備考" />
                  </n-input-group> </n-form-item
              ></n-gi> </n-grid
          ></n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
      <car-select-modal ref="carModal" @click="selectCar" />
      <driver-select-modal ref="driverModal" @click="selectDriver" />
      <staff-select-modal ref="staffModal" @click="selectStaff" />
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import moment from 'moment';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});

const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.Call>({
  id: '',
  carId: '',
  carNo: '',
  carName: '',
  driverId: '',
  driverNo: '',
  driverName: '',
  date: moment().format('yyyy/MM/DD'),
  amCallMethod: '',
  amCallTime: new Date().getTime(),
  am1: '有',
  am2: '無',
  am3: '無',
  am4: '無',
  amConfirmBy: '',
  amRemark: '',

  manager: '',
  helper: ''
});
const rules = {
  carName: {
    required: true,
    message: '車両を選択してください',
    trigger: 'change'
  },
  driverName: {
    required: true,
    message: 'ドライバーを選択してください',
    trigger: 'change'
  },
  amConfirmBy: {
    required: true,
    message: '確認者を選択してください',
    trigger: 'input'
  },
  amCallMethod: {
    required: true,
    message: '点呼方法を選択してください',
    trigger: 'change'
  },
  manager: {
    required: true,
    message: '管理者を選択してください',
    trigger: 'change'
  }
};

const carModal = ref<any>(null);
const showCar = () => {
  carModal.value?.showModal();
};
const selectCar = (result: any) => {
  formValue.value.carId = result.value;
  formValue.value.carName = result.text;
  formValue.value.carNo = result.carNo;
};

const driverModal = ref<any>(null);
const showDriver = () => {
  driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
  formValue.value.driverId = result.id;
  formValue.value.driverName = result.userName;
  formValue.value.driverNo = result.userNo;
};

const staffModal = ref<any>(null);
const staffFlag = ref();
const showStaff = (flag: string) => {
  staffModal.value?.showModal();
  staffFlag.value = flag;
};
const selectStaff = (result: any) => {
  const flag = staffFlag.value;
  formValue.value[flag] = result.userName;
};

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Call | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    formValue.value.carId = row?.carId;
    formValue.value.carNo = row?.carNo;
    formValue.value.carName = row?.carName;
    formValue.value.driverId = row?.driverId;
    formValue.value.driverName = row?.driverName;
    formValue.value.driverNo = row?.driverNo;

    formValue.value.date = row?.date;
    formValue.value.amCallMethod = row?.amCallMethod;
    formValue.value.amCallTime = new Date(row?.amCallTime ?? new Date()).getTime();
    formValue.value.am1 = row?.am1;
    formValue.value.am2 = row?.am2;
    formValue.value.am3 = row?.am3;
    formValue.value.am4 = row?.am4;
    formValue.value.amConfirmBy = row?.amConfirmBy;
    formValue.value.amRemark = row?.amRemark;

    formValue.value.manager = row?.manager;
    formValue.value.helper = row?.helper;
  } else {
    formValue.value.id = '';
    formValue.value.carId = '';
    formValue.value.carNo = '';
    formValue.value.carName = '';
    formValue.value.driverId = '';
    formValue.value.driverName = '';
    formValue.value.driverNo = '';

    formValue.value.date = moment().format('yyyy/MM/DD');
    formValue.value.amCallMethod = '';
    formValue.value.amCallTime = new Date().getTime();
    formValue.value.am1 = '有';
    formValue.value.am2 = '無';
    formValue.value.am3 = '無';
    formValue.value.am4 = '無';
    formValue.value.amConfirmBy = '';
    formValue.value.amRemark = '';

    formValue.value.manager = '';
    formValue.value.helper = '';
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
  setAmCall: `operate/call/setAmCall`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.Call = {
        id: formValue.value.id,
        carId: formValue.value.carId,
        carNo: formValue.value.carNo,
        carName: formValue.value.carName,
        driverId: formValue.value.driverId,
        driverNo: formValue.value.driverNo,
        driverName: formValue.value.driverName,

        date: formValue.value.date,
        amCallMethod: formValue.value.amCallMethod,
        amCallTime: formValue.value.amCallTime,
        am1: formValue.value.am1,
        am2: formValue.value.am2,
        am3: formValue.value.am3,
        am4: formValue.value.am4,
        amConfirmBy: formValue.value.amConfirmBy,
        amRemark: formValue.value.amRemark,

        manager: formValue.value.manager,
        helper: formValue.value.helper
      };
      const promise = request.post<Boolean>(`${urls.setAmCall}`, params);
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

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'operate_call_method':
      formValue.value.amCallMethod = result.text;
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
