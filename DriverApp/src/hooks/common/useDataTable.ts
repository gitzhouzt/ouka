import { reactive, Ref, ref } from 'vue';
import { showLoadingToast, closeToast } from 'vant';
import { request } from '@/service/request/index';
import useLoadingEmpty from './useLoadingEmpty';

export default function useDataTable<T = MyModel.Root>(
	module: string,
	moduleParams: any
) {
	const { loading, empty, setEmpty } = useLoadingEmpty();
	const finishedRef = ref(false);
	const refreshingRef = ref(false);

	const urls = {
		list: `/${module}/list`,
		delete: `/${module}/delete`,
		audit: `/${module}/audit`,
		export: `/${module}/export`,
	};

	const paginationReactive = reactive({
		page: 1,
		pageCount: 1,
		pageSize: 10,
		itemCount: 0,
		prefix({ itemCount }: { itemCount: any }) {
			return `合計 ${itemCount} 件`;
		},
	});

	const searchParamsReactive = reactive<any>({
		page: paginationReactive.page,
		pageSize: paginationReactive.pageSize,
	});

	const resetParams = () => {
		Object.keys(moduleParams).forEach((p) => {
			searchParamsReactive[p] = moduleParams[p];
		});
	};

	const dataSourceRef = ref<T[]>([]) as Ref<T[]>;

	const loadData = () => {
		loading.value = true;
		showLoadingToast('loading');
		if (refreshingRef.value) {
			dataSourceRef.value = [];
			refreshingRef.value = false;
		}
		searchParamsReactive.page =
			searchParamsReactive.page ?? paginationReactive.page;
		searchParamsReactive.pageSize =
			searchParamsReactive.pageSize ?? paginationReactive.pageSize;
		const promise = request.post<MyModel.MyResultArray<T>>(
			urls.list,
			searchParamsReactive
		);
		promise
			.then((res) => {
				if (res.data) {
					const { list, page, pageCount, pageSize, itemCount } =
						res.data as MyModel.MyResultArray<T>;
					dataSourceRef.value = [...dataSourceRef.value, ...list];
					paginationReactive.page = page;
					paginationReactive.pageSize = pageSize ?? paginationReactive.pageSize;
					paginationReactive.pageCount =
						pageCount ?? paginationReactive.pageCount;
					paginationReactive.itemCount =
						itemCount ?? paginationReactive.itemCount;
				} else {
					setEmpty(true);
				}
			})
			.finally(() => {
				loading.value = false;
				closeToast();
			});
	};

	const searchQuery = () => {
		finishedRef.value = false;
		dataSourceRef.value = [];
		searchParamsReactive.page = 1;
		loadData();
	};

	const searchReset = () => {
		resetParams();
		searchQuery();
	};

	const handleRefresh = () => {
		finishedRef.value = false;
		searchParamsReactive.page = 1;
		loadData();
	};

	const handlePageChange = () => {
		if (paginationReactive.pageCount >= searchParamsReactive.page + 1) {
			searchParamsReactive.page += 1;
			loadData();
		} else {
			finishedRef.value = true;
		}
	};

	const handleExport = () => {
		loading.value = true;
		showLoadingToast('loading');
		const promise = request.post(urls.export, searchParamsReactive, {
			responseType: 'arraybuffer',
		});
		promise
			.then((res: any) => {
				const { data } = res;
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
				closeToast();
			});
	};

	return {
		handlePageChange,
		handleExport,
		handleRefresh,
		searchQuery,
		searchReset,
		resetParams,
		setEmpty,
		searchParams: searchParamsReactive,
		pagination: paginationReactive,
		dataSource: dataSourceRef,
		finished: finishedRef,
		refreshing: refreshingRef,
		loading,
		empty,
	};
}
