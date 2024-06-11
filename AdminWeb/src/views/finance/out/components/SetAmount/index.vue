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
              <n-form-item label="備品タイプ" path="goodsType">
                <n-input
                  v-model:value="formValue.goodsType"
                  placeholder="クリック備品タイプを選択"
                  readonly
                  @click="showDict('order_goods_type')"
                />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="件数" path="amount">
                <n-select v-model:value="formValue.amount" :options="options"
              /></n-form-item>
            </n-gi>
            <n-gi :span="2">
              <n-form-item label="備考" path="remark">
                <n-input v-model:value="formValue.remark" />
              </n-form-item>
            </n-gi>
          </n-grid>
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
import { h, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn, NEllipsis, NButton } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});
const module = 'order/goods';
const moduleParams: MySearch.SearchParams = {
  keyword: ''
};

const { handlePageChange, searchQuery, resetParams, searchParams, pagination, dataSource, loading, empty } =
  useDataTable<MyModel.OrderGoods>(module, moduleParams);

resetParams();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
// const labelStyle = ref({ width: '20%' });
const message = useMessage();
const loadingBar = useLoadingBar();
const modelRef = ref<MyModel.Order>();
const showModalRef = ref<boolean | undefined>(false);
const formValue = ref<MyModel.OrderGoods>({
  id: '',
  orderId: '',
  orderNo: '',
  goodsType: '',
  goodsTypeCode: '',
  amount: 1,
  remark: ''
});
const rules = {
  goodsType: {
    required: true,
    message: '入力してください'
  }
};
const urls = {
  addOrEdit: `/order/goods/addOrEdit`,
  list: `/order/goods/list`,
  deletePhysics: `/order/goods/deletePhysics`
};

const options: any = [];
const showModal = (row: MyModel.Order) => {
  modelRef.value = row;
  formValue.value.orderId = row.id;
  formValue.value.orderNo = row.orderNo ?? '';
  showModalRef.value = true;
  searchParams.orderId = row.id;
  for (let index = 0; index < 10; ) {
    const element = {
      label: index + 1,
      value: index + 1
    };
    index += 1;
    options.push(element);
  }
  searchQuery();
};

const handleDelete = (row: MyModel.OrderFile) => {
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

const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params = {
        orderId: formValue.value.orderId,
        orderNo: formValue.value.orderNo,
        goodsType: formValue.value.goodsType,
        goodsTypeCode: formValue.value.goodsTypeCode,
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

const columns: DataTableColumn<MyModel.OrderFile>[] = [
  {
    title: '備品',
    key: 'goodsType',
    align: 'center',
    width: 100
  },
  {
    title: '数量',
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

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'order_goods_type':
      formValue.value.goodsType = result.text;
      formValue.value.goodsTypeCode = result.value;
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
