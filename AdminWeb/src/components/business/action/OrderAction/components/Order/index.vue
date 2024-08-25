<template>
	<div>
		<n-spin :show="loading">
			<n-form ref="formRef" label-placement="left" :label-width="140" :model="formValue" :rules="rules" :size="size">
				<n-grid :cols="2">
					<n-gi>
						<n-form-item label="出発地" path="orderFrom">
							<n-input v-model:value="formValue.orderFrom" placeholder="○○空港" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="出発地詳細" path="orderFromDetails">
							<n-input v-model:value="formValue.orderFromDetails" placeholder="東京都○○区○○○○" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="目的地" path="orderTo">
							<n-input v-model:value="formValue.orderTo" placeholder="東京都○○区○○○○" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="目的地詳細" path="orderToDetails">
							<n-input v-model:value="formValue.orderToDetails" placeholder="東京都○○区○○○○" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="注文番号&nbsp;&nbsp;" path="orderNo">
							<n-input v-model:value="formValue.orderNo" placeholder="入力なしの場合、自動発行(選択した订单来源＋7桁自動発行 例：Web_0000001)" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="運行内容" path="orderType">
							<n-select v-model:value="formValue.orderType" :options="orderTypeOptions" :consistent-menu-width="false"
								@update:value="handleUpdateValue" /> </n-form-item></n-gi>
					<n-gi>
						<n-form-item label="旅行日数" path="orderDays">
							<n-input-number v-model:value="formValue.orderDays" :precision="2" :step="0.25" :min="0"
								:show-button="false" :disabled="EnumOrderType[formValue.orderType ?? ''] !== EnumOrderType.Haiya &&
									EnumOrderType[formValue.orderType ?? ''] !== EnumOrderType.LongHaul
									" @update:value="onUpdate">
								<template #suffix> 日 </template>
							</n-input-number></n-form-item></n-gi>

					<n-gi>
						<n-form-item label="出発日" path="startTime" required>
							<n-date-picker v-model:value="formValue.startTime" type="datetime" clearable
								@update:value="onUpdate" /></n-form-item></n-gi>
					<!--<n-gi v-if="EnumOrderType[formValue.orderType ?? ''] === EnumOrderType.Haiya">-->
					<n-gi>
						<n-form-item label="終了日" path="endTime" required>
							<n-date-picker v-model:value="formValue.endTime" type="datetime" clearable /> </n-form-item></n-gi>

					<template v-if="
						EnumOrderType[formValue.orderType ?? ''] === EnumOrderType.Airport_S ||
						EnumOrderType[formValue.orderType ?? ''] === EnumOrderType.Airport_Y
					">
						<n-gi>
							<n-form-item label="空港" path="airportCode">
								<n-select v-model:value="formValue.airportCode" :options="airportOptions" :consistent-menu-width="false"
									@update:value="airportUpdateValue" />
							</n-form-item>
						</n-gi>
						<n-gi>
							<n-form-item label="ターミナル" path="terminal">
								<n-select v-model:value="formValue.terminal" :options="terminalOptions"
									:consistent-menu-width="false" />
							</n-form-item>
						</n-gi>
						<n-gi>
							<n-form-item label="航空便" path="flightNo">
								<n-input v-model:value="formValue.flightNo" placeholder="E99999" /></n-form-item></n-gi>
					</template>

					<n-gi>
						<n-form-item label="料金通貨" path="currency">
							<n-input v-model:value="formValue.currency" placeholder="クリック通貨を選択" readonly
								@click="showDict('pay_currency')" />
						</n-form-item></n-gi><n-gi>
						<n-form-item label="基本金額" path="currencyAmount">
							<n-input-number v-model:value="formValue.currencyAmount" :min="0" :precision="0" :show-button="false">
								<template #prefix> {{ formValue.currencyCode === 'usd' ? '$' : '￥' }} </template>
							</n-input-number>
						</n-form-item></n-gi>
					<n-gi>
						<n-form-item label="车费类型" path="feeType">
							<n-radio-group v-model:value="formValue.feeType" path="feeType" name="feeTypeRdo">
								<n-radio-button key="全包" value="全包" label="全包" />
								<n-radio-button key="高停别" value="高停别" label="高停别" /> </n-radio-group></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="時間超過料" path="isOutTimeCash">
							<n-radio-group v-model:value="formValue.isOutTimeCash" name="isOutTimeCashRdo">
								<n-radio-button key="true" value="true" label="是" />
								<n-radio-button key="false" value="false" label="否" /></n-radio-group></n-form-item></n-gi>
					<n-gi v-if="formValue.isOutTimeCash === 'true'">
						<n-form-item label="超過料金" path="outTimeAmount">
							<n-input-number v-model:value="formValue.outTimeAmount" :precision="0" :min="0" :show-button="false">
								<template #suffix> 円/30分 </template>
							</n-input-number></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="荷物数" path="luggageNum">
							<n-input-number v-model:value="formValue.luggageNum" :precision="0" :min="0" :show-button="false">
								<template #suffix> 件 </template>
							</n-input-number></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="大人人数" path="adultNum">
							<n-input-number v-model:value="formValue.adultNum" :precision="0" :min="0" :show-button="false">
								<template #suffix> 名 </template>
							</n-input-number></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="子供人数" path="childrenNum">
							<n-input-number v-model:value="formValue.childrenNum" :precision="0" :min="0" :show-button="false">
								<template #suffix> 名 </template>
							</n-input-number></n-form-item></n-gi>
					<n-gi>
						<n-form-item label="希望車両タイプ" path="specifyCarType">
							<n-input v-model:value="formValue.specifyCarType" placeholder="クリックタイプを選択" readonly
								@click="showDict('car_type')" />
						</n-form-item>
					</n-gi>
					<n-gi>
						<n-form-item label="車両支配" path="carName">
							<n-input-group>
								<n-input v-model:value="formValue.carName" readonly placeholder="クリック車両を選択"
									@click="showCar()"></n-input>
							</n-input-group> </n-form-item></n-gi>
					<n-gi>
						<n-form-item label="ドライバー支配" path="driverName">
							<n-input-group>
								<n-input v-model:value="formValue.driverName" readonly placeholder="クリックドライバーを選択"
									@click="showDriver()"></n-input>
							</n-input-group> </n-form-item></n-gi>
				</n-grid>
				<dict-select-modal ref="dictModal" @click="selectDict" />
				<car-select-modal ref="carModal" @click="selectCar" />
				<driver-select-modal ref="driverModal" @click="selectDriver" />
			</n-form>
			<n-divider />
			<n-space justify="center">
				<n-button @click="prev">前へ</n-button>
				<n-button type="primary" @click="next">次へ</n-button>
			</n-space>
		</n-spin>
	</div>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { FormInst, useMessage, useLoadingBar, SelectOption } from 'naive-ui';
