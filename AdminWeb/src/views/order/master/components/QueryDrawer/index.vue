<template>
  <div>
    <n-drawer v-model:show="showModalRef" :style="bodyStyleRef" placement="right" :mask-closable="true">
      <n-card class="h-full shadow-sm rounded-16px" :title="titleRef">
        <n-space :vertical="true">
          <n-grid :y-gap="2" :cols="1">
            <n-grid-item>
              <n-form-item label="第三者" path="orderKey">
                <n-input-group>
                  <n-input v-model:value="params.orderKey"></n-input>
                </n-input-group>
              </n-form-item>
              <n-form-item label="責任者" path="sellerName">
                <n-input-group>
                  <n-input
                    v-model:value="params.sellerName"
                    readonly
                    placeholder="クリック責任者を選択"
                    @click="showStaff()"
                  ></n-input>
                </n-input-group>
              </n-form-item>
              <n-form-item label="ドライバー" path="driverName">
                <n-input-group>
                  <n-input
                    v-model:value="params.driverName"
                    readonly
                    placeholder="クリックドライバーを選択"
                    @click="showDriver()"
                  ></n-input>
                </n-input-group>
              </n-form-item>
              <n-form-item label="車両" path="carNo">
                <n-input-group>
                  <n-input
                    v-model:value="params.carNo"
                    readonly
                    placeholder="クリック車両を選択"
                    @click="showCar()"
                  ></n-input>
                </n-input-group>
              </n-form-item>
              <n-form-item label="都道府県">
                <n-input
                  v-model:value="params.city"
                  placeholder="クリック都道府県を選択"
                  readonly
                  @click="showDict('order_city')"
                />
              </n-form-item>
            </n-grid-item>
          </n-grid>
        </n-space>
        <n-space justify="center">
          <n-button type="primary" @click="onClick">検索</n-button>
          <n-button class="ml-2" @click="onReset">リセット</n-button>
        </n-space>
        <staff-select-modal ref="staffModal" @click="selectStaff" />
        <car-select-modal ref="carModal" @click="selectCar" />
        <driver-select-modal ref="driverModal" @click="selectDriver" />
        <dict-select-modal ref="dictModal" @click="selectDict" />
      </n-card>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emits = defineEmits(['click', 'reset']);
const params = ref<any>();
const bodyStyleRef = ref({
  width: '300px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = (searchParams: any) => {
  params.value = searchParams;
  showModalRef.value = true;
};

const titleRef = ref<string | undefined>('詳細検索');
const setTitle = (title: string) => {
  titleRef.value = title;
};
const close = () => {
  showModalRef.value = false;
};

const staffModal = ref<any>(null);
const showStaff = () => {
  staffModal.value?.showModal();
};
const selectStaff = (result: any) => {
  params.value.sellerName = result.userName;
};

const driverModal = ref<any>(null);
const showDriver = () => {
  driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
  params.value.driverName = result.userName;
};

const carModal = ref<any>(null);
const showCar = () => {
  carModal.value?.showModal();
};
const selectCar = (result: any) => {
  params.value.carNo = result.carNo;
};

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'order_source':
      params.value.orderSource = result.text;
      break;
    case 'order_city':
      params.value.city = result.text;
      break;
    default:
      break;
  }
};

const onClick = () => {
  emits('click', params.value);
};
const onReset = () => {
  emits('reset');
};
defineExpose({
  showModal,
  setTitle,
  close
});
</script>

<style scoped></style>
