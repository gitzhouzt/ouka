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
      <n-space class="mt-0">
        <loading-empty-wrapper :loading="loading" class="h-400px" :empty="empty">
          <n-data-table
            striped
            remote
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
              <n-form-item label="ファイル名" path="fileName">
                <n-input v-model:value="formValue.fileName" /> </n-form-item
            ></n-gi>
            <n-gi>
              <n-form-item label="シェア" path="share">
                <n-switch v-model:value="formValue.share">
                  <template #checked> 公開 </template>
                  <template #unchecked> 機密 </template>
                </n-switch>
              </n-form-item></n-gi
            >
            <n-gi :span="2">
              <n-form-item label="ファイル" path="userAvatar">
                <n-space>
                  <file-upload
                    ref="fileUploadRef"
                    list-type="text"
                    file-key="files/order"
                    :max="1"
                    :size="20971520"
                    @finish="onFinish"
                  />
                </n-space> </n-form-item></n-gi
            ><n-gi :span="2">
              <n-form-item label="備考" path="remark">
                <n-input v-model:value="formValue.remark" />
              </n-form-item> </n-gi
          ></n-grid>
        </n-form>
        <n-divider />
        <n-space justify="center">
          <n-button type="primary" ghost @click="close">Cancel</n-button>
          <n-button type="primary" @click="handleValidateClick">保存</n-button>
        </n-space>
      </n-spin>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar, DataTableColumn, NEllipsis, NButton, NSwitch } from 'naive-ui';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});
const module = 'order/file';
const moduleParams: MySearch.SearchParams = {
  keyword: ''
};

const { handlePageChange, searchQuery, resetParams, searchParams, pagination, dataSource, loading, empty } =
  useDataTable<MyModel.OrderFile>(module, moduleParams);

resetParams();
const envConfig = getEnvConfig(import.meta.env);
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
// const labelStyle = ref({ width: '20%' });
const message = useMessage();
const loadingBar = useLoadingBar();
const modelRef = ref<MyModel.Order>();
const showModalRef = ref<boolean | undefined>(false);
const formValue = ref<MyModel.OrderFile>({
  id: '',
  orderId: '',
  orderNo: '',
  fileName: '',
  fileUrl: '',
  share: true,
  remark: ''
});
const rules = {
  fileName: {
    required: true,
    message: '入力してください'
  }
};
const urls = {
  addOrEdit: `/order/file/addOrEdit`,
  list: `/order/file/list`,
  deletePhysics: `/order/file/deletePhysics`,
  setShare: `/order/file/setShare`
};
const fileUploadRef = ref<any>();
const reset = () => {
  formValue.value.fileName = '';
  formValue.value.fileUrl = '';
  formValue.value.remark = '';
  fileUploadRef.value.cleanFiles();
};

const showModal = (row: MyModel.Order) => {
  modelRef.value = row;
  formValue.value.orderId = row.id;
  formValue.value.orderNo = row.orderNo ?? '';
  showModalRef.value = true;
  searchParams.orderId = row.id;
  searchQuery();
};

const handleShare = (row: MyModel.OrderFile) => {
  loading.value = true;
  const promise = request.put<boolean>(`${urls.setShare}/${row.id}`);
  const { share } = row;
  const objStr = row.fileName ? ` ${row.fileName} ` : '';
  promise
    .then(res => {
      if (res.data) {
        window.$message?.success(`${!share ? `${objStr}を公開` : `${objStr}を機密`}された`);
      }
    })
    .finally(() => {
      loading.value = false;
      searchQuery();
    });
};

const handleDelete = (row: MyModel.OrderFile) => {
  const promise = request.delete<Boolean>(`${urls.deletePhysics}/${row.id}`);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        message.success('削除しました');
        reset();
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
        fileName: formValue.value.fileName,
        fileUrl: formValue.value.fileUrl,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.addOrEdit}`, params);
      loadingBar.start();
      loading.value = true;
      promise
        .then(res => {
          if (res.data) {
            message.success('保存しました');
            reset();
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
    title: '',
    key: 'share',
    align: 'center',
    width: 50,
    render(row) {
      return h(
        NSwitch,
        { onClick: () => handleShare(row), value: row.share },
        { checked: () => '公開', unchecked: () => '機密' }
      );
    }
  },
  {
    title: 'ファイル名',
    key: 'fileName',
    align: 'center',
    width: 100,
    render(row) {
      const link = h(
        NEllipsis,
        { lineClamp: 1, tooltip: true },
        {
          default: () =>
            h(
              'a',
              { target: '_blank', href: `${envConfig.static}${row.fileUrl}`, class: 'cursor-pointer text-blue' },
              { default: () => row.fileName }
            )
        }
      );
      return link;
    }
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

const onFinish = (files: any) => {
  if (!files || files.length <= 0) {
    formValue.value.fileUrl = '';
    return;
  }
  formValue.value.fileUrl = files[0].url;
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

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
