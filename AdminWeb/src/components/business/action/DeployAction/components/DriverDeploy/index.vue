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
      <n-form-item label="ドライバー" path="driverName">
        <n-input-group>
          <n-input
            v-model:value="formValue.driverName"
            readonly
            placeholder="クリックドライバーを選択"
            @click="showDriver()"
          ></n-input>
        </n-input-group>
      </n-form-item>
      <n-form-item path="driverRemark" label="変更理由">
        <n-input
          v-model:value="formValue.driverRemark"
          :autosize="{
            minRows: 3,
            maxRows: 3
          }"
          placeholder="ドライバーの支配が変更する場合は、理由を説明してください"
          type="textarea"
        />
      </n-form-item>
    </n-form>
    <n-divider />
    <n-space justify="center">
      <n-button type="primary" @click="next">次へ</n-button>
    </n-space>
    <driver-select-modal ref="driverModal" @click="selectDriver" />
  </div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const emits = defineEmits(['next', 'close']);
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
    title: '番号',
    key: 'newNo',
    align: 'left'
  },
  {
    title: 'ドライバー名',
    key: 'newName',
    align: 'left'
  },
  {
    title: '変更理由',
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
  driverName: {
    required: false,
    message: '選択してください'
  },
  driverRemark: {
    required: false,
    message: 'ドライバーの支配が変更する場合は、理由を説明してください'
  }
};

const urls = {
  driverDeployDetails: `/order/driverDeployDetails`
};

const driverModal = ref<any>(null);
const showDriver = () => {
  driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
  formValue.value.driverId = result.id;
  formValue.value.driverName = result.userName;
  formValue.value.driverNo = result.userNo;
  formValue.value.driverPhoto = result.userAvatar;
};
const loadData = () => {
  const promise = request.get<MyModel.DeployDetails>(`${urls.driverDeployDetails}/${props.model?.id}`);
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
const next = () => {
  emits('next', { key: 'driver', params: formValue.value }); // 将司机信息传到下一步
};
onMounted(() => {
  loadData();
});
</script>

<style scoped></style>
