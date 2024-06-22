<!-- eslint-disable no-param-reassign -->
<template>
	<van-uploader v-model="fileListRef" name="file" :max-count="max" :max-size="size" :accept="accept"
		:disabled="disabled" :upload-text="btnText" :headers="{ Authorization: token ?? '' }" :data="{
		key: fileKey,
	}" :multiple="max > 0" :before-read="beforeRead" :after-read="afterRead" @oversize="onOversize" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { showNotify, showToast } from 'vant';
import loading from 'vant/lib/loading';
import { getToken } from '@/utils';
import { request } from '@/service/request';

const token = getToken();

const emits = defineEmits(['finish']);
const props = defineProps({
	accept: {
		type: String,
		default() {
			return 'image/*';
		},
	},
	fileKey: {
		type: String,
		default() {
			return '';
		},
	},
	disabled: {
		type: Boolean,
		default() {
			return false;
		},
	},
	size: {
		type: Number,
		default() {
			return 500 * 1024; // 20971520; // < 20M  |  1M=1048576
		},
	},
	max: {
		type: Number,
		default() {
			return 1;
		},
	},
	showTips: {
		type: Boolean,
		default() {
			return true;
		},
	},
	showFileList: {
		type: Boolean,
		default() {
			return true;
		},
	},
	btnText: {
		type: String,
		default() {
			return 'アップロード';
		},
	},
});

const fileListRef = ref([]);
const cleanFiles = () => {
	fileListRef.value = [];
};
const initFiles = (files: any) => {
	console.debug('files: ', files);
	if (!files || files.length <= 0) {
		return;
	}
	fileListRef.value = files;
};

const urls = {
	upload: '/common/upload',
};

const beforeRead = (file: any) => {
	//

	// console.log(file);
	return true;
};
const afterRead = (file: any) => {
	// eslint-disable-next-line no-param-reassign
	file.status = 'uploading';
	// eslint-disable-next-line no-param-reassign
	file.message = 'アップロード...';

	const formData = new FormData();
	formData.append('file', file.file);
	formData.append('key', props.fileKey);
	const promise = request.post<string>(`${urls.upload}`, formData);
	loading.value = true;
	promise
		.then((res) => {
			if (res.data) {
				file.status = 'success';
				file.message = '';
				emits('finish', res.data);
			}
		})
		.catch((err) => {
			console.log(err);
			showNotify({
				type: 'danger',
				message: err,
			});
			file.status = 'failed';
			file.message = '失敗';
		})
		.finally(() => {
			loading.value = false;
		});
};
const onOversize = (file: any) => {
	showToast(`写真サイズは${props.size / 1024}KB以下`);
};

defineExpose({
	initFiles,
	cleanFiles,
});
</script>

<style>
/* you can make up upload button and sample style by using stylesheets */
.ant-upload-select-picture-card i {
	font-size: 32px;
	color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
	margin-top: 8px;
	color: #666;
}
</style>
