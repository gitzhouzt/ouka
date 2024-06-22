<template>
	<div class="pb-20">
		<van-form>
			<van-cell-group inset title="ー 基本情報 ー">
				<van-field :label="modelRef.date" readonly />
				<van-field v-model="modelRef.driver1Name" label="運転士(正)" />
				<van-field v-model="modelRef.driver2Name" label="運転士(副)" value="" />
				<van-field v-model="modelRef.carNo" label="車両番号" value="" />
				<van-field v-model="modelRef.weather" label="天候" value="" />
				<van-field v-model="modelRef.personNum" label="人数" value="" />
				<van-field v-model="modelRef.manager" label="整備管理者" />
			</van-cell-group>
			<van-cell-group v-if="logRecordVOs.length > 0" inset title="ー 走行記録 ー">
				<van-cell v-for="(r, i) in logRecordVOs" :key="r.id" center
					:title="`記録 ${i + 1} - ${moment(r.startTime).format('HH:mm:DD')}`" :label="r.start">
					<template #label>
						<div>
							<div>
								{{ `${r.start ?? '出発地'} - ${r.end ?? '到着地'}` }}
							</div>
							<div>距離：{{ `${r.distance ?? 0} km` }}</div>
							<div>実車：{{ `${r.jissha ?? 0} km` }}</div>
							<div>回送：{{ `${r.kaisou ?? 0} km` }}</div>
							<div>休憩時間：{{ `${r.rest ? r.rest : '-'}` }}</div>
							<div>休憩時間数：{{ `${r.restNum ? r.restNum : '-'}` }}</div>
						</div>
						<div></div>
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー 運転席点検 ー">
				<van-cell center title="ブレーキぺルタ-踏みしろ・きき具合">
					<template #right-icon>
						<van-switch v-model="modelRef.a1" />
					</template>
				</van-cell>
				<van-cell center title="駐車ブレーキ・レバー-引きしろ">
					<template #right-icon>
						<van-switch v-model="modelRef.a2" />
					</template>
				</van-cell>
				<van-cell center title="エンジン-かかり具合・低速、加速状態">
					<template #right-icon>
						<van-switch v-model="modelRef.a3" />
					</template>
				</van-cell>
				<van-cell center title="ウインド・ウォッシャー-噴射状態">
					<template #right-icon>
						<van-switch v-model="modelRef.a4" />
					</template>
				</van-cell>
				<van-cell center title="ウイパー-払拭状態">
					<template #right-icon>
						<van-switch v-model="modelRef.a5" />
					</template>
				</van-cell>
				<van-cell center title="室内-清掃状態">
					<template #right-icon>
						<van-switch v-model="modelRef.a6" />
					</template>
				</van-cell>
				<van-cell center title="ドアロック・座席ベルト-損傷有無">
					<template #right-icon>
						<van-switch v-model="modelRef.a7" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー ルーム点検・エンジン ー">
				<van-cell center title="ウィンド・ウォッシャー-液量">
					<template #right-icon>
						<van-switch v-model="modelRef.b1" />
					</template>
				</van-cell>
				<van-cell center title="ブルーキリザーブ・タンク-液量">
					<template #right-icon>
						<van-switch v-model="modelRef.b2" />
					</template>
				</van-cell>
				<van-cell center title="バッテリー-液量">
					<template #right-icon>
						<van-switch v-model="modelRef.b3" />
					</template>
				</van-cell>
				<van-cell center title="ラジエーター-水量">
					<template #right-icon>
						<van-switch v-model="modelRef.b4" />
					</template>
				</van-cell>
				<van-cell center title="エンジンオイル-油量">
					<template #right-icon>
						<van-switch v-model="modelRef.b5" />
					</template>
				</van-cell>
				<van-cell center title="ファンベルト-張り具合・損傷">
					<template #right-icon>
						<van-switch v-model="modelRef.b6" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー 車の周りの点検 ー">
				<van-cell center title="タイヤ-空気圧">
					<template #right-icon>
						<van-switch v-model="modelRef.c1" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-亀裂、損傷">
					<template #right-icon>
						<van-switch v-model="modelRef.c2" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-異常な摩耗">
					<template #right-icon>
						<van-switch v-model="modelRef.c3" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-溝の深さ">
					<template #right-icon>
						<van-switch v-model="modelRef.c4" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-点灯具合">
					<template #right-icon>
						<van-switch v-model="modelRef.c5" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-点滅具合">
					<template #right-icon>
						<van-switch v-model="modelRef.c6" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-汚れ、損傷">
					<template #right-icon>
						<van-switch v-model="modelRef.c7" />
					</template>
				</van-cell>
				<van-cell center title="ディスク・ホイールの取付-取り付け状況">
					<template #right-icon>
						<van-switch v-model="modelRef.c8" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー その他 ー">
				<van-cell center title="非常信号計・工具-備え付け">
					<template #right-icon>
						<van-switch v-model="modelRef.d1" />
					</template>
				</van-cell>
				<van-cell center title="車検証・保険証-備え付け">
					<template #right-icon>
						<van-switch v-model="modelRef.d2" />
					</template>
				</van-cell>
				<van-cell title="点検備考">
					<template #label>
						{{ modelRef.checkRemark }}
					</template>
				</van-cell>
			</van-cell-group>
			<!-- <div style="margin: 16px">
				<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
					保存
				</van-button>
			</div> -->
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { request } from '@/service/request';

const modelRef = ref<MyModel.Log>({
	id: '',
});
const logRecordVOs = ref<MyModel.LogRecord[]>([]);

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const route = useRouter();
const urls = {
	details: '/driver/user/log/details',
};
const onload = () => {
	const { id } = route.currentRoute.value.query;
	request
		.get<MyModel.LogDetails>(`${urls.details}/${id}`)
		.then((res) => {
			if (res.data) {
				const { data } = res;
				modelRef.value = data.userLogVO;
				logRecordVOs.value = data.logRecordVOs;
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

const toLogList = () => {
	routerPush({
		path: '/log/list',
	});
};

onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
