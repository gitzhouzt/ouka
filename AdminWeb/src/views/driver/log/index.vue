<template>
	<div class="h-max">
		<n-space :vertical="true">
			<n-space>
				<n-form :inline="!isMobile" :label-width="100" label-placement="left">
					<n-form-item label="キーワード">
						<n-input v-model:value="searchParams.keyword" style="min-width: 30%" type="text" placeholder="スタッフ番号/名前"
							clearable />
					</n-form-item>
					<n-form-item label="日記時間" path="createStartTime">
						<n-date-picker v-model:value="searchParams.selTime" type="daterange" clearable @update:value="onUpdate" />
					</n-form-item>
					<n-form-item>
						<n-button type="primary" @click="() => {
								searchQuery();
							}
							">検索</n-button>
						<n-button class="ml-2" @click="searchReset">リセット</n-button>
					</n-form-item>
				</n-form>
			</n-space>
			<loading-empty-wrapper :style="{ height: hightRef + 'px' }" :loading="loading" :empty="empty">
				<n-data-table remote bordered :columns="columns" :data="dataSource" :pagination="pagination" :scroll-x="4000"
					:single-line="false" :flex-height="true" :style="{ height: hightRef + 'px' }" @update:page="handlePageChange"
					@update:page-size="handleUpdatePageSize" />
			</loading-empty-wrapper>
		</n-space>
		<add-or-edit ref="aoeModal" @close="searchQuery" />
		<del-action ref="delModal" @close="searchQuery" />
	</div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { DataTableColumn, NButton, NTag } from 'naive-ui';
import { EnumUserRole } from '@/enum';
import { useMyOptions, useMyTags, useMyCommon } from '@/composables';
import { useDataTable } from '@/hooks';
import { AddOrEdit, DelAction } from './components';

const module = 'user/log';
const moduleParams: MySearch.UserSearchParams = {
	keyword: '',
	userRole: '',
	selTime: [Date.now(), Date.now()],
	beginTime: Date.now(),
	endTime: Date.now()
};

const {
	delModal,
	aoeModal,
	handleAdd,
	handleEdit,
	handleAudit,
	handleDelete,
	searchQuery,
	searchReset,
	handlePageChange,
	handleUpdatePageSize,
	resetParams,
	searchParams,
	pagination,
	dataSource,
	loading,
	empty
} = useDataTable<MyModel.User>(module, moduleParams);
resetParams();

const { userRoleOptions } = useMyOptions();
const { userRoleTagType } = useMyTags();
const { isMobile } = useMyCommon();

