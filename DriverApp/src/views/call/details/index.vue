<template>
	<div class="pb-20">
		<van-skeleton title :row="10" :loading="loading">
			<van-form @submit="onSubmit">
				<van-cell-group inset :title="`ー ${modelRef.date} 乗務${flagRef === 'am' ? '前' : '後'
			}点呼 ー`">
					<template v-if="flagRef === 'am'">
						<van-cell title="点呼日時">{{
			moment(modelRef.amCallTime).format('HH:mm')
		}}</van-cell>
						<van-cell title="点呼方法">{{ modelRef.amCallMethod }}</van-cell>
						<van-cell center title="アルコール検知器">
							<template #right-icon>
								<van-switch v-model="modelRef.am1" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.am1 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="酒気帯びの有無">
							<template #right-icon>
								<van-switch v-model="modelRef.am2" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.am2 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="異常の有無">
							<template #right-icon>
								<van-switch v-model="modelRef.am3" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.am3 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="睡眠不足有無">
							<template #right-icon>
								<van-switch v-model="modelRef.am4" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.am4 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-field name="amImage" label="写真">
							<template #input>
								<van-image v-if="modelRef.amImage" width="80" height="80"
									:src="`${envConfig.static}${modelRef.amImage}`" />
								<file-upload class="m-4" :max="1" file-key="images/call" @finish="onFinish" />
							</template>
						</van-field>
						<van-field v-model="modelRef.amRemark" type="textarea" maxlength="200" show-word-limit label="備考" />
					</template>
					<template v-else-if="flagRef === 'pm'">
						<van-cell title="点呼日時">{{
			moment(modelRef.pmCallTime).format('HH:mm')
		}}</van-cell>
						<van-cell title="点呼方法">{{ modelRef.pmCallMethod }}</van-cell>
						<van-cell center title="アルコール検知器">
							<template #right-icon>
								<van-switch v-model="modelRef.pm1" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.pm1 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="酒気帯びの有無">
							<template #right-icon>
								<van-switch v-model="modelRef.pm2" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.pm2 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="異常の有無">
							<template #right-icon>
								<van-switch v-model="modelRef.pm3" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.pm3 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-cell center title="睡眠不足有無">
							<template #right-icon>
								<van-switch v-model="modelRef.pm4" active-value="有" inactive-value="無">
									<template #node>
										<div class="text-size-xs text-center">
											{{ modelRef.pm4 }}
										</div>
									</template>
								</van-switch>
							</template>
						</van-cell>
						<van-field name="pmImage" label="写真">
							<template #input>
								<van-image v-if="modelRef.pmImage" width="80" height="80"
									:src="`${envConfig.static}${modelRef.pmImage}`" />
								<file-upload class="m-4" :max="1" file-key="images/call" @finish="onFinish" />
							</template>
						</van-field>
						<van-field v-model="modelRef.pmRemark" type="textarea" maxlength="200" show-word-limit label="備考" />
					</template>
				</van-cell-group>
				<div style="margin: 16px">
					<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading" :disabled="okRef">
						{{ `${okRef ? '提出済' : '報告提出'}` }}
					</van-button>
				</div>
			</van-form>
		</van-skeleton>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify, showToast } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const envConfig = getEnvConfig(import.meta.env);

const modelRef = ref<MyModel.Call>({
	id: '',
});
const flagRef = ref<any>();
const okRef = ref<any>(false);
const images = ref<string>('');
const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const route = useRouter();

const urls = {
	details: '/driver/call/details/',
	setAmCall: '/driver/setAmCall',
	setPmCall: '/driver/setPmCall',
};
const onload = () => {
	const { flag, item, isOk } = route.currentRoute.value.query;
	const jsonStr: any = item;
	const isOkStr: any = isOk;
	okRef.value = isOkStr === '1';

	flagRef.value = flag;
	modelRef.value = JSON.parse(decodeURIComponent(jsonStr));
	console.log(modelRef.value);
	switch (flagRef.value) {
		case 'am':
			images.value = modelRef.value.amImage ?? '';
			break;
		case 'pm':
			images.value = modelRef.value.pmImage ?? '';
			break;
		default:
			images.value = '';
			break;
	}

	if (!modelRef.value.id) {
		modelRef.value.id = '';

		modelRef.value.date = moment().format('yyyy/MM/DD');
		modelRef.value.amCallMethod = '報告';
		modelRef.value.amCallTime = moment().valueOf();
		modelRef.value.am1 = '有';
		modelRef.value.am2 = '無';
		modelRef.value.am3 = '無';
		modelRef.value.am4 = '無';
		modelRef.value.amImage = '';
		modelRef.value.amRemark = '';
	}
	if (!modelRef.value.pmCallTime) {
		modelRef.value.pmCallMethod = '報告';
		modelRef.value.pmCallTime = moment().valueOf();
		modelRef.value.pm1 = '有';
		modelRef.value.pm2 = '無';
		modelRef.value.pm3 = '無';
		modelRef.value.pm4 = '無';
		modelRef.value.pmImage = '';
		modelRef.value.pmRemark = '';
	}
};

const onSubmit = () => {
	if (!images.value) {
		showToast('写真をアップロードください');
		return;
	}
	loading.value = true;
	const amParams = {
		id: modelRef.value.id,
		driverId: modelRef.value.driverId,
		driverNo: modelRef.value.driverNo,
		driverName: modelRef.value.driverName,

		date: modelRef.value.date,
		amCallMethod: modelRef.value.amCallMethod,
		amCallTime: modelRef.value.amCallTime,
		am1: modelRef.value.am1,
		am2: modelRef.value.am2,
		am3: modelRef.value.am3,
		am4: modelRef.value.am4,
		amRemark: modelRef.value.amRemark,
		amImage: images.value,
	};

	const pmParams = {
		id: modelRef.value.id,
		driverId: modelRef.value.driverId,
		driverNo: modelRef.value.driverNo,
		driverName: modelRef.value.driverName,

		pmCallMethod: modelRef.value.pmCallMethod,
		pmCallTime: modelRef.value.pmCallTime,
		pm1: modelRef.value.pm1,
		pm2: modelRef.value.pm2,
		pm3: modelRef.value.pm3,
		pm4: modelRef.value.pm4,
		pmRemark: modelRef.value.pmRemark,
		pmImage: images.value,
	};

	const url = flagRef.value === 'am' ? urls.setAmCall : urls.setPmCall;
	const params = flagRef.value === 'am' ? amParams : pmParams;
	request
		.post<MyModel.Call>(`${url}`, params)
		.then((res) => {
			if (res.data) {
				const { data } = res;
				modelRef.value = data;
				routerPush({
					path: '/home',
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
const onFinish = (fileUrl: string) => {
	if (!fileUrl) {
		images.value = '';
		return;
	}
	images.value = fileUrl;
};
onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
