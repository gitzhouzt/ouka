<template>
	<div>
		<n-drawer v-model:show="showModalRef" :style="bodyStyleRef" placement="right" :mask-closable="true">
			<n-card class="h-full shadow-sm rounded-16px" :title="titleRef">
				<n-space :vertical="true">
					<n-grid :y-gap="2" :cols="1">
						<n-grid-item>
							<n-form-item label="订单来源">
								<n-input v-model:value="params.orderSource" placeholder="クリック订单来源を選択" readonly
									@click="showDict('order_source')" />
							</n-form-item>
							<n-form-item label="第三者" path="orderKey">
								<n-input-group>
									<n-input v-model:value="params.orderKey"></n-input>
								</n-input-group>
							</n-form-item>
							<n-form-item label="責任人" path="sellerName">
								<n-input-group>
									<n-input v-model:value="params.sellerName" readonly placeholder="クリック責任人を選択"
										@click="showStaff()"></n-input>
								</n-input-group>
							</n-form-item>
							<n-form-item label="都道府県">
								<n-input v-model:value="params.city" placeholder="クリック都道府県を選択" readonly
									@click="showDict('order_city')" />
							</n-form-item>
							<n-form-item label="注文日時" path="orderTime">
								<n-date-picker v-model:value="params.orderTime" type="datetimerange" format="yyyy/MM/dd HH:mm:ss"
									clearable @update:value="onUpdate" />
							</n-form-item>
							<n-form-item label="ステータス" path="orderStatus">
								<n-select v-model:value="params.orderStatus" class="w-50" multiple :options="deployStatusOptions"
									:consistent-menu-width="false" />
							</n-form-item>
						</n-grid-item>
					</n-grid>
				</n-space>
				<n-space justify="center">
					<n-button type="primary" @click="onClick">検索</n-button>
					<n-button class="ml-2" @click="onReset">リセット</n-button>
				</n-space>
				<staff-select-modal ref="staffModal" @click="selectStaff" />
				<dict-select-modal ref="dictModal" @click="selectDict" />
			</n-card>
		</n-drawer>
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useMyOptions } from '@/composables';

const { deployStatusOptions } = useMyOptions();

const emits = defineEmits(['click', 'reset']);
const params = ref<any>();
const bodyStyleRef = ref({
	width: '300px'
});
const showModalRef = ref<boolean | undefined>(false);
const showModal = (searchParams: any) => {
	params.value = searchParams;
	showModalRef.value = true;
};

const titleRef = ref<string | undefined>('詳細検索');
const setTitle = (title: string) => {
	titleRef.value = title;
};
const close = () => {
	showModalRef.value = false;
};

const staffModal = ref<any>(null);
const showStaff = () => {
	staffModal.value?.showModal();
};
const selectStaff = (result: any) => {
	params.value.sellerName = result.userName;
};

const onUpdate = (value: [number, number] | null, formattedValue: [string, string] | null) => {
	params.value.beginTime = value ? value[0] : undefined;
	params.value.endTime = value ? value[1] : undefined;
};
const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
	dictModal.value?.showModal();
	dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
	switch (result.type) {
		case 'order_source':
			params.value.orderSource = result.text;
			break;
		case 'order_city':
			params.value.city = result.text;
			break;
		default:
			break;
	}
};

const onClick = () => {
	emits('click', params.value);
};
const onReset = () => {
	emits('reset');
};
defineExpose({
	showModal,
	setTitle,
	close
});
</script>

<style scoped></style>
