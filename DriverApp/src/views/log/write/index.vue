<template>
	<div class="pb-20">
		<van-form @submit="onSubmit">
			<van-cell-group inset title="ー 基本情報 ー">
				<van-field v-model="modelRef.userNo" :label="formValue.date" readonly />
				<van-field v-model="formValue.driver1Name" label="運転士(正)" value="" />
				<van-field v-model="formValue.driver2Name" label="運転士(副)" value="" />
				<van-field v-model="formValue.carNo" label="車両番号" value="" />
				<van-field v-model="formValue.weather" label="天候" value="" />
				<van-field v-model="formValue.personNum" label="人数" value="" />
				<van-field v-model="formValue.manager" label="整備管理者" />
			</van-cell-group>
			<van-cell-group inset title="ー 運転席点検 ー">
				<van-cell center title="ブレーキぺルタ-踏みしろ・きき具合">
					<template #right-icon>
						<van-switch v-model="formValue.a1" />
					</template>
				</van-cell>
				<van-cell center title="駐車ブレーキ・レバー-引きしろ">
					<template #right-icon>
						<van-switch v-model="formValue.a2" />
					</template>
				</van-cell>
				<van-cell center title="エンジン-かかり具合・低速、加速状態">
					<template #right-icon>
						<van-switch v-model="formValue.a3" />
					</template>
				</van-cell>
				<van-cell center title="ウインド・ウォッシャー-噴射状態">
					<template #right-icon>
						<van-switch v-model="formValue.a4" />
					</template>
				</van-cell>
				<van-cell center title="ウイパー-払拭状態">
					<template #right-icon>
						<van-switch v-model="formValue.a5" />
					</template>
				</van-cell>
				<van-cell center title="室内-清掃状態">
					<template #right-icon>
						<van-switch v-model="formValue.a6" />
					</template>
				</van-cell>
				<van-cell center title="ドアロック・座席ベルト-損傷有無">
					<template #right-icon>
						<van-switch v-model="formValue.a7" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー ルーム点検・エンジン ー">
				<van-cell center title="ウィンド・ウォッシャー-液量">
					<template #right-icon>
						<van-switch v-model="formValue.b1" />
					</template>
				</van-cell>
				<van-cell center title="ブルーキリザーブ・タンク-液量">
					<template #right-icon>
						<van-switch v-model="formValue.b2" />
					</template>
				</van-cell>
				<van-cell center title="バッテリー-液量">
					<template #right-icon>
						<van-switch v-model="formValue.b3" />
					</template>
				</van-cell>
				<van-cell center title="ラジエーター-水量">
					<template #right-icon>
						<van-switch v-model="formValue.b4" />
					</template>
				</van-cell>
				<van-cell center title="エンジンオイル-油量">
					<template #right-icon>
						<van-switch v-model="formValue.b5" />
					</template>
				</van-cell>
				<van-cell center title="ファンベルト-張り具合・損傷">
					<template #right-icon>
						<van-switch v-model="formValue.b6" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー 車の周りの点検 ー">
				<van-cell center title="タイヤ-空気圧">
					<template #right-icon>
						<van-switch v-model="formValue.c1" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-亀裂、損傷">
					<template #right-icon>
						<van-switch v-model="formValue.c2" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-異常な摩耗">
					<template #right-icon>
						<van-switch v-model="formValue.c3" />
					</template>
				</van-cell>
				<van-cell center title="タイヤ-溝の深さ">
					<template #right-icon>
						<van-switch v-model="formValue.c4" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-点灯具合">
					<template #right-icon>
						<van-switch v-model="formValue.c5" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-点滅具合">
					<template #right-icon>
						<van-switch v-model="formValue.c6" />
					</template>
				</van-cell>
				<van-cell center title="灯火装置及び方向指示器-汚れ、損傷">
					<template #right-icon>
						<van-switch v-model="formValue.c7" />
					</template>
				</van-cell>
				<van-cell center title="ディスク・ホイールの取付-取り付け状況">
					<template #right-icon>
						<van-switch v-model="formValue.c8" />
					</template>
				</van-cell>
			</van-cell-group>
			<van-cell-group inset title="ー その他 ー">
				<van-cell center title="非常信号計・工具-備え付け">
					<template #right-icon>
						<van-switch v-model="formValue.d1" />
					</template>
				</van-cell>
				<van-cell center title="車検証・保険証-備え付け">
					<template #right-icon>
						<van-switch v-model="formValue.d2" />
					</template>
				</van-cell>
				<van-cell title="点検備考">
					<template #label>
						{{ formValue.checkRemark }}
					</template>
				</van-cell>
			</van-cell-group>
			<div style="margin: 16px">
				<van-button size="large" block color="#fb8c00" native-type="submit" :loading="loading">
					保存
				</van-button>
			</div>
		</van-form>
	</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { showNotify } from 'vant';
import moment from 'moment';
import { useRouterPush } from '@/composables';
import { getDictItems } from '@/service';
import { request } from '@/service/request';

const modelRef = ref<MyModel.User>({
	id: '',
});
const formValue = ref<MyModel.Log>({
	id: '',
	date: moment().format('yyyy-MM-DD'),
	a1: true,
	a2: true,
	a3: true,
	a4: true,
	a5: true,
	a6: true,
	a7: true,

	b1: true,
	b2: true,
	b3: true,
	b4: true,
	b5: true,
	b6: true,

	c1: true,
	c2: true,
	c3: true,
	c4: true,
	c5: true,
	c6: true,
	c7: true,
	c8: true,

	d1: true,
	d2: true,
});

const { routerPush } = useRouterPush();
const loading = ref<boolean>(false);
const urls = {
	writeLog: '/driver/user/writeLog',
	dict: '/base/dictItem/list',
	upload: '/common/upload',
};
const onload = () => {
	// const jsonStr: any = route.currentRoute.value.query.item;
	// modelRef.value = JSON.parse(decodeURIComponent(jsonStr));
};

const toLogList = () => {
	routerPush({
		path: '/log/list',
	});
};
const onSubmit = () => {
	const params = {
		date: moment().format('yyyy-MM-DD'),

		driver2Name: formValue.value.driver2Name,
		carName: formValue.value.carName,
		manager: formValue.value.manager,
		personNum: formValue.value.personNum,
		weather: formValue.value.weather,

		a1: formValue.value.a1,
		a2: formValue.value.a2,
		a3: formValue.value.a3,
		a4: formValue.value.a4,
		a5: formValue.value.a5,
		a6: formValue.value.a6,
		a7: formValue.value.a7,

		b1: formValue.value.b1,
		b2: formValue.value.b2,
		b3: formValue.value.b3,
		b4: formValue.value.b4,
		b5: formValue.value.b5,
		b6: formValue.value.b6,

		c1: formValue.value.c1,
		c2: formValue.value.c2,
		c3: formValue.value.c3,
		c4: formValue.value.c4,
		c5: formValue.value.c5,
		c6: formValue.value.c6,
		c7: formValue.value.c7,
		c8: formValue.value.c8,

		d1: formValue.value.d1,
		d2: formValue.value.d2,
		checkRemark: formValue.value.checkRemark,
	};
	const promise = request.post<Boolean>(`${urls.writeLog}`, params);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				toLogList();
				showNotify({
					type: 'success',
					message: '保存しました',
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

onMounted(() => {
	onload();
});
</script>

<style lang="scss"></style>