import moment from 'moment';
import { EnumOrderType, EnumAirport } from '@/enum';
import { useMyOptions } from '@/composables';
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
const { orderTypeOptions, terminalOptions, airportOptions } = useMyOptions();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const loading = ref<boolean>(false);
const formValue = ref<MyModel.Order>({
	id: props.model?.id ?? '',
	orderNo: props.model?.orderNo,
	orderType: props.model?.orderType,
	airportType: props.model?.airportType,
	orderDays: props.model?.orderDays,
	startTime: new Date(props.model?.startTime ?? new Date()).getTime(),
	endTime: new Date(props.model?.endTime ?? new Date()).getTime(),
	adultNum: props.model?.adultNum,
	childrenNum: props.model?.childrenNum,
	luggageNum: props.model?.luggageNum,

	amount: props.model?.amount,
	currencyAmount: props.model?.currencyAmount,
	currency: props.model?.currency,
	currencyCode: props.model?.currencyCode,

	orderFrom: props.model?.orderFrom,
	orderTo: props.model?.orderTo,
	orderFromDetails: props.model?.orderFromDetails,
	orderToDetails: props.model?.orderToDetails,

	flightNo: props.model?.flightNo,
	airport: props.model?.airport,
	airportCode: props.model?.airportCode,
	terminal: props.model?.terminal,

	feeType: props.model?.feeType ?? '全包',
	isCash: props.model?.isCash === true ? 'true' : 'false',
	isOutTimeCash: props.model?.isOutTimeCash === true ? 'true' : 'false',
	outTimeAmount: props.model?.outTimeAmount,

	carId: props.model?.carId,
	carNo: props.model?.carNo,
	carName: props.model?.carName,
	carType: props.model?.carType,
	carSeat: props.model?.carSeat,
	specifyCarType: props.model?.specifyCarType,

	driverId: props.model?.driverId,
	driverNo: props.model?.driverNo,
	driverName: props.model?.driverName,
	driverPhoto: props.model?.driverPhoto,

	sellerId: props.model?.sellerId,
	sellerNo: props.model?.sellerNo,
	sellerName: props.model?.sellerName,
	sellerPhoto: props.model?.sellerPhoto
});

