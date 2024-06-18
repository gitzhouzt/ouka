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
            <n-form-item label="タイプ" path="repairType">
              <n-input
                v-model:value="formValue.repairType"
                placeholder="クリックタイプを選択"
                readonly
                @click="showDict('repair_type')"
              />
            </n-form-item>
            <n-form-item label="修理日時" path="startTime" required>
              <n-date-picker v-model:value="formValue.repairTime" type="date" clearable
            /></n-form-item>
            <n-form-item label="ステータス" path="status">
              <n-select
                v-model:value="formValue.status"
                :options="selectStatusOptions"
                :consistent-menu-width="false"
              />
            </n-form-item>
            <n-form-item label="備考" path="remark">
              <n-input-group>
                <n-input v-model:value="formValue.remark" placeholder="備考" />
              </n-input-group>
            </n-form-item>
          </n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
      <car-select-modal ref="carModal" @click="selectCar" />
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';
import { useMyOptions } from '@/composables/options';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '800px'
});

const { selectStatusOptions } = useMyOptions();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.CarRepair>({
  id: '',
  carNo: '',
  carName: '',
  repairType: '',
  repairTime: new Date().getTime(),
  remark: '',
  status: ''
});
const rules = {
  carName: {
    required: true,
    message: '1-20文字まで入力してください',
    trigger: 'input',
    max: 20
  },
  repairType: {
    required: true,
    message: 'タイプを選択してください',
    trigger: 'change'
  },
  status: {
    required: true,
    message: 'ステータスを選択してください',
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

const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.CarRepair | undefined) => {
  showModalRef.value = true;
  if (row) {
    formValue.value.id = row?.id;
    formValue.value.carId = row?.carId;
    formValue.value.carNo = row?.carNo;
    formValue.value.carName = row?.carName;
    formValue.value.repairType = row?.repairType;
    formValue.value.repairTime = new Date(row?.repairTime ?? new Date()).getTime();
    formValue.value.status = row?.status;
    formValue.value.remark = row?.remark;
  } else {
    formValue.value.id = '';
    formValue.value.carId = '';
    formValue.value.carNo = '';
    formValue.value.carName = '';
    formValue.value.repairType = '';
    formValue.value.repairTime = new Date();
    formValue.value.status = '';
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
  addOrEdit: `car/repair/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.CarRepair = {
        id: formValue.value.id,
        carNo: formValue.value.carNo,
        carName: formValue.value.carName,
        repairType: formValue.value.repairType,
        repairTime: formValue.value.repairTime,
        status: formValue.value.status,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.addOrEdit}`, params);
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
    case 'repair_type':
      formValue.value.repairType = result.text;
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
