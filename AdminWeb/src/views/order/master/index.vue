<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile && !isWrap" label-placement="left">
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="キーワード">
							<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="注文番号/連絡先"
								clearable />
						</n-form-item>
						<n-form-item label="注文元">
							<n-input v-model:value="searchParams.orderSource" placeholder="クリック注文元を選択" readonly
								@click="showDict('order_source')" />
						</n-form-item>
					</n-form>
					<n-form :inline="!isMobile" :label-width="100" label-placement="left">
						<n-form-item label="注文内容">
							<n-select v-model:value="searchParams.orderType" class="w-50" :options="orderTypeOptions"
								:consistent-menu-width="false" />
						</n-form-item>
						<n-form-item label="サービス時間" path="selTime">
							<n-date-picker v-model:value="searchParams.selTime" type="datetimerange" format="yyyy/MM/dd HH:mm:ss"
								clearable @update:value="onUpdate" />
						</n-form-item>
					</n-form>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="handleReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<n-space justify="space-between">
				<div>
					<n-button type="primary" @click="handleAction()">新規注文/押車</n-button>
					<n-button type="primary" class="ml-2" @click="handleExport">ダウンロード</n-button>
				</div>
				<div>
					<n-button class="ml-2" @click="handleQuery">詳細検索</n-button>
					<n-button class="ml-2" @click="handleCols">表示項目設定</n-button>
				</div>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="cols" :data="dataSource" :pagination="pagination" :scroll-x="scrollX"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:sorter="handleSorter" @update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<order-action ref="actionModal" @close="searchQuery" />
		<order-details ref="detailsModal" />
		<order-cancel ref="cancelModal" @close="searchQuery" />
		<order-pay ref="payModal" @close="searchQuery" />
		<order-files ref="filesModal" @close="searchQuery" />
		<order-goods ref="goodsModal" @close="searchQuery" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
		<set-cols-drawer ref="colsModal" @click="setCols" />
		<query-drawer ref="queryModal" @click="onQuery" @reset="searchReset" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NEllipsis, NPopconfirm, NTag } from 'naive-ui';
import moment from 'moment';
import { EnumOrderStatus, EnumOrderType } from '@/enum';
import { useMyTags, useMyCommon, useMyOptions } from '@/composables';
import { useDataTable } from '@/hooks';
import { request } from '@/service/request';
import { OrderDetails, OrderCancel, OrderPay, OrderAction, OrderFiles, OrderGoods, QueryDrawer } from './components';
import { getEnvConfig } from '~/.env-config';

const module = 'order';
const moduleParams: MySearch.OrderSearchParams = {
	keyword: '',
	orderSource: '',
	orderKey: '',
	driverName: '',
	sellerName: '',
	carNo: '',
	city: '',
	orderStatus: undefined
	// selTime: [
	//   moment().set('hours', 0).set('minutes', 0).set('seconds', 0).valueOf(),
	//   moment().add(1, 'd').set('hours', 3).set('minutes', 0).set('seconds', 0).valueOf()
	// ],
	// startBeginTime: moment().set('hours', 0).set('minutes', 0).set('seconds', 0).valueOf(),
	// startEndTime: moment().add(1, 'd').set('hours', 3).set('minutes', 0).set('seconds', 0).valueOf()
};