const columns: DataTableColumn<MyModel.Log>[] = [
	{
		title: '日付',
		key: 'date',
		width: 100,
		align: 'center'
	},
	{
		title: 'ドライバー',
		key: 'driver1Name',
		width: 100,
		align: 'center'
	},
	{
		title: '車両',
		key: 'carNo',
		width: 100,
		align: 'center'
	},
	{
		title: '開始前距離',
		key: 'startDistance',
		width: 100,
		align: 'center'
	},
	{
		title: '終了距離',
		key: 'endDistance',
		width: 100,
		align: 'center'
	},
	{
		title: '差引距離',
		key: 'diffDistance',
		width: 100,
		align: 'center'
	},
	{
		title: '実車距離',
		key: 'jisshaDistance',
		width: 100,
		align: 'center'
	},
	{
		title: '回送距離',
		key: 'kaisouDistance',
		width: 100,
		align: 'center'
	},
	{
		title: '給油量',
		key: 'refueling',
		width: 100,
		align: 'center'
	},
	{
		title: '運転席点検',
		key: 'a',
		width: 100,
		align: 'center',
		render(row) {
			let t = '❌';
			if (row.a1 && row.a2 && row.a3 && row.a4 && row.a5 && row.a6 && row.a7) {
				t = '✅';
			}
			return t;
		}
	},
	{
		title: 'ルーム点検・エンジン',
		key: 'b',
		width: 100,
		align: 'center',
		render(row) {
			let t = '❌';
			if (row.b1 && row.b2 && row.b3 && row.b4 && row.b5 && row.b6) {
				t = '✅';
			}
			return t;
		}
	},
	{
		title: '車の周りの点検',
		key: 'b',
		width: 100,
		align: 'center',
		render(row) {
			let t = '❌';
			if (row.c1 && row.c2 && row.c3 && row.c4 && row.c5 && row.c6 && row.c7 && row.c8) {
				t = '✅';
			}
			return t;
		}
	},
	{
		title: 'その他',
		key: 'b',
		width: 100,
		align: 'center',
		render(row) {
			let t = '❌';
			if (row.d1 && row.d2) {
				t = '✅';
			}
			return t;
		}
	},
	// {
	//   title: '運転席',
	//   key: 'userRole',
	//   align: 'left',
	//   render(row) {
	//     return [
	//       h(
	//         'div',
	//         { class: 'flex flex-row items-center' },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `ブレーキペルタ ${row.a1 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `駐車ブレーキ・レバー ${row.a2 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `エンジン ${row.a3 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `ウィンド・ウォッシャー ${row.a4 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `ワイパー ${row.a5 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `室内 ${row.a6 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `ドアロック・座席ベルト ${row.a7 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       )
	//     ];
	//   }
	// },
	// {
	//   title: 'エンジンルーム',
	//   key: 'userRole',
	//   align: 'left',
	//   render(row) {
	//     return [
	//       h(
	//         'div',
	//         { class: 'flex flex-row items-center' },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `ウィンド・ウォッシャー ${row.b1 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `ブルーキリザーブ・タンク ${row.b2 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `バッテリー ${row.b3 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `ラジエーター ${row.b4 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `エンジンオイル ${row.b5 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `ファンベルト ${row.b6 ? '✅' : '❌'}` })]
	//         }
	//       )
	//     ];
	//   }
	// },
	// {
	//   title: '車の周り',
	//   key: 'userRole',
	//   align: 'left',
	//   render(row) {
	//     return [
	//       h(
	//         'div',
	//         { class: 'flex flex-row items-center' },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `タイヤ空気圧 ${row.c1 ? '✅' : '❌'}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `タイヤ亀裂、損傷 ${row.c2 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [
	//             h('span', { class: 'ml-4px' }, { default: () => `タイヤ異常な摩耗 ${row.c3 ? '✅' : '❌'}` })
	//           ]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `タイヤ異常な摩耗 ${row.c4}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点灯具合 ${row.c5}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `点滅具合 ${row.c6}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `汚れ、損傷 ${row.c7}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `ディスク・ホイールの取付 ${row.c8}` })]
	//         }
	//       )
	//     ];
	//   }
	// },
	// {
	//   title: 'その他',
	//   key: 'userRole',
	//   align: 'left',
	//   render(row) {
	//     return [
	//       h(
	//         'div',
	//         { class: 'flex flex-row items-center' },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `非常信号計・工具 ${row.d1}` })]
	//         }
	//       ),
	//       h(
	//         'div',
	//         {
	//           class: 'flex flex-row items-center'
	//         },
	//         {
	//           default: () => [h('span', { class: 'ml-4px' }, { default: () => `車検証・保険証 ${row.d2}` })]
	//         }
	//       )
	//     ];
	//   }
	// },
	{
		title: '操作',
		key: 'actions',
		width: 80,
		align: 'center',
		render(row) {
			const delOption = h(
				NButton,
				{
					quaternary: true,
					size: 'small',
					type: 'error',
					onClick: () => handleDelete('仕事日記', row)
				},
				{ default: () => '削除' }
			);
			const options: any = [];
			options.push(delOption);
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
const data = [
	{
		date: '2023-9-18',
		driverNo: '0001',
		driverName: '鈴木',
		carNo: '5532',
		carName: '7座海狮',
		log: '9:00-12:30 车库',
		a1: '✅',
		a2: '✅',
		a3: '✅',
		a4: '✅',
		a5: '✅',
		a6: '✅',
		a7: '✅'
	},
	{
		date: '2023-9-18',
		driverNo: '0002',
		driverName: '原村',
		carNo: '8877',
		carName: '14座海狮',
		log: '9:00-12:30 车库',
		a1: '✅',
		a2: '✅',
		a3: '✅',
		a4: '✅',
		a5: '✅',
		a6: '✅',
		a7: '✅'
	},
	{
		date: '2024-01-18',
		driverNo: '0002',
		driverName: '原村',
		carNo: '8877',
		carName: '14座海狮',
		log: '9:00-12:30 车库',
		a1: '✅',
		a2: '✅',
		a3: '✅',
		a4: '✅',
		a5: '✅',
		a6: '✅',
		a7: '✅'
	},
	{
		date: '2024-01-22',
		driverNo: '0002',
		driverName: '原村',
		carNo: '8877',
		carName: '14座海狮',
		log: '9:00-12:30 车库',
		a1: '✅',
		a2: '✅',
		a3: '✅',
		a4: '✅',
		a5: '✅',
		a6: '✅',
		a7: '✅'
	}
];

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	searchParams.beginTime = value ? value[0] : undefined;
	searchParams.endTime = value ? value[1] : undefined;
};

const hightRef = ref();

onMounted(() => {
	hightRef.value = document.documentElement.offsetHeight - 240;

	searchQuery();
});
</script>
<style scoped></style>
