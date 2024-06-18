<template>
  <div>
    <loading-empty-wrapper class="h-400px" :empty="empty" :loading="loading">
      <n-data-table
        striped
        remote
        :loading="loading"
        :columns="columns"
        :data="dataSource"
        :pagination="pagination"
        :flex-height="true"
        class="h-400px"
        @update:page="handlePageChange"
      />
    </loading-empty-wrapper>
    <n-form
      ref="formRef"
      class="mt-10"
      label-placement="left"
      :label-width="120"
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
            @click="showCar()"
          ></n-input>
        </n-input-group>
      </n-form-item>
      <n-form-item path="carRemark" label="変更理由">
        <n-input
          v-model:value="formValue.carRemark"
          :autosize="{
            minRows: 3,
            maxRows: 3
          }"
          placeholder="車両の支配が変更する場合は、理由を説明してください"
          type="textarea"
        />
      </n-form-item>
    </n-form>
    <n-divider />
    <n-space justify="center">
      <n-button @click="prev">前へ</n-button>
      <n-button type="primary" @click="next">次へ</n-button>
    </n-space>
    <car-select-modal ref="carModal" @click="selectCar" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, PropType, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn } from 'naive-ui';
import { useDataTable } from '@/hooks';
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
const module = '';
const moduleParams: MySearch.OrderSearchParams = {
  keyword: '',
  orderStatus: undefined
};
const { handlePageChange, resetParams, pagination, dataSource, loading, empty } = useDataTable<MyModel.Order>(
  module,
  moduleParams
);
resetParams();
const columns: DataTableColumn<MyModel.DeployRecord>[] = [
  {
    title: '車両番号',
    key: 'newNo',
    align: 'left'
  },
  {
    title: '車両名',
    key: 'newName',
    align: 'left'
  },
  {
    title: '備考',
    key: 'remark',
    align: 'left'
  },
  {
    title: '変更時間',
    key: 'createTime',
    align: 'center',
    width: 120
  }
];
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
  carName: {
    required: false,
    message: '選択してください'
  },
  carRemark: {
    required: false,
    message: '車両の支配が変更する場合は、理由を説明してください'
  }
};

const urls = {
  carDeployDetails: `/order/carDeployDetails`
};
const carModal = ref<any>(null);
const showCar = () => {
  carModal.value?.showModal();
};
const selectCar = (result: any) => {
  formValue.value.carId = result.value;
  formValue.value.carName = result.text;
  formValue.value.carNo = result.carNo;
  formValue.value.carType = result.carType;
  formValue.value.carSeat = result.carSeat;
};

const prev = () => {
  emits('prev', { key: 'car', params: formValue.value }); // 将司机信息传回上一步
};
const next = () => {
  emits('next', { key: 'car', params: formValue.value }); // 将司机与车辆信息传到下一步
};
const loadData = () => {
  const promise = request.get<MyModel.DeployDetails>(`${urls.carDeployDetails}/${props.model?.id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        const { data } = res;
        dataSource.value = data.deployRecordVOs ?? [];
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
onMounted(() => {
  loadData();
});
</script>

<style scoped></style>