const rules = {
	orderType: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	orderNo: {
		required: false,
		trigger: 'input',
		message: '20文字まで入力してください',
		max: 20
	},
	airportType: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	feeType: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	isCash: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	isOutTimeCash: {
		required: true,
		trigger: 'change',
		message: '選択してください'
	},
	currency: {
		required: true,
		message: '選択してください',
		trigger: 'change'
	},
	currencyAmount: {
		required: true,
		message: '入力してください',
		type: 'number',
		trigger: ['blur', 'change']
	},
	outTimeAmount: {
		required: true,
		message: '入力してください',
		type: 'number',
		trigger: ['blur', 'change']
	},
	adultNum: {
		required: true,
		message: '入力してください',
		type: 'number',
		trigger: ['blur', 'change']
	},
	luggageNum: {
		required: true,
		message: '入力してください',
		type: 'number',
		trigger: ['blur', 'change']
	},
	orderFrom: {
		required: true,
		trigger: 'input',
		message: '200文字まで入力してください',
		max: 200
	},
	orderTo: {
		required: true,
		trigger: 'input',
		message: '200文字まで入力してください',
		max: 200
	}
};

/** ************************ Save Form ************************ */

const urls = {
	setOrder: `/order/setOrder`
};

const save = () => {
	const params: MyModel.Order = {
		id: formValue.value.id,
		orderNo: formValue.value.orderNo,
		orderType: formValue.value.orderType,
		airportType: formValue.value.airportType,
		orderDays: formValue.value.orderDays,
		startTime: formValue.value.startTime,
		endTime: formValue.value.endTime,
		adultNum: formValue.value.adultNum,
		childrenNum: formValue.value.childrenNum,
		luggageNum: formValue.value.luggageNum,
		amount: formValue.value.amount,
		currencyAmount: formValue.value.currencyAmount,
		currency: formValue.value.currency,
		currencyCode: formValue.value.currencyCode,

		orderFrom: formValue.value.orderFrom,
		orderTo: formValue.value.orderTo,
		orderFromDetails: formValue.value.orderFromDetails,
		orderToDetails: formValue.value.orderToDetails,
		flightNo: formValue.value?.flightNo,
		airport: formValue.value?.airport,
		airportCode: formValue.value?.airportCode,
		terminal: formValue.value?.terminal,

		feeType: formValue.value?.feeType,
		isCash: formValue.value?.isCash === 'true',
		isOutTimeCash: formValue.value?.isOutTimeCash === 'true',
		outTimeAmount: formValue.value?.outTimeAmount,

		carId: formValue.value?.carId,
		carNo: formValue.value?.carNo,
		carName: formValue.value?.carName,
		carType: formValue.value?.carType,
		carSeat: formValue.value?.carSeat,
		specifyCarType: formValue.value?.specifyCarType,

		driverId: formValue.value?.driverId,
		driverNo: formValue.value?.driverNo,
		driverName: formValue.value?.driverName,
		driverPhoto: formValue.value?.driverPhoto
	};
	const promise = request.post<MyModel.Order>(`${urls.setOrder}`, params);
	loadingBar.start();
	loading.value = true;
	promise
		.then(res => {
			if (res.data) {
				emits('next', { key: 'order', params: res.data });
			}
		})
		.catch(err => {
			message.warning(err);
		})
		.finally(() => {
			loadingBar.finish();
		});
};

