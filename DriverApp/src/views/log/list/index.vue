<template>
	<div>
		<div style="margin: 10px">
			<van-button v-if="false" color="#fb8c00" size="large" block @click="handleExport">ダウンロード</van-button>
		</div>
		<van-search v-if="false" v-model="searchParams.keyword" :clearable="false" placeholder="キーワード：番号" show-action
			shape="round">
			<template #action>
				<div @click="searchQuery">検索</div>
			</template>
		</van-search>
		<van-pull-refresh v-model="refreshing" class="min-h-full" @refresh="handleRefresh">
			<template v-if="!empty">
				<van-list v-model:loading="loading" offset="100" :finished="finished" finished-text="データなし"
					@load="handlePageChange">
					<div v-for="item in dataSource" :key="item.id"
						class="bg-white rounded-1 m-2 p-2 flex justify-between items-center" @click="onClick(item.id)">
						<div class="mt-4px text-16px">
							<div class="mt-4px">
								<span class="font-bold">日付：</span>
								<span class="ml-2">{{ item.date }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">車両：</span>
								<span class="ml-2"> {{ item.carNo }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">運転士(正)：</span>
								<span class="ml-2">{{ item.driver1Name }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">運転士(副)：</span>
								<span class="ml-2">{{ item.driver2Name ?? '-' }}</span>
							</div>
							<div class="mt-4px">
								<span class="font-bold">整備管理者：</span>
								<span class="ml-2">{{ item.manager ?? '-' }}</span>
							</div>
						</div>
					</div>
				</van-list>
			</template>
			<template v-else>
				<van-empty image="search" description="データなし" />
			</template>
		</van-pull-refresh>
	</div>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue';
import { showNotify } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { useDataTable } from '@/hooks/common';
import { getEnvConfig } from '~/.env-config';

const envConfig = getEnvConfig(import.meta.env);

const module = 'driver/user/log';
const moduleParams: MySearch.SearchParams = {
	keyword: '',
};

const {
	searchQuery,
	resetParams,
	handleExport,
	handleRefresh,
	handlePageChange,
	searchParams,
	empty,
	dataSource,
	loading,
	finished,
	refreshing,
} = useDataTable<MyModel.Log>(module, moduleParams);
resetParams();
const { routerPush } = useRouterPush();

const showTips = (message: string) => {
	showNotify({ type: 'primary', message, position: 'bottom' });
};
const onClick = (id: string) => {
	routerPush({
		path: '/log/details',
		query: { id },
	});
};
onMounted(() => {
	searchQuery();
});
</script>

<style lang="scss"></style>
