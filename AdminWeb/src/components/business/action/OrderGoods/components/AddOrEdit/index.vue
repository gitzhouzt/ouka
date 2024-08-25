<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-spin :show="loading">
				<n-form ref="formRef" label-placement="left" :label-width="100" :model="formValue" :rules="rules" :size="size">
					<n-grid :cols="2">
						<n-gi>
							<n-form-item label="備品タイプ" path="goodsType">
								<n-input v-model:value="formValue.goodsType" placeholder="クリック備品タイプを選択" readonly
									@click="showDict('order_goods_type')" />
							</n-form-item>
						</n-gi>
						<n-gi>
							<n-form-item label="件数" path="amount">
								<n-select v-model:value="formValue.amount" :options="options" /></n-form-item>
						</n-gi>
						<n-gi :span="2">
							<n-form-item label="備考" path="remark">
								<n-input v-model:value="formValue.remark" />
							</n-form-item> </n-gi></n-grid>
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
import { PropType, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const props = defineProps({
	model: {
		type: Object as PropType<MyModel.Order | null>,
		default: () => {
			return {};
		}
	}
});
const bodyStyleRef = ref({
	width: '800px'
});

const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
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
	addOrEdit: `/order/goods/addOrEdit`
};
const showModalRef = ref<boolean | undefined>(false);
const options: any = [];
const showModal = async (row: MyModel.OrderGoods | undefined) => {
	showModalRef.value = true;
	for (let index = 0; index < 10;) {
		const element = {
			label: index + 1,
			value: index + 1
		};
		index += 1;
		options.push(element);
	}
	if (row) {
		formValue.value.id = row?.id;
		formValue.value.orderId = row?.orderId;
		formValue.value.orderNo = row?.orderNo;
		formValue.value.amount = row?.amount ?? 0;
		formValue.value.goodsType = row?.goodsType;
		formValue.value.goodsTypeCode = row?.goodsTypeCode;
		formValue.value.remark = row?.remark;
	} else {
		formValue.value = {
			id: '',
			orderId: props.model?.id ?? '',
			orderNo: props.model?.orderNo ?? '',
			goodsType: '',
			goodsTypeCode: '',
			amount: 0,
			remark: ''
		};
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

const loading = ref<boolean>(false);
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
						close();
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
