<template>
  <n-upload
    v-model:file-list="fileListRef"
    :file-list-style="fileListStyle"
    name="file"
    :max="max"
    :max-size="size"
    :accept="accept"
    :action="action"
    :disabled="disabled"
    :list-type="listType"
    :show-file-list="showFileList"
    :headers="{ Authorization: token ?? '' }"
    :data="{
      key: fileKey
    }"
    @change="handleChange"
  >
    <div v-if="listType === 'text' || listType === 'image'" class="flex" align="center">
      <div>
        <n-button>{{ btnText }}</n-button>
      </div>
      <div v-if="showTips" class="flex items-center">
        <n-tag :bordered="false" class="ml-4"
          >ファイルサイズは{{ `${(props.size / 1048576).toFixed(1)}MB` }}以下
        </n-tag>
      </div>
    </div>
    <div v-else>
      {{ btnText }}
    </div>
  </n-upload>
</template>

<script setup lang="ts">
import { ref, PropType, onMounted } from 'vue';
import { ListType } from 'naive-ui/es/upload/src/interface';
import { getToken } from '@/utils';

const token = getToken();

/**
 * A valid case-insensitive filename extension, starting with a period (".") character. For example: .jpg, .pdf, or .doc.
 * A valid MIME type string, with no extensions.
 * The string audio/* meaning "any audio file".
 * The string video/* meaning "any video file".
 * The string image/* meaning "any image file".
 * .doc,.docx,.xml,application/msword,
 * application/vnd.openxmlformats-officedocument.wordprocessingml.document
 */
const props = defineProps({
  accept: {
    type: String,
    default() {
      return 'image/*,.pdf,.doc,.docx';
    }
  },
  fileKey: {
    type: String,
    default() {
      return '';
    }
  },
  disabled: {
    type: Boolean,
    default() {
      return false;
    }
  },
  action: {
    type: String,
    default() {
      return '/api/common/upload/';
    }
  },
  size: {
    type: Number,
    default() {
      return 20971520; // < 20M  |  1M=1048576
    }
  },
  max: {
    type: Number,
    default() {
      return 1;
    }
  },
  listType: {
    type: String as PropType<ListType | undefined>,
    default() {
      return 'text'; // text、image、image-card
    }
  },
  showTips: {
    type: Boolean,
    default() {
      return true;
    }
  },
  showFileList: {
    type: Boolean,
    default() {
      return true;
    }
  },
  btnText: {
    type: String,
    default() {
      return 'アップロード';
    }
  },
  fileListStyle: {
    type: Object,
    default() {
      return {
        width: '600px'
      };
    }
  }
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
const emits = defineEmits(['finish']);

const handleChange = (options: any) => {
  const { file, fileList, event } = options;

  if (file.status === 'uploading') {
    return;
  }
  if (file.status === 'finished') {
    fileListRef.value = fileList;
    file.url = JSON.parse(event.target.response).data;
    const files = fileList.map((item: any) => {
      return {
        name: item.name,
        url: item.url ?? item.fileUrl
      };
    });
    emits('finish', files);
  }
  if (file.status === 'error') {
    // error
  }
};
defineExpose({
  initFiles,
  cleanFiles
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
