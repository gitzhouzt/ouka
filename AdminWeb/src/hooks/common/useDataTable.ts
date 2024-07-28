import { reactive, Ref, ref } from 'vue';
import { DataTableSortState } from 'naive-ui';
import { request } from '@/service/request/index';
import useLoadingEmpty from './useLoadingEmpty';

export default function useDataTable<T = MyModel.Root>(module: string, moduleParams: any) {
  const { loading, empty, setEmpty } = useLoadingEmpty();

  const urls = {
    list: `/${module}/list`,
    delete: `/${module}/delete`,
    audit: `/${module}/audit`,
    export: `/${module}/export`
  };

  const aoeModalRef = ref<any>(null);
  const delModalRef = ref<any>(null);

  function handleAdd(title: String) {
    aoeModalRef.value?.setTitle(title);
    aoeModalRef.value?.showModal(null);
  }

  function handleEdit(title: String, row: any) {
    aoeModalRef.value?.setTitle(title);
    aoeModalRef.value?.showModal(row);
  }

  function handleAudit(row: any, obj?: string) {
    loading.value = true;
    const promise = request.put<boolean>(`${urls.audit}/${row.id}`);
    const { isAudit } = row;
    const objStr = obj ? ` ${obj} ` : '';
    promise
      .then(res => {
        if (res.data) {
          window.$message?.success(`${isAudit ? `${objStr}を無効` : `${objStr}を無効解除`}しました`);
        }
      })
      .finally(() => {
        loading.value = false;
        searchQuery();
      });
  }

  function handleDelete(title: String, row: any) {
    delModalRef.value?.setTitle(title);
    delModalRef.value?.showModal(row);
    delModalRef.value?.setCurrent(1);
  }

  const paginationReactive = reactive({
    page: 1,
    pageCount: 1,
    pageSize: 10,
    itemCount: 0,
    showSizePicker: true,
    pageSizes: [10, 30, 50, 100],
    prefix({ itemCount }: { itemCount: any }) {
      return `合計 ${itemCount} 件`;
    }
  });

  const searchParamsReactive = reactive<any>({
    page: paginationReactive.page,
    pageSize: paginationReactive.pageSize
  });

  function resetParams() {
    Object.keys(moduleParams).forEach(p => {
      searchParamsReactive[p] = moduleParams[p];
    });
  }

  const dataSourceRef = ref<T[]>([]) as Ref<T[]>;

  function searchQuery(beforeFun?: Function, afterFun?: Function) {
    loading.value = true;
    searchParamsReactive.page = searchParamsReactive.page ?? paginationReactive.page;
    searchParamsReactive.pageSize = searchParamsReactive.pageSize ?? paginationReactive.pageSize;
    beforeLoad(beforeFun);
    const promise = request.post<MyModel.MyResultArray<T>>(urls.list, searchParamsReactive);
    promise
      .then(res => {
        if (res.data) {
          const { list, page, pageCount, pageSize, itemCount } = res.data as MyModel.MyResultArray<T>;
          dataSourceRef.value = list;
          paginationReactive.page = page;
          paginationReactive.pageSize = pageSize ?? paginationReactive.pageSize;
          paginationReactive.pageCount = pageCount ?? paginationReactive.pageCount;
          paginationReactive.itemCount = itemCount ?? paginationReactive.itemCount;
        } else {
          setEmpty(true);
        }
      })
      .finally(() => {
        afterLoad(afterFun);
        loading.value = false;
      });
  }

  function beforeLoad(fun?: Function) {
    if (fun === undefined) return;
    fun();
  }

  function afterLoad(fun?: Function) {
    if (fun === undefined) return;
    fun();
  }

  function searchReset() {
    resetParams();
    searchParamsReactive.page = 1;
    searchQuery();
  }

  function handlePageChange(currentPage: number) {
    searchParamsReactive.page = currentPage;
    searchQuery();
  }

  function handleUpdatePageSize(pageSize: number) {
    searchParamsReactive.pageSize = pageSize;
    searchParamsReactive.page = 1;
    searchQuery();
  }

  function handleSorter(options: DataTableSortState | DataTableSortState[] | null) {
    searchParamsReactive.sort = options?.columnKey;
    searchParamsReactive.sortType = options?.order;
    searchQuery();
  }

  const handleExport = () => {
    loading.value = true;
    const promise = request.post(urls.export, searchParamsReactive, {
      // responseType: 'arraybuffer'
      responseType: 'blob'
    });
    console.log(promise);

    promise
      .then((res: any) => {
        const { data } = res;
        console.log(res);

        const blob = new Blob([data.file]);
        const objUrl = URL.createObjectURL(blob);
        const fileLink = document.createElement('a');
        fileLink.href = objUrl;
        fileLink.download = decodeURIComponent(data.filename);
        fileLink.click();
        fileLink.remove();
      })
      .finally(() => {
        loading.value = false;
      });
  };

  return {
    handleAdd,
    handleEdit,
    handleAudit,
    handleDelete,
    handlePageChange,
    handleUpdatePageSize,
    handleExport,
    handleSorter,
    searchQuery,
    searchReset,
    resetParams,
    setEmpty,
    beforeLoad,
    afterLoad,
    aoeModal: aoeModalRef,
    delModal: delModalRef,
    searchParams: searchParamsReactive,
    pagination: paginationReactive,
    dataSource: dataSourceRef,
    loading,
    empty
  };
}