const prev = () => {
	emits('prev', { key: 'order', params: formValue.value });
};
const next = (e: MouseEvent) => {
	e.preventDefault();
	formRef.value?.validate(errors => {
		if (!errors) {
			save();
		}
	});
};

/** ************************ Save Form ************************ */
const handleUpdateValue = (value: string, option: SelectOption) => {
	// message.info(`value: ${JSON.stringify(value)}`);
	// message.info(`option: ${JSON.stringify(option)}`);
	const type = value as MyEnumType.EnumOrderTypeKey;
	let hours = 1;
	switch (EnumOrderType[type]) {
		case EnumOrderType.Haiya:
			formValue.value.orderDays = 1;
			hours = 10;
			break;
		case EnumOrderType.TimeLimitHaiya:
			formValue.value.orderDays = 0.5;
			hours = 5;
			break;
		case EnumOrderType.Single:
			formValue.value.orderDays = 0.25;
			hours = 3;
			break;
		case EnumOrderType.Airport_S:
		case EnumOrderType.Airport_Y:
			formValue.value.orderDays = 0.25;
			break;
		default:
			formValue.value.orderDays = 1;
			break;
	}
	// formValue.value.endTime = moment(formValue.value.startTime).add(formValue.value.orderDays, 'days').valueOf();
	formValue.value.endTime = moment(formValue.value.startTime).add(hours, 'hours').valueOf();
};
const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
	dictModal.value?.showModal();
	dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
	switch (result.type) {
		case 'car_type':
			formValue.value.specifyCarType = result.text;
			break;
		case 'pay_currency':
			formValue.value.currency = result.text;
			formValue.value.currencyCode = result.value;
			break;

		default:
			break;
	}
};
const carModal = ref<any>(null);
const showCar = () => {
	carModal.value?.showModal();
};
const selectCar = (result: any) => {
	formValue.value.carId = result.value;
	formValue.value.carName = result.text;
	formValue.value.carNo = result.carNo;
	formValue.value.carType = result.carType;
	formValue.value.carSeat = result.carSeat;
};
const driverModal = ref<any>(null);
const showDriver = () => {
	driverModal.value?.showModal();
};

const selectDriver = (result: any) => {
	formValue.value.driverId = result.id;
	formValue.value.driverName = result.userName;
	formValue.value.driverNo = result.userNo;
	formValue.value.driverPhoto = result.userAvatar;
};

const airportUpdateValue = (value: string) => {
	const type = value as MyEnumType.EnumAirportKey;
	formValue.value.airport = EnumAirport[type];
	let hours = 1;
	switch (EnumAirport[type]) {
		case EnumAirport.Narita_N:
		case EnumAirport.Narita_Y:
			hours = 4;
			break;
		case EnumAirport.Haneda_N:
		case EnumAirport.Haneda_Y:
			hours = 3;
			break;
		default:
			break;
	}
	formValue.value.endTime = moment(formValue.value.startTime).add(hours, 'hours').valueOf();
};
const onUpdate = () => {
	// formValue.value.endTime = moment(formValue.value.startTime).add(formValue.value.orderDays, 'days').valueOf();
	let hours = 1;
	switch (formValue.value.orderType) {
		case 'Haiya':
			hours = 10;
			break;
		case 'TimeLimitHaiya':
			hours = 5;
			break;
		case 'Single':
			hours = 3;
			break;
		default:
			break;
	}
	switch (formValue.value.airport) {
		case 'Narita_N':
		case 'Narita_Y':
			hours = 4;
			break;
		case 'Haneda_N':
		case 'Haneda_Y':
			hours = 3;
			break;
		default:
			break;
	}
	formValue.value.endTime = moment(formValue.value.startTime).add(hours, 'hours').valueOf();
};

onMounted(() => {
	loading.value = true;
	setTimeout(() => {
		loading.value = false;
	}, 300);
});
</script>

<style scoped></style>
