<template>
	<div>
		<n-spin :show="loading">
			<n-form ref="formRef" label-placement="left" :label-width="100" :model="formValue" :rules="rules" :size="size">
				<n-grid :cols="2">
					<n-gi :span="2">
						<n-form-item label="事故車両" path="carName">
							<n-input-group>
								<n-input v-model:value="formValue.carName" readonly placeholder="クリック車両を選択" :disabled="!!formValue.id"
									@click="showCar()"></n-input>
								<n-input-group-label>番号</n-input-group-label>
								<n-input v-model:value="formValue.carNo" :disabled="true" placeholder="車両を選択して表示" />
							</n-input-group>
						</n-form-item>
					</n-gi>
					<n-gi :span="2">
						<n-form-item label="事故者" path="driverName">
							<n-input-group>
								<n-input v-model:value="formValue.driverName" readonly placeholder="クリックドライバーを選択"
									:disabled="!!formValue.id" @click="showDriver()"></n-input>
								<n-input-group-label>番号</n-input-group-label>
								<n-input v-model:value="formValue.driverNo" :disabled="true" placeholder="ドライバーを選択して表示" />
							</n-input-group>
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="事故原因" path="accidentType">
							<n-input v-model:value="formValue.accidentType" placeholder="クリック分類を選択" readonly
								@click="showDict('operate_accident_type')" /> </n-form-item></n-gi>
					<n-gi>
						<n-form-item label="事故日時" path="accidentTime" required>
							<n-date-picker v-model:value="formValue.accidentTime" type="datetime" clearable /></n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="責任側" path="responsible">
							<n-input v-model:value="formValue.responsible" /></n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="事故比例" path="proportion">
							<n-input v-model:value="formValue.proportion" placeholder="クリック比例を選択" readonly
								@click="showDict('operate_accident_proportion')" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="キズ確認者" path="confirmBy">
							<n-input v-model:value="formValue.confirmBy" /></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="事故通知日時" path="noticeTime">
							<n-date-picker v-model:value="formValue.noticeTime" type="datetime" clearable /></n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="修理業者" path="repairBy"> <n-input
								v-model:value="formValue.repairBy" /></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="修理終了日時" path="repairTime">
							<n-date-picker v-model:value="formValue.repairTime" type="datetime" clearable /></n-form-item>
					</n-gi>
					<n-gi :span="2">
						<n-form-item label="事故詳細" path="details"> <n-input v-model:value="formValue.details" /></n-form-item></n-gi>
					<n-gi :span="2">
						<n-form-item label="事故写真" path="images">
							<n-space>
								<file-upload ref="fileUploadRef" accept="image/*" list-type="image-card" file-key="images/accident"
									:file-list-style="{
										width: '600px'
									}" :max="5" :size="1048576" @finish="onFinish" />
							</n-space>
						</n-form-item>
					</n-gi>
					<n-gi :span="2">
						<n-form-item label="備考" path="remark">
							<n-input-group>
								<n-input v-model:value="formValue.remark" placeholder="備考" />
							</n-input-group> </n-form-item></n-gi> </n-grid></n-form>
			<n-divider />
			<n-space justify="center">
				<n-button type="primary" @click="next">次へ</n-button>
			</n-space>
		</n-spin>
		<car-select-modal ref="carModal" @click="selectCar" />
		<driver-select-modal ref="driverModal" @click="selectDriver" />
		<dict-select-modal ref="dictModal" @click="selectDict" />
	</div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['next', 'close']);
const props = defineProps({
	model: {
		type: Object as PropType<MyModel.Accident | null>,
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
const formValue = ref<MyModel.Accident>({
	id: props.model?.id ?? '',
	carId: props.model?.carId,
	carNo: props.model?.carNo,
	carName: props.model?.carName,
	driverId: props.model?.driverId,
	driverName: props.model?.driverName,
	driverNo: props.model?.driverNo,

	accidentType: props.model?.accidentType,
	accidentTypeCode: props.model?.accidentTypeCode,
	details: props.model?.details,
	confirmBy: props.model?.confirmBy,
	accidentTime: new Date(props.model?.accidentTime ?? new Date()).getTime(),
	images: props.model?.images,

	repairTime: new Date(props.model?.repairTime ?? new Date()).getTime(),
	repairBy: props.model?.repairBy,
	noticeTime: new Date(props.model?.noticeTime ?? new Date()).getTime(),

	proportion: props.model?.proportion,
	responsible: props.model?.responsible,

	remark: props.model?.remark
});

const rules = {
	carName: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	driverName: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	accidentType: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	details: {
		required: true,
		trigger: 'input',
		message: '1000文字まで入力してください'
	}
};

const driverModal = ref<any>(null);
const showDriver = () => {
	driverModal.value?.showModal();
};
const selectDriver = (result: any) => {
	formValue.value.driverId = result.id;
	formValue.value.driverName = result.userName;
	formValue.value.driverNo = result.userNo;
};

const carModal = ref<any>(null);
const showCar = () => {
	carModal.value?.showModal();
};
const selectCar = (result: any) => {
	formValue.value.carId = result.value;
	formValue.value.carName = result.text;
	formValue.value.carNo = result.carNo;
};

/** ************************ Save Form ************************ */

const urls = {
	setAccident: `operate/accident/setAccident`
};

const save = () => {
	const params: MyModel.Accident = {
		id: formValue.value.id,
		carId: formValue.value.carId,
		carNo: formValue.value.carNo,
		carName: formValue.value.carName,
		driverId: formValue.value.driverId,
		driverNo: formValue.value.driverNo,
		driverName: formValue.value.driverName,

		accidentType: formValue.value.accidentType,
		accidentTypeCode: formValue.value.accidentTypeCode,
		details: formValue.value.details,
		confirmBy: formValue.value.confirmBy,
		accidentTime: formValue.value.accidentTime,
		images: formValue.value.images,

		repairTime: formValue.value.repairTime,
		repairBy: formValue.value.repairBy,
		noticeTime: formValue.value.noticeTime,

		proportion: formValue.value?.proportion,
		responsible: formValue.value?.responsible,

		// status: formValue.value.status,
		remark: formValue.value.remark,

		insuranceAmount: props.model?.insuranceAmount ?? 0,
		driverAmount: props.model?.driverAmount ?? 0,
		amount: props.model?.insuranceAmount ?? 0,
		companyAmount: props.model?.driverAmount ?? 0,
		financeAmount: props.model?.financeAmount ?? 0,

		financeNoticeTime: new Date(props.model?.financeNoticeTime ?? new Date()).getTime()
	};
	const promise = request.post<MyModel.Accident>(`${urls.setAccident}`, params);
	loadingBar.start();
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				emits('next', { key: 'accident', params: res.data });
			}
		})
		.catch(err => {
			message.warning(err);
		})
		.finally(() => {
			loadingBar.finish();
		});
};
const next = (e: MouseEvent) => {
	e.preventDefault();
	formRef.value?.validate(errors => {
		if (!errors) {
			save();
		}
	});
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

/** ************************ Save Form ************************ */

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
	dictModal.value?.showModal();
	dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
	switch (result.type) {
		case 'operate_accident_type':
			formValue.value.accidentType = result.text;
			formValue.value.accidentTypeCode = result.value;
			break;
		case 'operate_accident_proportion':
			formValue.value.proportion = result.text;
			break;
		default:
			break;
	}
};

onMounted(() => {
	loading.value = true;
	setTimeout(() => {
		renderFileUpload();
		loading.value = false;
	}, 200);
});
</script>

<style scoped></style>
