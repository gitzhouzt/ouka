<template>
  <n-dropdown :options="options" @select="handleDropdown">
    <hover-container class="px-12px" :inverted="theme.header.inverted">
      <icon-custom-avatar class="text-32px" />
      <span class="pl-8px text-16px font-medium">{{ auth.userInfo.userName }} <br />{{ auth.userInfo.userEmail }}</span>
    </hover-container>
  </n-dropdown>
  <reset-pwd-modal ref="restPwd" />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useAuthStore, useThemeStore } from '@/store';
import { iconifyRender } from '@/utils';
import { fetchLogout } from '@/service/api';

type DropdownKey = 'user-center' | 'logout' | 'rest-pwd';

const auth = useAuthStore();
const theme = useThemeStore();

const options = [
  // {
  //   label: 'マイページ',
  //   key: 'user-center',
  //   icon: iconifyRender('carbon:user-avatar')
  // },
  {
    label: 'パスワード変更',
    key: 'rest-pwd',
    icon: iconifyRender('material-symbols:lock')
  },
  {
    type: 'divider',
    key: 'divider'
  },
  {
    label: 'ログアウト',
    key: 'logout',
    icon: iconifyRender('carbon:logout')
  }
];

const restPwd = ref<any>(null);
const handleRestPwd = () => {
  restPwd.value?.showModal();
};

function handleDropdown(optionKey: string) {
  const key = optionKey as DropdownKey;
  if (key === 'logout') {
    window.$dialog?.info({
      title: 'ログアウト',
      content: 'ログアウトでよろしいですか？',
      positiveText: 'OK',
      negativeText: 'Cancel',
      onPositiveClick: async () => {
        await fetchLogout();
        auth.resetAuthStoreToHome();
      }
    });
  } else if (key === 'rest-pwd') {
    handleRestPwd();
  }
}
</script>
<style scoped></style>
