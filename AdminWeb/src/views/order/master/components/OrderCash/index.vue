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
      <n-card size="small" role="dialog" aria-modal="true">
        <n-space class="mt-4">
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
        </n-space>

        <n-spin :show="loading">
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
                <n-form-item label="收现名目" path="payItem">
                  <n-input
                    v-model:value="formValue.payItem"
                    placeholder="クリック名目を選択"
                    readonly
                    @click="showDict('pay_cash_item')"
                  />
                </n-form-item> </n-gi
              ><n-gi>
                <n-form-item label="金額" path="amount">
                  <n-input-number v-model:value="formValue.amount" :min="0" :precision="0" :show-button="false">
                    <template #prefix> ￥ </template>
                  </n-input-number>
                </n-form-item></n-gi
              >
              <n-gi :span="2">
                <n-form-item label="備考" path="remark">
                  <n-input
                    v-model:value="formValue.remark"
                    type="textarea"
                    :autosize="{
                      minRows: 2,
                      maxRows: 2
                    }"
                    placeholder="備考を入力してください"
                  />
                </n-form-item> </n-gi
            ></n-grid>
          </n-form>
          <n-divider />
          <n-space justify="center">
            <n-button ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
        <dict-select-modal ref="dictModal" @click="selectDict" />
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn, NEllipsis, NButton } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1200px'
});
const module = 'order/cashItem';
const moduleParams: MySearch.OrderSearchParams = {
  keyword: '',
  orderStatus: undefined
};

const { searchQuery, handlePageChange, searchParams, resetParams, pagination, dataSource, loading, empty } =
  useDataTable<MyModel.Order>(module, moduleParams);

resetParams();

const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const modelRef = ref<MyModel.Order>();
const showModalRef = ref<boolean | undefined>(false);

const formValue = ref<MyModel.PayItem>({
  id: '',
  orderId: '',
  orderNo: '',
  payItem: '',
  payItemCode: '',
  amount: 0,
  remark: ''
});
const rules = {
  payItem: {
    required: true,
    message: '選択してください'
  },
  amount: {
    required: true,
    message: '入力してください'
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
  addOrEdit: `/order/cashItem/addOrEdit`,
  deletePhysics: `/order/cashItem/deletePhysics`
};

const showModal = (row: MyModel.Order) => {
  showModalRef.value = true;
  modelRef.value = row;
  searchParams.orderId = row.id;
  searchQuery();
};
const clearValue = () => {
  formValue.value = {
    id: '',
    orderId: modelRef.value?.id,
    orderNo: modelRef.value?.orderNo,
    payItem: '',
    payItemCode: '',
    amount: 0,
    remark: ''
  };
};
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params = {
        orderId: modelRef.value?.id,
        orderNo: modelRef.value?.orderNo,
        payItem: formValue.value.payItem,
        payItemCode: formValue.value.payItemCode,
        amount: formValue.value.amount,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.addOrEdit}`, params);
      loadingBar.start();
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('保存しました');
            clearValue();
            searchQuery();
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

const handleDelete = (row: MyModel.PayItem) => {
  const promise = request.delete<Boolean>(`${urls.deletePhysics}/${row.id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        message.success('削除しました');
        searchQuery();
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

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'pay_cash_item':
      formValue.value.payItem = result.text;
      formValue.value.payItemCode = result.value;
      break;
    default:
      break;
  }
};

const columns: DataTableColumn<MyModel.PayItem>[] = [
  {
    title: '入金名目',
    key: 'payItem',
    align: 'center',
    width: 100
  },
  {
    title: '金額',
    key: 'amount',
    align: 'center',
    width: 50
  },
  {
    title: '備考',
    key: 'remark',
    align: 'center',
    width: 100,
    render(row) {
      return [h(NEllipsis, { lineClamp: 1, tooltip: true }, { default: () => row.remark ?? '-' })];
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 80,
    align: 'center',
    render(row) {
      const deleteOption = h(
        NButton,
        {
          quaternary: true,
          size: 'small',
          type: 'error',
          onClick: () => handleDelete(row)
        },
        { default: () => '削除' }
      );
      return deleteOption;
    }
  }
];

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
