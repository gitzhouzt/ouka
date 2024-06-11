<template>
  <div>
    <n-modal
      v-model:show="showModalRef"
      :style="bodyStyleRef"
      transform-origin="center"
      :mask-closable="false"
      preset="card"
      :title="titleRef"
      closable
      @update-show="onUpdateShow"
    >
      <n-card size="huge" role="dialog" aria-modal="true">
        <n-spin :show="loading">
          <n-form ref="formRef" label-placement="left" :label-width="80" :model="formValue" :rules="rules" :size="size">
            <n-form-item label="番号" path="userNo">
              <n-input v-model:value="formValue.userNo" :disabled="disabledRole" />
            </n-form-item>
            <n-form-item label="名前" path="userName">
              <n-input v-model:value="formValue.userName" :disabled="disabledRole" />
            </n-form-item>
            <!-- <n-form-item label="写真" path="userAvatar">
              <n-space>
                <file-upload
                  ref="fileUploadRef"
                  accept="image/*"
                  list-type="image-card"
                  file-key="images/user"
                  :max="1"
                  :size="1048576"
                  @finish="
              (fileList:any) => {
                onFinish(fileList);
              }
            "
                />
              </n-space>
            </n-form-item> -->
            <n-form-item label="役職" path="selUserRoles">
              <n-select
                v-model:value="formValue.selUserRoles"
                :disabled="disabledRole"
                :options="addUserRoleOptions"
                multiple
                :consistent-menu-width="false"
              />
            </n-form-item>
            <n-form-item label="メール" path="userEmail">
              <n-input v-model:value="formValue.userEmail" placeholder="example@example.com" />
            </n-form-item>
            <n-form-item label="電話" path="userPhone">
              <n-input-group
                ><n-input-number
                  v-model:value="formValue.countryNum"
                  placeholder="81"
                  :max="999"
                  :min="0"
                  :show-button="false"
                >
                  <template #prefix> + </template>
                </n-input-number>
                <n-input v-model:value="formValue.userPhone" placeholder="09012345678" />
              </n-input-group>
            </n-form-item>
            <n-form-item label="郵便番号" path="userPost">
              <n-input-group>
                <n-input-group-label>〒</n-input-group-label>
                <n-input v-model:value="formValue.userPost" placeholder="103-0000" />
              </n-input-group>
            </n-form-item>
            <n-form-item label="住所" path="userAddress">
              <n-input-group>
                <n-input v-model:value="formValue.userAddress" placeholder="東京都" />
              </n-input-group>
            </n-form-item>
            <n-form-item label="備考" path="remark">
              <n-input-group>
                <n-input v-model:value="formValue.remark" placeholder="備考" />
              </n-input-group>
            </n-form-item>
          </n-form>
          <n-space justify="center" class="text-red-500">
            アカウント：登録メールアドレス<br />
            初期パスワード：abcd@6789</n-space
          >
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { EnumUserRole } from '@/enum';
import { useMyOptions } from '@/composables';
import { formRules } from '@/utils';
import { request } from '@/service/request';
import { getEnvConfig } from '~/.env-config';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '600px'
});
const { addUserRoleOptions } = useMyOptions();
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.User>({
  id: '',
  userNo: '',
  userName: '',
  userEmail: '',
  userSex: '',
  userRoles: 'STAFF',

  selUserRoles: [],
  countryNum: 81,
  userPhone: undefined,
  userAvatar: undefined,
  userAddress: undefined,
  userPost: undefined,

  remark: ''
});
const rules = {
  userName: {
    required: true,
    message: '1-20文字まで入力してください',
    trigger: 'input',
    max: 20
  },
  userEmail: formRules.mail,
  userPhone: formRules.phone,
  userPost: formRules.post,
  userRoles: {
    required: true,
    message: '役職を選択してください',
    trigger: 'change'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const disabledRole = ref(false);
const showModal = (row: MyModel.User | undefined) => {
  showModalRef.value = true;
  if (row) {
    // 利用者
    formValue.value.id = row?.id;
    formValue.value.userNo = row?.userNo;
    formValue.value.userName = row?.userName;
    formValue.value.userEmail = row?.userEmail;
    formValue.value.userSex = row?.userSex;
    formValue.value.userRoles = row?.userRoles;
    formValue.value.selUserRoles = row?.userRoles.split(',');

    formValue.value.countryNum = row?.countryNum;
    formValue.value.userPhone = row?.userPhone;
    formValue.value.userAvatar = row?.userAvatar;
    formValue.value.userAddress = row?.userAddress;
    formValue.value.userPost = row?.userPost;
    formValue.value.remark = row?.remark;

    // 役職
    const { userRoles } = formValue.value;
    if (userRoles) {
      const roles = userRoles.split(',') as MyEnumType.EnumUserRoleKey[];
      roles.forEach(role => {
        if (EnumUserRole[role] === EnumUserRole.ADMIN || EnumUserRole[role] === EnumUserRole.SUPER) {
          disabledRole.value = true;
        }
      });
    }
  } else {
    formValue.value = {
      id: '',
      userNo: '',
      userName: '',
      userEmail: '',
      userSex: '',
      userRoles: 'STAFF',

      selUserRoles: [],
      countryNum: 81,
      userPhone: undefined,
      userAvatar: undefined,
      userAddress: undefined,
      userPost: undefined,

      remark: ''
    };
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
  const { userAvatar } = formValue.value;
  if (userAvatar) {
    const file = {
      uid: 'logo',
      name: userAvatar,
      status: 'done',
      thumbUrl: `${envConfig.static}${userAvatar}`,
      url: `${envConfig.static}${userAvatar}`,
      fileUrl: userAvatar
    };
    uploadFiles.push(file);
    fileUploadRef.value.initFiles(uploadFiles);
  }
}

const onFinish = (files: any) => {
  if (!files || files.length <= 0) {
    formValue.value.userAvatar = undefined;
    return;
  }
  formValue.value.userAvatar = files[0].url;
};

const urls = {
  addOrEdit: `sys/user/addOrEdit`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.User = {
        id: formValue.value.id,
        userNo: formValue.value.userNo,
        userName: formValue.value.userName,
        userEmail: formValue.value.userEmail,
        userSex: formValue.value.userSex,
        userRoles: formValue.value.selUserRoles.join(','),
        selUserRoles: [],
        countryNum: formValue.value.countryNum,
        userPhone: formValue.value.userPhone,
        userAvatar: formValue.value.userAvatar,
        userAddress: formValue.value.userAddress,
        userPost: formValue.value.userPost,
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
onMounted(() => {
  nextTick(() => {
    renderFileUpload();
  });
});
defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
