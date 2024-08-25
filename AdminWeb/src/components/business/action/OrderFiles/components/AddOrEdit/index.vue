<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-spin :show="loading">
				<n-form ref="formRef" label-placement="left" :label-width="120" :model="formValue" :rules="rules" :size="size">
					<n-grid :cols="2">
						<n-gi>
							<n-form-item label="ファイル名" path="fileName">
								<n-input v-model:value="formValue.fileName" /> </n-form-item></n-gi>
						<n-gi>
							<n-form-item label="シェア" path="share">
								<n-switch v-model:value="formValue.share">
									<template #checked> 公開 </template>
									<template #unchecked> 非公開 </template>
								</n-switch>
							</n-form-item></n-gi>
						<n-gi :span="2">
							<n-form-item label="ファイル" path="fileUrl">
								<n-space>
									<file-upload ref="fileUploadRef" list-type="text" file-key="files/order" :max="1" :size="20971520"
										@finish="onFinish" />
								</n-space> </n-form-item></n-gi><n-gi :span="2">
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
const fileUploadRef = ref<any>();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
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
	},
	fileUrl: {
		required: true,
		message: 'アップロードしてください'
	}
};
const urls = {
	addOrEdit: `/order/file/addOrEdit`
};
const showModalRef = ref<boolean | undefined>(false);
const options: any = [];
const showModal = async (row: MyModel.OrderFile | undefined) => {
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
		formValue.value.fileName = row?.fileName;
		formValue.value.fileUrl = row?.fileUrl;
		formValue.value.share = row?.share;
		formValue.value.remark = row?.remark;
	} else {
		formValue.value = {
			id: '',
			orderId: props.model?.id ?? '',
			orderNo: props.model?.orderNo ?? '',
			fileName: '',
			fileUrl: '',
			share: true,
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
const onFinish = (files: any) => {
	if (!files || files.length <= 0) {
		formValue.value.fileUrl = '';
		return;
	}
	formValue.value.fileUrl = files[0].url;
};

const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
	e.preventDefault();
	formRef.value?.validate(errors => {
		if (!errors) {
			// if (!formValue.value.fileUrl) {
			// 	message.warning('ファイルをアップロードしてください');
			// 	return;
			// }
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

defineExpose({
	showModal,
	setTitle
});
</script>

<style scoped></style>
