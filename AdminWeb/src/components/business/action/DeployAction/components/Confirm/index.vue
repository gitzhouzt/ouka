<template>
	<div>
		<n-spin :show="loading">
			<n-result status="info" size="small" description="以下の内容を保存しました。">
				<template #footer>
					<deploy-details :model="model" :deploy-model="deployModel" />
					<n-space justify="center" class="pt-20px">
						<n-button type="default" @click="prev">前へ</n-button>
						<n-button type="primary" @click="next">完了</n-button>
					</n-space>
				</template>
			</n-result>
		</n-spin>
	</div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['next', 'prev']);
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
const loading = ref<boolean>(false);
const message = useMessage();
const loadingBar = useLoadingBar();

const urls = {
	deploy: `/order/deploy`
};
const save = () => {
	const promise = request.post<Boolean>(`${urls.deploy}`, props.deployModel);
	loadingBar.start();
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				emits('next', { key: 'confirm' });
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
	save();
};

const prev = () => {
	emits('prev', { key: 'confirm', params: props.deployModel });
};

onMounted(() => {
	loading.value = true;
	setTimeout(() => {
		loading.value = false;
	}, 200);
});
</script>

<style scoped></style>
