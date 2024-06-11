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
          <!-- <n-descriptions :size="size" label-placement="left" bordered :column="2" :label-style="labelStyle">
            <n-descriptions-item label="注文金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${modelRef?.orderPrice}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="払込金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${modelRef?.incomeAmount}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="支出金額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${modelRef?.expensesAmount}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
            <n-descriptions-item label="実額(円)">
              <n-space>
                <div class="flex items-center">
                  <span>{{ `${modelRef?.amount}` }}</span>
                </div>
              </n-space>
            </n-descriptions-item>
          </n-descriptions> -->
          <n-space class="mt-0">
            <loading-empty-wrapper class="h-400px" :empty="empty">
              <n-data-table
                striped
                remote
                :loading="loading"
                :columns="columns"
                :data="dataSource"
                :pagination="pagination"
                :flex-height="true"
                :scroll-x="1000"
                class="h-400px"
                @update:page="handlePageChange"
              />
            </loading-empty-wrapper>
          </n-space>
          <!--
          <n-form
            ref="formRef"
            class="mt-10"
            label-placement="left"
            :label-width="120"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-grid :cols="2">
              <n-gi>
                <n-form-item label="操作タイプ" path="payType">
                  <n-input
                    v-model:value="formValue.payType"
                    placeholder="クリックタイプを選択"
                    readonly
                    @click="showDict('pay_type')"
                  />
                </n-form-item>
              </n-gi>
              <n-gi>
                <n-form-item label="支払い方法" path="payMethod">
                  <n-input
                    v-model:value="formValue.payMethod"
                    placeholder="クリック方法を選択"
                    readonly
                    @click="showDict('pay_method')"
                  /> </n-form-item></n-gi
              ><n-gi>
                <n-form-item label="通貨" path="currency">
                  <n-input
                    v-model:value="formValue.currency"
                    placeholder="クリック通貨を選択"
                    readonly
                    @click="showDict('pay_currency')"
                  /> </n-form-item></n-gi
              ><n-gi>
                <n-form-item label="金額" path="amount">
                  <n-input-number v-model:value="formValue.amount" :min="0" :precision="0">
                    <template #prefix> ￥ </template>
                  </n-input-number>
                </n-form-item></n-gi
              ><n-gi>
                <n-form-item label="引取番号" path="payNo">
                  <n-input v-model:value="formValue.payNo" /> </n-form-item></n-gi
              ><n-gi>
                <n-form-item label="備考" path="remark">
                  <n-input v-model:value="formValue.remark" />
                </n-form-item> </n-gi
            ></n-grid>
          </n-form> -->
          <!-- <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space> -->
        </n-spin>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useMessage, useLoadingBar, DataTableColumn } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1200px'
});
const module = 'order';
const moduleParams: MySearch.OrderSearchParams = {
  keyword: '',
  orderStatus: undefined
};

const { searchQuery, handlePageChange, resetParams, pagination, dataSource, loading, empty } =
  useDataTable<MyModel.Order>(module, moduleParams);

resetParams();

const columns: DataTableColumn<MyModel.PayRecord>[] = [
  {
    title: '支払タイプ',
    key: 'payType',
    align: 'left'
  },
  {
    title: '支払方法',
    key: 'payMethod',
    align: 'left'
  },
  {
    title: '金額',
    key: 'amount',
    align: 'left'
  },
  {
    title: '取引番号',
    key: 'payNo',
    align: 'left'
  },
  {
    title: '通貨',
    key: 'currency',
    align: 'left'
  },
  {
    title: '備考',
    key: 'remark',
    align: 'left'
  }
];

const message = useMessage();
const loadingBar = useLoadingBar();
const modelRef = ref<MyModel.PayDetails>();
const showModalRef = ref<boolean | undefined>(false);
const formValue = ref<MyModel.PayRecord>({
  id: '',
  orderId: '',
  orderNo: '',
  payNo: '',
  payMethod: '',
  payMethodCode: '',
  payType: '',
  payTypeCode: '',
  amount: 0,
  currency: '',
  currencyCode: '',
  remark: ''
});

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
  pay: `/order/pay`,
  payDetails: `/order/payDetails`
};
const showModal = (id: string) => {
  searchQuery();
  showModalRef.value = true;
  const promise = request.get<MyModel.PayDetails>(`${urls.payDetails}/${id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        modelRef.value = res.data;
        formValue.value.orderId = modelRef.value.orderId;
        formValue.value.orderNo = modelRef.value.orderNo;
        dataSource.value = res.data.payRecordVOs;
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

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