const {
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	handleSorter,
	resetParams,
	handleExport,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.Order>(module, moduleParams);
resetParams();

const handleReset = () => {
	searchParams.orderType = null;
	searchParams.selTime = null;
	searchReset();
};

const envConfig = getEnvConfig(import.meta.env);
const { orderTypeOptions } = useMyOptions();
const { orderStatusTagType } = useMyTags();
const { isMobile, isWrap } = useMyCommon();

const actionModal = ref<any>(null);
const handleAction = (row?: MyModel.Order, current?: number) => {
	actionModal.value.showModal(row, current ?? 1);
};

const detailsModal = ref<any>(null);
const handleDetails = (title: string, row: MyModel.Order) => {
	detailsModal.value.setTitle(`${title} - ${row.orderNo}`);
	detailsModal.value.showModal(row);
};

const cancelModal = ref<any>(null);
const handleCancel = (title: string, row: MyModel.Order) => {
	cancelModal.value.setTitle(`${title} - ${row.orderNo}`);
	cancelModal.value.showModal(row.id);
};

const payModal = ref<any>(null);
const handlePay = (title: string, row: MyModel.Order) => {
	payModal.value.setTitle(`${title} - ${row.orderNo}`);
	payModal.value.showModal(row);
};

const filesModal = ref<any>(null);
const handleFiles = (title: string, row: MyModel.Order) => {
	filesModal.value.setTitle(`${title} - ${row.orderNo}`);
	filesModal.value.showModal(row);
};

const goodsModal = ref<any>(null);
const handleGoods = (title: string, row: MyModel.Order) => {
	goodsModal.value.setTitle(`${title} - ${row.orderNo}`);
	goodsModal.value.showModal(row);
};

const urls = {
	defaultPlacard: `/order/defaultPlacard`,
	recover: `/order/recover`,
	isLodgingTips: `/order/isLodgingTips`
};
const handleDefaultPlacard = (id: string) => {
	loading.value = true;
	const promise = request.post(`${urls.defaultPlacard}/${id}`);
	promise
		.then((res: any) => {
			const { data } = res;
			const fileLink = document.createElement('a');
			fileLink.href = `${envConfig.static}${data}`;
			fileLink.target = '_blank';
			fileLink.click();
			fileLink.remove();
		})
		.finally(() => {
			loading.value = false;
		});
};

const handleRecover = (row: MyModel.Order) => {
	loading.value = true;
	const promise = request.post(`${urls.recover}/${row.id}`);
	promise
		.then((res: any) => {
			const { data } = res;
			if (data) {
				window.$message?.success(`注文 【${row.orderNo}】 戻りました`);
				searchQuery();
			}
		})
		.finally(() => {
			loading.value = false;
		});
};
const handleIsLodgingTips = (row: MyModel.Order) => {
	loading.value = true;
	const flag = !row.isLodgingTips;
	const promise = request.get(`${urls.isLodgingTips}?id=${row.id}&flag=${flag}`);
	promise
		.then((res: any) => {
			const { data } = res;
			if (data) {
				const text = flag ? `注文 【${row.orderNo}】 设置住宿提醒` : `注文 【${row.orderNo}】 已安排住宿`;
				window.$message?.success(text);
				searchQuery();
			}
		})
		.finally(() => {
			loading.value = false;
		});
};

const columns: DataTableColumn<MyModel.Order>[] = [
	{
		title: '詳細',
		key: 'details',
		width: 30,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDetails('注文詳細', row)
				},
				{ default: () => '閲覧' }
			);
			return option;
		}
	},
	{
		title: '注文番号',
		key: 'orderNo',
		align: 'center',
		sorter: true,
		width: 80,
		resizable: true,
		render(row) {
			const newRow = {
				id: '',
				orderNo: row.orderNo,
				orderSource: row.orderSource,
				orderSourceCode: row.orderSourceCode,
				orderKey: row.orderKey,
				city: row.city,

				customerName: row.customerName,
				contactMethod1: row.contactMethod1,
				contactContent1: row.contactContent1,
				contactMethod2: row.contactMethod2,
				contactContent2: row.contactContent2,
				contactMethod3: row.contactMethod3,
				contactContent3: row.contactContent3,
				billingAddress: row.billingAddress,

				sellerId: row.sellerId,
				sellerNo: row.sellerNo,
				sellerName: row.sellerName,
				sellerPhoto: row.sellerPhoto
			};
			const addOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleAction(newRow, 1)
				},
				{ default: () => '追加' }
			);
			const tipsOption = h(
				NPopconfirm,
				{
					positiveText: 'ok',
					negativeText: 'cancel',
					onPositiveClick: () => handleIsLodgingTips(row)
				},
				{
					trigger: () =>
						h(
							NButton,
							{
								quaternary: true,
								size: 'small',
								type: row.isLodgingTips ? 'error' : 'info'
							},
							{ default: () => (row.isLodgingTips ? '住宿必要' : '已安排') }
						),
					default: () => (row.isLodgingTips ? '不再提醒' : '设置住宿提醒')
				}
			);
			let options = [[h('span', {}, { default: () => row.orderNo }), addOption]];
			if (EnumOrderType[row.orderType ?? ''] === EnumOrderType.Haiya && (row.orderDays ?? 0) > 1) {
				options = [[h('span', {}, { default: () => row.orderNo }), tipsOption, addOption]];
			}
			return [
				h(
					'div',
					{
						class: 'flex flex-row items-center justify-between'
					},
					{
						default: () => options
					}
				)
			];
		}
	},
	{
		title: '責任者',
		key: 'sellerName',
		align: 'center',
		sorter: true,
		width: 50,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						class: `${row.sellerName ?? 'text-red'} `,
						style: { textAlign: 'left' }
					},
					{ default: () => (row.sellerName ? row.sellerName : '未定') }
				)
			];
		}
	},
	{
		title: '注文元',
		key: 'orderSource',
		sorter: true,
		align: 'center',
		width: 80,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					{ default: () => (row.orderSource ? row.orderSource : '-') }
				)
			];
		}
	},
	{
		title: '第三者',
		key: 'orderKey',
		align: 'center',
		width: 100,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					{ default: () => (row.orderKey ? row.orderKey : '-') }
				)
			];
		}
	},
	{
		title: '運行内容',
		key: 'orderType',
		align: 'center',
		width: 60,
		resizable: true,
		render(row) {
			const orderType = row.orderType as MyEnumType.EnumOrderTypeKey;
			const status = EnumOrderType[orderType];
			return h(
				'div',
				{
					style: { textAlign: 'left' }
				},
				[
					h(
						'span',
						{},
						{
							default: () => status || '未定'
						}
					)
				]
			);
		}
	},
	{
		title: 'お客様',
		key: 'customerName',
		align: 'center',
		width: 100,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					{ default: () => (row.customerName ? row.customerName : '-') }
				)
			];
		}
	},
	{
		title: '連絡方法①',
		key: 'contactMethod1',
		align: 'center',
		width: 80,
		resizable: true,
		render(row) {
			return h(
				'div',
				{
					style: { textAlign: 'left' }
				},
				[h('span', {}, { default: () => `${row.contactMethod1}：${row.contactContent1}` })]
			);
		}
	},
	{
		title: '連絡方法②',
		key: 'contactMethod2',
		align: 'center',
		width: 80,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					[h('span', {}, row.contactMethod2 ? `${row.contactMethod2}：${row.contactContent2}` : '-')]
				)
			];
		}
	},
	{
		title: '連絡方法③',
		key: 'contactMethod3',
		align: 'center',
		width: 80,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					[h('span', {}, row.contactMethod3 ? `${row.contactMethod3}：${row.contactMethod3}` : '-')]
				)
			];
		}
	},
	{
		title: '大人数',
		key: 'adultNum',
		align: 'center',
		width: 30,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.adultNum ? row.adultNum : '-') }
				)
			];
		}
	},
	{
		title: '子供数',
		key: 'childrenNum',
		align: 'center',
		width: 30,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.childrenNum ? row.childrenNum : '-') }
				)
			];
		}
	},
	{
		title: '荷物数',
		key: 'luggageNum',
		align: 'center',
		width: 30,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.luggageNum ? row.luggageNum : '-') }
				)
			];
		}
	},
	{
		title: '希望車両',
		key: 'specifyCarType',
		align: 'center',
		width: 60,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						class: 'text-right'
					},
					{ default: () => (row.specifyCarType ? row.specifyCarType : '-') }
				)
			];
		}
	},
	{
		title: 'ドライバー',
		key: 'driverName',
		align: 'center',
		sorter: true,
		width: 60,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						class: `${row.driverName ?? 'text-red'} `,
						style: { textAlign: 'left' }
					},
					{ default: () => (row.driverName ? row.driverName : '未定') }
				)
			];
		}
	},
	{
		title: '車両',
		key: 'carName',
		align: 'center',
		sorter: true,
		width: 30,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						class: `${row.carNo ?? 'text-red'} `,
						style: { textAlign: 'left' }
					},
					{ default: () => (row.carNo ? row.carNo : '未定') }
				)
			];
		}
	},
	// {
	//   title: '座席数',
	//   key: 'carSeat',
	//   align: 'center',
	//   width: 80,
	//   render(row) {
	//     return [h('div', {}, { default: () => (row.carSeat ? `${row.carSeat}座` : '-') })];
	//   }
	// },
	// {
	//   title: '空港',
	//   key: 'airport',
	//   align: 'center',
	//   width: 100,
	//   render(row) {
	//     return [h('div', {}, { default: () => (row.airport ? row.airport : '-') })];
	//   }
	// },
	// {
	//   title: '航空便',
	//   key: 'flightNo',
	//   align: 'center',
	//   width: 100,
	//   render(row) {
	//     return [h('div', {}, { default: () => (row.flightNo ? row.flightNo : '-') })];
	//   }
	// },
	{
		title: '出発地',
		key: 'orderFrom',
		align: 'center',
		width: 150,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					[
						h(
							NEllipsis,
							{
								lineClamp: 1,
								tooltip: true
							},
							{ default: () => (row.orderFrom ? row.orderFrom : '-') }
						)
					]
				)
			];
		}
	},
	// {
	//   title: '出発地詳細',
	//   key: 'orderFromDetails',
	//   align: 'center',
	//   width: 300,
	//   render(row) {
	//     return [
	//       h(
	//         NEllipsis,
	//         { lineClamp: 1, tooltip: true },
	//         { default: () => (row.orderFromDetails ? row.orderFromDetails : '-') }
	//       )
	//     ];
	//   }
	// },
	{
		title: '目的地',
		key: 'orderTo',
		align: 'center',
		width: 150,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'left' }
					},
					[
						h(
							NEllipsis,
							{
								lineClamp: 1,
								tooltip: true
							},
							{ default: () => (row.orderTo ? row.orderTo : '-') }
						)
					]
				)
			];
		}
	},
	// {
	//   title: '目的地詳細',
	//   key: 'orderToDetails',
	//   align: 'center',
	//   width: 300,
	//   render(row) {
	//     return [
	//       h(
	//         NEllipsis,
	//         { lineClamp: 1, tooltip: true },
	//         { default: () => (row.orderToDetails ? row.orderToDetails : '-') }
	//       )
	//     ];
	//   }
	// },
	{
		title: 'ツアー開始日',
		key: 'startTime',
		align: 'center',
		sorter: true,
		width: 60,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.startTime ? row.startTime : '未定') }
				)
			];
		}
	},
	{
		title: 'ツアー終了日',
		key: 'endTime',
		align: 'center',
		width: 60,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.endTime ? row.endTime : '未定') }
				)
			];
		}
	},
	{
		title: '注文日時',
		key: 'createTime',
		sorter: true,
		align: 'center',
		width: 60,
		resizable: true,
		render(row) {
			return [
				h(
					'div',
					{
						style: { textAlign: 'right' }
					},
					{ default: () => (row.createTime ? row.createTime : '未定') }
				)
			];
		}
	},
	{
		title: 'ステータス',
		key: 'orderStatus',
		align: 'center',
		width: 30,
		render(row) {
			const orderStatus = row.orderStatus as MyEnumType.EnumOrderStatusKey;
			const status = EnumOrderStatus[orderStatus];
			const tagType = orderStatusTagType(orderStatus);
			return h(
				NTag,
				{
					type: tagType,
					round: true
				},
				{
					default: () => status
				}
			);
		}
	},
	{
		title: '接机牌',
		key: 'placard',
		width: 30,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleDefaultPlacard(row.id)
				},
				{ default: () => '默认模板' }
			);

			return EnumOrderType[row.orderType ?? ''] === EnumOrderType.Airport_S ||
				EnumOrderType[row.orderType ?? ''] === EnumOrderType.Airport_Y
				? option
				: '-';
		}
	},
	{
		title: '備品明細',
		key: 'goods',
		width: 30,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleGoods('備品明細', row)
				},
				{ default: () => '追加' }
			);
			return option;
		}
	},
	// {
	//   title: '收现名目',
	//   key: 'cash',
	//   width: 80,
	//   align: 'center',
	//   render(row) {
	//     const option = h(
	//       NButton,
	//       {
	//         quaternary: true,
	//         size: 'small',
	//         type: 'info',
	//         onClick: () => handleCash('收现名目', row)
	//       },
	//       { default: () => '追加' }
	//     );
	//     return option;
	//   }
	// },
	{
		title: '料金記録',
		key: 'price',
		width: 30,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handlePay('料金記録', row)
				},
				{ default: () => '追加' }
			);
			return option;
		}
	},
	{
		title: '添付資料',
		key: 'files',
		width: 30,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleFiles('添付明細', row)
				},
				{ default: () => '明細' }
			);
			return option;
		}
	},

	{
		title: '編集',
		key: 'edit',
		width: 20,
		align: 'center',
		render(row) {
			const option = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'info',
					onClick: () => handleAction(row, 1)
				},
				{ default: () => '編集' }
			);
			return option;
		}
	},
	{
		title: '操作',
		key: 'actions',
		width: 30,
		align: 'center',
		render(row) {
			const orderStatus = row.orderStatus as MyEnumType.EnumOrderStatusKey;
			const status = EnumOrderStatus[orderStatus];
			const cancelOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'error',
					onClick: () => handleCancel('キャンセル', row)
				},
				{ default: () => 'キャンセル' }
			);
			const recoverOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'default',
					onClick: () => handleRecover(row)
				},
				{ default: () => '戻す' }
			);
			const tipsOption = h('span', {}, { default: () => '-' });
			const options: any = [];
			switch (status) {
				case EnumOrderStatus.Filling:
				case EnumOrderStatus.Assigning:
				case EnumOrderStatus.Sending:
				case EnumOrderStatus.Check:
				case EnumOrderStatus.Booked:
					options.push(cancelOption);
					break;
				case EnumOrderStatus.Working:
				case EnumOrderStatus.Completed:
					options.push(tipsOption);
					break;
				case EnumOrderStatus.Cancelled:
					options.push(recoverOption);
					break;
				default:
					break;
			}
			return h(
				'div',
				{
					class: 'flex flex-col items-center'
				},
				{
					default: () => options
				}
			);
		}
	}
];

const cols = ref<any>(columns);
const scrollX = ref<number>(5000);
const colsModal = ref<any>(null);
const handleCols = () => {
	colsModal.value?.setTitle('表示項目設定');
	colsModal.value?.showModal(columns, cols.value, ['details', 'orderNo', 'edit']);
};
const setCols = (c: any) => {
	cols.value = c;
	const x = c.length === columns.length ? 5000 : 5000 - (columns.length - c.length) * 250;
	scrollX.value = x;
};

const queryModal = ref<any>(null);
const handleQuery = () => {
	queryModal.value?.showModal(searchParams);
};
const onQuery = (params: any) => {
	searchParams.driverName = params.driverName;
	searchParams.carNo = params.carNo;
	searchQuery();
};

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
	dictModal.value?.showModal();
	dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
	switch (result.type) {
		case 'order_source':
			searchParams.orderSource = result.text;
			break;
		default:
			break;
	}
};

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	searchParams.startBeginTime = value ? value[0] : undefined;
	searchParams.startEndTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;
	searchQuery();
});
</script>

<style scoped></style>
