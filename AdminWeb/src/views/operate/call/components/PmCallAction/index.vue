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
                    ></n-input>
                    <n-input-group-label>番号</n-input-group-label>
                    <n-input v-model:value="formValue.carNo" :disabled="true" placeholder="車両を選択して表示" />
                  </n-input-group>
                </n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="確認者" path="pmConfirmBy">
                  <n-input
                    v-model:value="formValue.pmConfirmBy"
                    readonly
                    placeholder="クリックスタッフを選択"
                    @click="showStaff('pmConfirmBy')"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="点呼方法" path="pmCallMethod">
                  <n-input
                    v-model:value="formValue.pmCallMethod"
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
                <n-form-item label="点呼日" path="pmCallTime">
                  <n-time-picker v-model:value="formValue.pmCallTime" clearable
                /></n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="アルコール検知器" path="pm1">
                  <n-radio-group v-model:value="formValue.pm1" name="pm1Rdo">
                    <n-radio-button key="pm1_true" value="有" label="有" />
                    <n-radio-button key="pm1_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="酒気帯びの有無" path="pm2">
                  <n-radio-group v-model:value="formValue.pm2" name="pm2Rdo">
                    <n-radio-button key="pm2_true" value="有" label="有" />
                    <n-radio-button key="pm2_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="異常の有無" path="pm3">
                  <n-radio-group v-model:value="formValue.pm3" name="pm3Rdo">
                    <n-radio-button key="pm3_true" value="有" label="有" />
                    <n-radio-button key="pm3_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="睡眠不足有無" path="pm4">
                  <n-radio-group v-model:value="formValue.pm4" name="pm4Rdo">
                    <n-radio-button key="pm4_true" value="有" label="有" />
                    <n-radio-button key="pm4_false" value="無" label="無" /> </n-radio-group></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="運行管理者" path="manager">
                  <n-input
                    v-model:value="formValue.manager"
                    readonly
                    :disabled="!!formValue.id"
                    placeholder="クリックスタッフを選択"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi>
                <n-form-item label="運行補佐者" path="helper">
                  <n-input
                    v-model:value="formValue.helper"
                    readonly
                    :disabled="!!formValue.id"
                    placeholder="クリックスタッフを選択"
                  ></n-input></n-form-item
              ></n-gi>
              <n-gi :span="2">
                <n-form-item label="備考" path="pmRemark">
                  <n-input-group>
                    <n-input v-model:value="formValue.pmRemark" placeholder="備考" />
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
      <staff-select-modal ref="staffModal" @click="selectStaff" />
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
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

  date: `${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}`,

  pmCallMethod: '',
  pmCallTime: new Date().getTime(),
  pm1: '有',
  pm2: '無',
  pm3: '無',
  pm4: '無',
  pmConfirmBy: '',
  pmRemark: '',

  manager: '',
  helper: ''
});
const rules = {
  pmConfirmBy: {
    required: true,
    message: '確認者を選択してください',
    trigger: 'input'
  },
  pmCallMethod: {
    required: true,
    message: '点呼方法を選択してください',
    trigger: 'change'
  }
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
  console.debug(row);

  if (row) {
    formValue.value.id = row?.id;
    formValue.value.carId = row?.carId;
    formValue.value.carNo = row?.carNo;
    formValue.value.carName = row?.carName;
    formValue.value.driverId = row?.driverId;
    formValue.value.driverName = row?.driverName;
    formValue.value.driverNo = row?.driverNo;
    formValue.value.date = row?.date;

    formValue.value.manager = row?.manager;
    formValue.value.helper = row?.helper;

    formValue.value.pmCallMethod = row?.pmCallMethod;
    formValue.value.pmCallTime = new Date(row?.pmCallTime ?? new Date()).getTime();
    formValue.value.pm1 = row?.pm1 ?? '有';
    formValue.value.pm2 = row?.pm2 ?? '無';
    formValue.value.pm3 = row?.pm3 ?? '無';
    formValue.value.pm4 = row?.pm4 ?? '無';
    formValue.value.pmConfirmBy = row?.pmConfirmBy;
    formValue.value.pmRemark = row?.pmRemark;
  } else {
    formValue.value.id = '';
    formValue.value.carId = '';
    formValue.value.carNo = '';
    formValue.value.carName = '';
    formValue.value.driverId = '';
    formValue.value.driverName = '';
    formValue.value.driverNo = '';
    formValue.value.date = `${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}`;

    formValue.value.manager = '';
    formValue.value.helper = '';

    formValue.value.pmCallMethod = '';
    formValue.value.pmCallTime = new Date().getTime();
    formValue.value.pm1 = '有';
    formValue.value.pm2 = '無';
    formValue.value.pm3 = '無';
    formValue.value.pm4 = '無';
    formValue.value.pmConfirmBy = '';
    formValue.value.pmRemark = '';
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
  setPmCall: `operate/call/setPmCall`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.Call = {
        id: formValue.value.id,

        pmCallMethod: formValue.value.pmCallMethod,
        pmCallTime: formValue.value.pmCallTime,
        pm1: formValue.value.pm1,
        pm2: formValue.value.pm2,
        pm3: formValue.value.pm3,
        pm4: formValue.value.pm4,
        pmConfirmBy: formValue.value.pmConfirmBy,
        pmRemark: formValue.value.pmRemark
      };
      const promise = request.post<Boolean>(`${urls.setPmCall}`, params);
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
      formValue.value.pmCallMethod = result.text;
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
