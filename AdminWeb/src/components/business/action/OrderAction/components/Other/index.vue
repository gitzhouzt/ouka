<template>
	<div>
		<n-spin :show="loading">
			<n-form ref="formRef" label-placement="left" :label-width="140" :model="formValue" :rules="rules" :size="size">
				<n-grid :cols="3">
					<n-gi>
						<n-form-item label=" 料金記録">
							<n-button type="primary" @click="handlePay('料金記録')">追加</n-button>
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="車備品">
							<n-button type="primary" @click="handleGoods('車備品')">追加</n-button>
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="添付資料">
							<n-button type="primary" @click="handleFiles('添付資料')">明細</n-button>
						</n-form-item>
					</n-gi>
					<n-gi :span="3">
						<n-form-item label="お客様要望" path="customerRemark">
							<n-input v-model:value="formValue.customerRemark" type="textarea" :autosize="{
								minRows: 3,
								maxRows: 3
							}" placeholder="お客様の要望を入力してください" /></n-form-item></n-gi>
					<n-gi :span="3">
						<n-form-item label="備考" path="companyRemark">
							<n-input v-model:value="formValue.companyRemark" type="textarea" :autosize="{
								minRows: 3,
								maxRows: 3
							}" placeholder="備考を入力してください" /></n-form-item></n-gi>
				</n-grid>
			</n-form>
			<n-divider />
			<n-space justify="center">
				<n-button @click="prev">前へ</n-button>
				<n-button type="primary" @click="next">次へ</n-button>
			</n-space>
		</n-spin>
		<order-pay ref="payModal" />
		<order-files ref="filesModal" />
		<order-goods ref="goodsModal" />
	</div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['prev', 'next', 'close']);
const props = defineProps({
	model: {
		type: Object as PropType<MyModel.Order | null>,
		default: () => {
			return {};
		}
	}
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const formValue = ref<MyModel.Order>({
	id: props.model?.id ?? '',
	customerRemark: props.model?.customerRemark,
	companyRemark: props.model?.companyRemark
});

const rules = {
	customerRemark: {
		required: false,
		trigger: 'input',
		message: '2000文字まで入力してください',
		max: 2000
	},
	companyRemark: {
		required: false,
		trigger: 'input',
		message: '2000文字まで入力してください',
		max: 2000
	}
};

/** ************************ Save Form ************************ */

const urls = {
	setOther: `/order/setOther`
};

const save = () => {
	const params: MyModel.Order = {
		id: formValue.value.id,
		customerRemark: formValue.value?.customerRemark,
		companyRemark: formValue.value?.companyRemark
	};
	const promise = request.post<MyModel.Order>(`${urls.setOther}`, params);
	loadingBar.start();
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				emits('next', { key: 'other', params: res.data });
			}
		})
		.catch(err => {
			message.warning(err);
		})
		.finally(() => {
			loadingBar.finish();
		});
};

/** ************************ modal ************************ */

const payModal = ref<any>(null);
const handlePay = (title: string) => {
	payModal.value.setTitle(`${title} - ${props.model?.orderNo}`);
	payModal.value.showModal(props.model);
};

const filesModal = ref<any>(null);
const handleFiles = (title: string) => {
	filesModal.value.setTitle(`${title} - ${props.model?.orderNo}`);
	filesModal.value.showModal(props.model);
};

const goodsModal = ref<any>(null);
const handleGoods = (title: string) => {
	goodsModal.value.setTitle(`${title} - ${props.model?.orderNo}`);
	goodsModal.value.showModal(props.model);
};

const prev = () => {
	emits('prev', { key: 'other', params: formValue.value });
};
const next = (e: MouseEvent) => {
	e.preventDefault();
	formRef.value?.validate(errors => {
		if (!errors) {
			save();
		}
	});
};

onMounted(() => {
	loading.value = true;
	setTimeout(() => {
		loading.value = false;
	}, 300);
});
</script>

<style scoped></style>
