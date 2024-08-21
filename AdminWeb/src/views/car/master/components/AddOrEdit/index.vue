<template>
	<div>
		<n-modal v-model:show="showModalRef" :style="bodyStyleRef" transform-origin="center" :mask-closable="false"
			preset="card" :title="titleRef" closable @update-show="onUpdateShow">
			<n-card size="huge" role="dialog" aria-modal="true">
				<n-spin :show="loading">
					<n-form ref="formRef" label-placement="left" :label-width="80" :model="formValue" :rules="rules" :size="size">
						<n-form-item label="番号" path="carNo">
							<n-input v-model:value="formValue.carNo" :disabled="disabled" placeholder="入力しない場合は自動的に作成" />
						</n-form-item>
						<n-form-item label="車名" path="carName">
							<n-input v-model:value="formValue.carName" />
						</n-form-item>
						<n-form-item label="タイプ" path="carType">
							<n-input v-model:value="formValue.carType" placeholder="クリックタイプを選択" readonly
								@click="showDict('car_type')" />
						</n-form-item>
						<n-form-item label="写真" path="images">
							<n-space>
								<file-upload ref="fileUploadRef" accept="image/*" list-type="image-card" file-key="images/car" :max="10"
									:size="1048576" @finish="onFinish" />
							</n-space>
						</n-form-item>
						<n-form-item label="ナンバープレート" path="plateNum">
							<n-input v-model:value="formValue.plateNum" />
						</n-form-item>
						<n-form-item label="駐車場" path="carPark">
							<n-input v-model:value="formValue.carPark" placeholder="クリック駐車場を選択" readonly
								@click="showDict('car_park')" />
						</n-form-item>
						<n-form-item label="駐車料金" path="parkingFee" :show-button="false">
							<n-input-number v-model:value="formValue.parkingFee" :min="0" :show-button="false" />
						</n-form-item>
						<n-form-item label="座位数" path="carSeat" :show-button="false">
							<n-input-number v-model:value="formValue.carSeat" :min="5" :max="100" :show-button="false" />
						</n-form-item>
						<n-form-item label="備考" path="remark">
							<n-input-group>
								<n-input v-model:value="formValue.remark" placeholder="備考" />
							</n-input-group>
						</n-form-item>
					</n-form>
					<n-divider />
					<n-space justify="center">
						<n-button type="primary" ghost @click="close">Cancel</n-button>
						<n-button type="primary" @click="handleValidateClick">保存</n-button>
					</n-space>
				</n-spin>
			</n-card>
			<dict-select-modal ref="dictModal" @click="selectDict" />
		</n-modal>
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
	width: '800px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.Car>({
	id: '',
	carNo: '',
	carName: '',
	carType: '',
	carSeat: 7,
	plateNum: '',
	carPark: '',
	parkingFee: 0,
	images: '',
	remark: ''
});
const rules = {
	plateNum: {
		required: true,
		message: '入力してください',
		trigger: 'input',
		max: 20
	},
	carPark: {
		required: true,
		message: '駐車場を選択してください',
		trigger: 'change'
	},
	carType: {
		required: true,
		message: 'タイプを選択してください',
		trigger: 'change'
	}
};

const disabled = ref(false);
const showModalRef = ref<boolean | undefined>(false);
const showModal = (row: MyModel.Car | undefined) => {
	showModalRef.value = true;
	if (row) {
		formValue.value.id = row?.id;
		formValue.value.carNo = row?.carNo;
		formValue.value.carName = row?.carName;
		formValue.value.carType = row?.carType;
		formValue.value.carSeat = row?.carSeat;

		formValue.value.plateNum = row?.plateNum;
		formValue.value.images = row?.images;
		formValue.value.carPark = row?.carPark;
		formValue.value.parkingFee = row?.parkingFee;
		formValue.value.remark = row?.remark;
		disabled.value = true;
		if (formValue.value.images) {
			renderFileUpload();
		}
	} else {
		formValue.value.id = '';
		formValue.value.carNo = '';
		formValue.value.carName = '';
		formValue.value.carType = '';
		formValue.value.carSeat = 7;

		formValue.value.plateNum = '';
		formValue.value.images = '';
		formValue.value.carPark = '';
		formValue.value.parkingFee = 0;
		formValue.value.remark = '';
		disabled.value = false;
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
const envConfig = getEnvConfig(import.meta.env);
const fileUploadRef = ref<any>();
function renderFileUpload() {
	const uploadFiles: any[] = [];
	const { images } = formValue.value;
	if (images) {
		const array = images.split(',');

		array.forEach((f, index) => {
			const file = {
				uid: f + index,
				name: f,
				status: 'finished',
				thumbnailUrl: `${envConfig.static}${f}`,
				url: `${envConfig.static}${f}`,
				fileUrl: f
			};
			uploadFiles.push(file);
		});
		console.debug(uploadFiles);

		fileUploadRef.value.initFiles(uploadFiles);
	}
}
const onFinish = (files: any) => {
	if (!files || files.length <= 0) {
		formValue.value.images = '';
		return;
	}
	formValue.value.images = files
		.map((f: any) => {
			return f.url.replace(envConfig.static, '');
		})
		.join(',');
};

const urls = {
	addOrEdit: `car/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
	e.preventDefault();
	formRef.value?.validate(errors => {
		if (!errors) {
			const params: MyModel.Car = {
				id: formValue.value.id,
				carNo: formValue.value.carNo,
				carName: formValue.value.carName,
				carType: formValue.value.carType,
				carSeat: formValue.value.carSeat,
				plateNum: formValue.value.plateNum,
				images: formValue.value.images,
				carPark: formValue.value.carPark,
				parkingFee: formValue.value.parkingFee,
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
		case 'car_type':
			formValue.value.carType = result.text;
			break;
		case 'car_park':
			formValue.value.carPark = result.text;
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
