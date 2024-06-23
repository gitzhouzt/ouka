<template>
	<div style="margin-top: 20px">
		<van-tabs v-model:active="active">
			<van-tab title="請求なし">
				<van-form @submit="onSubmit">
					<van-cell-group inset>
						<van-field v-model="modelRef.orderNo" label="注文番号" readonly />
						<van-field v-model="modelRef.sellerName" label="責任人" readonly />
						<van-field v-model="formValue.payItem" name="payItem" label="名目" required placeholder="選択してください" readonly
							@click="onShowPicker('payItem')" />
						<van-field v-model="formValue.currency" name="currency" label="通貨" required placeholder="選択してください" readonly
							@click="onShowPicker('currency')" />
						<van-field v-model="formValue.currencyAmount" name="currencyAmount" label="金額" placeholder="入力してください"
							required type="number" />
						<file-upload class="m-4" :max="8" file-key="files/advance" @finish="onFinish" />
						<van-field v-model="formValue.remark" name="remark" label="備考" type="textarea" rows="3" maxlength="200"
							show-word-limit />
					</van-cell-group>
					<div style="margin: 16px">
						<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
							申請
						</van-button>
					</div>
				</van-form></van-tab>
			<van-tab title="請求あり"><van-form @submit="onSubmit">
					<van-cell-group inset>
						<van-field v-model="modelRef.orderNo" label="注文番号" readonly />
						<van-field v-model="modelRef.sellerName" label="責任人" readonly />
						<van-field v-model="formValue.advance_water" name="advance_water" label="立替水代" placeholder="選択してください"
							type="number" />
						<van-field v-model="formValue.advance_ticket" name="advance_ticket" label="立替入門料チケット" type="number" />
						<van-field v-model="formValue.advance_road" name="advance_road" label="立替有料道路" type="number" />
						<van-field v-model="formValue.advance_park" name="advance_park" label="立替駐車代" type="number" />
						<van-field v-model="formValue.advance_overtime" name="advance_overtime" label="超時料金" type="number" />
						<van-field v-model="formValue.advance_meal" name="advance_meal" label="立替食事代" type="number" />
						<van-field v-model="formValue.advance_hotel" name="advance_hotel" label="立替ホテル代" type="number" />
						<van-field v-model="formValue.advance_bath" name="advance_bath" label="立替入湯税" type="number" />
						<van-field v-model="formValue.advance_etc" name="	advance_etc" label="ETC料金" type="number" />
						<van-field v-model="formValue.advance_other1" name="advance_other1" label="立替その他①" type="number" />
						<van-field v-model="formValue.advance_other2" name="advance_other2" label="立替その他②" type="number" />
						<file-upload class="m-4" :max="8" file-key="files/advance" @finish="onFinish" />
						<van-field v-model="formValue.remark" name="remark" label="備考" type="textarea" rows="3" maxlength="200"
							show-word-limit />
					</van-cell-group>
					<div style="margin: 16px">
						<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
							申請
						</van-button>
					</div>
				</van-form></van-tab>
		</van-tabs>

		<van-popup v-model:show="showPicker.payItem" round position="bottom">
			<van-picker :columns="selectPayItemOptions" @cancel="onCancel('payItem')" @confirm="(values) => {
					onConfirm(values, 'payItem');
				}
				" />
		</van-popup>
		<van-popup v-model:show="showPicker.currency" round position="bottom">
			<van-picker :columns="selectCurrencyOptions" @cancel="onCancel('currency')" @confirm="(values) => {
					onConfirm(values, 'currency');
				}
				" />
		</van-popup>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import { useRouterPush } from '@/composables';
import { getDictItems } from '@/service';
import { request } from '@/service/request';

const modelRef = ref<MyModel.Order>({
	id: '',
});
const formValue = ref<MyModel.PayRecord>({
	id: '',
	amount: 0,
	currencyAmount: 0,
});

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);

const active = ref(0);
const route = useRouter();
const urls = {
	driverPay: '/driver/order/driverPay',
	dict: '/base/dictItem/list',
	upload: '/common/upload',
};
const selectPayItemOptions = ref<any>();
const selectCurrencyOptions = ref<any>();
const onload = () => {
	const jsonStr: any = route.currentRoute.value.query.item;
	modelRef.value = JSON.parse(decodeURIComponent(jsonStr));
	setTimeout(() => {
		getDictItems('pay_advance_item').then((res) => {
			selectPayItemOptions.value = res.data?.list.map((i) => {
				return { value: i.itemCode, text: i.itemName };
			});
		});
		getDictItems('pay_currency').then((res) => {
			selectCurrencyOptions.value = res.data?.list.map((i) => {
				return { value: i.itemCode, text: i.itemName };
			});
		});
	}, 300);
};

const toOrder = () => {
	routerPush({
		path: '/order/list',
	});
};
const onSubmit = () => {
	const params = {
		orderId: modelRef.value?.id,
		orderNo: modelRef.value?.orderNo,
		payMethod: '現金',
		payMethodCode: 'cash',
		payItem: formValue.value?.payItem,
		payItemCode: formValue.value?.payItemCode,
		amount: Number(formValue.value?.amount),
		currency: formValue.value?.currency,
		currencyCode: formValue.value?.currencyCode,
		currencyAmount: Number(formValue.value?.currencyAmount),
		remark: formValue.value?.remark,
		financeType: 'OutAdvance',
		images: formValue.value.images,
	};
	const promise = request.post<Boolean>(`${urls.driverPay}`, params);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				toOrder();
				showNotify({
					type: 'success',
					message: '申請しました',
				});
			}
		})
		.catch((err) => {
			showNotify({
				type: 'danger',
				message: err,
			});
		})
		.finally(() => {
			loading.value = false;
		});
};

const completed = ref({
	payItem: false,
	currency: false,
});
const showPicker = ref({
	payItem: false,
	currency: false,
});
const onShowPicker = (flag: string) => {
	if (!completed.value[flag]) showPicker.value[flag] = true;
};
const onConfirm = (
	{ selectedValues, selectedOptions, selectedIndexes }: any,
	flag: string
) => {
	if (selectedValues.length > 0) {
		const vf = `${flag}Code`;
		formValue.value[flag] = selectedOptions[0].text;
		formValue.value[vf] = selectedOptions[0].value;
		showPicker.value[flag] = false;
	}
};
const onCancel = (flag: string) => {
	showPicker.value[flag] = false;
};
const onFinish = (fileUrl: string) => {
	if (!fileUrl) {
		formValue.value.images = '';
		return;
	}
	const i = formValue.value.images;
	formValue.value.images = i ? `${i},${fileUrl}` : fileUrl;
};
onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
