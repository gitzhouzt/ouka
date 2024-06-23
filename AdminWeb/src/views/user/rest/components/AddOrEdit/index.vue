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
          <n-form
            ref="formRef"
            label-placement="left"
            :label-width="100"
            :model="formValue"
            :rules="rules"
            :size="size"
          >
            <n-grid :cols="2"
              ><n-gi :span="2">
                <n-form-item label="スタッフ" path="userName">
                  <n-input-group>
                    <n-input
                      v-model:value="formValue.userName"
                      readonly
                      placeholder="クリックスタッフを選択"
                      :disabled="!!formValue.id"
                      @click="showStaff()"
                    ></n-input>
                    <n-input-group-label>番号</n-input-group-label>
                    <n-input v-model:value="formValue.userNo" :disabled="true" placeholder="ドライバーを選択して表示" />
                  </n-input-group> </n-form-item></n-gi
              ><n-gi :span="2">
                <n-form-item label="休暇タイプ" path="restType">
                  <n-input
                    v-model:value="formValue.restType"
                    placeholder="クリックタイプを選択"
                    readonly
                    @click="showDict('user_rest_type')"
                  /> </n-form-item></n-gi
              ><n-gi>
                <n-form-item label="開始日時" path="startTime">
                  <n-date-picker
                    v-model:value="formValue.startTime"
                    type="date"
                    format="yyyy/MM/DD"
                    clearable /></n-form-item></n-gi
              ><n-gi>
                <n-form-item label="終了日時" path="endTime">
                  <n-date-picker
                    v-model:value="formValue.endTime"
                    type="date"
                    format="yyyy/MM/DD"
                    clearable /></n-form-item></n-gi
              ><n-gi :span="2">
                <n-form-item label="備考" path="remark">
                  <n-input-group>
                    <n-input v-model:value="formValue.remark" placeholder="備考" />
                  </n-input-group> </n-form-item
              ></n-gi>
            </n-grid>
          </n-form>
          <n-divider />
          <n-space justify="center">
            <n-button type="primary" ghost @click="close">Cancel</n-button>
            <n-button type="primary" @click="handleValidateClick">保存</n-button>
          </n-space>
        </n-spin>
      </n-card>
      <staff-select-modal ref="staffModal" @click="selectStaff" />
      <dict-select-modal ref="dictModal" @click="selectDict" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FormInst, useMessage, useLoadingBar } from 'naive-ui';
import { EnumUserRole } from '@/enum';
import { request } from '@/service/request';

const emits = defineEmits(['close']);
const bodyStyleRef = ref({
  width: '1000px'
});
const formRef = ref<FormInst | null>(null);
const size = ref<'small' | 'medium' | 'large'>('medium');
const message = useMessage();
const loadingBar = useLoadingBar();
const formValue = ref<MyModel.UserRest>({
  id: '',
  userId: '',
  userNo: '',
  userName: '',
  userRoles: '',
  startTime: new Date().getTime(),
  endTime: new Date().getTime(),
  restType: '',
  restTypeCode: '',
  remark: ''
});
const rules = {
  userName: {
    required: true,
    message: 'スタッフを選択してください',
    trigger: 'input'
  },
  restType: {
    required: true,
    message: 'タイプを選択してください',
    trigger: 'change'
  }
};

const showModalRef = ref<boolean | undefined>(false);
const disabledRole = ref(false);
const showModal = (row: MyModel.UserRest | undefined) => {
  showModalRef.value = true;
  if (row) {
    // 利用者
    formValue.value.id = row?.id;
    formValue.value.userId = row?.userId;
    formValue.value.userNo = row?.userNo;
    formValue.value.userName = row?.userName;
    formValue.value.restType = row?.restType;
    formValue.value.restTypeCode = row?.restTypeCode;
    formValue.value.startTime = new Date(row?.startTime ?? new Date()).getTime();
    formValue.value.endTime = new Date(row?.endTime ?? new Date()).getTime();
    formValue.value.remark = row?.remark;

    // 役職
    const { userRoles } = formValue.value;
    if (userRoles) {
      const roles = userRoles.split(',') as MyEnumType.EnumUserRoleKey[];
      roles.forEach(role => {
        if (EnumUserRole[role] === EnumUserRole.Admin || EnumUserRole[role] === EnumUserRole.Super) {
          disabledRole.value = true;
        }
      });
    }
  } else {
    formValue.value = {
      id: '',
      userId: '',
      userNo: '',
      userName: '',
      userRoles: '',
      startTime: new Date().getTime(),
      endTime: new Date().getTime(),
      restType: '',
      restTypeCode: '',
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

const urls = {
  rest: `user/rest`
};
const loading = ref<boolean>(false);
const handleValidateClick = (e: MouseEvent) => {
  e.preventDefault();
  formRef.value?.validate(errors => {
    if (!errors) {
      const params: MyModel.UserRest = {
        id: formValue.value.id,
        userId: formValue.value.userId,
        userNo: formValue.value.userNo,
        userName: formValue.value.userName,
        userRoles: formValue.value.userRoles,
        startTime: formValue.value.startTime,
        endTime: formValue.value.endTime,
        restType: formValue.value.restType,
        restTypeCode: formValue.value.restTypeCode,
        remark: formValue.value.remark
      };
      const promise = request.post<Boolean>(`${urls.rest}`, params);
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

const staffModal = ref<any>(null);
const showStaff = () => {
  staffModal.value?.showModal();
};
const selectStaff = (result: any) => {
  formValue.value.userId = result.id;
  formValue.value.userName = result.userName;
  formValue.value.userNo = result.userNo;
  formValue.value.userRoles = result.user.userRoles;
};

const dictModal = ref<any>(null);
const showDict = (typeCode: string) => {
  dictModal.value?.showModal();
  dictModal.value?.setDictCode(typeCode);
};
const selectDict = (result: any) => {
  switch (result.type) {
    case 'user_rest_type':
      formValue.value.restType = result.text;
      formValue.value.restTypeCode = result.value;
      break;
    default:
      break;
  }
};

defineExpose({
  showModal,
  setTitle
});
</script>

<style scoped></style>
