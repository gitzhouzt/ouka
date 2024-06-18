<template>
  <div>
    <n-card title="ドライバー日程" class="h-full shadow-sm rounded-16px">
      <n-space :vertical="true">
        <n-space>
          <n-form :inline="!isMobile" :label-width="100" label-placement="left">
            <n-form-item label="キーワード">
              <n-input
                v-model:value="searchParams.keyword"
                style="min-width: 30%"
                type="text"
                placeholder="番号/名前"
                clearable
              />
            </n-form-item>
            <n-form-item>
              <n-button
                type="primary"
                @click="
                  () => {
                    searchQuery();
                  }
                "
                >検索</n-button
              >
              <n-button class="ml-2" @click="searchReset">リセット</n-button>
            </n-form-item>
          </n-form>
        </n-space>
        <n-space>
          <!-- <n-button type="primary" @click="handleAdd('スタッフ登録')">スタッフ登録</n-button> -->
        </n-space>
        <n-spin :show="loading">
          <n-calendar
            v-model:value="value"
            #="{ year, month, date }"
            :is-date-disabled="isDateDisabled"
            @panel-change="handlePanelChange"
            @update:value="handleUpdateValue"
          >
            <div v-html="show({ year, month, date })"></div>
          </n-calendar>
        </n-spin>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { NButton, useLoadingBar, useMessage } from 'naive-ui';
import { isYesterday, addDays } from 'date-fns/esm';
import { useMyCommon } from '@/composables';
import { request } from '@/service/request';

const { isMobile } = useMyCommon();
const loading = ref(false);

const searchParams = ref({
  keyword: '',
  date: `${new Date().getFullYear()}-${
    new Date().getMonth() + 1 < 10 ? `0${new Date().getMonth() + 1}` : new Date().getMonth() + 1
  }`
});

const value = ref(addDays(Date.now(), 1).valueOf());
const isDateDisabled = (timestamp: number) => {
  if (isYesterday(timestamp)) {
    return true;
  }
  return false;
};

const schedule = ref<MyModel.Schedule[]>([]);
const message = useMessage();
const loadingBar = useLoadingBar();
const urls = {
  schedule: `user/findByMonth`
};
const show = ({ year, month, date }: { year: number; month: number; date: number }) => {
  const m = month < 10 ? `0${month}` : month;
  const list = schedule.value.filter(i => i.date === `${year}-${m}-${date}`);
  let str = '';
  list.forEach(s => {
    let color = '';
    if (s.details.indexOf('検') >= 0) {
      color = 'red';
    }
    str += `<span style='color:${color};'>${s.details}</span><br/>`;
  });
  return str;
};

const searchQuery = () => {
  console.debug(searchParams);
  const promise = request.post<MyModel.Schedule[]>(`${urls.schedule}`, searchParams.value);
  loadingBar.start();
  loading.value = true;
  promise
    .then(res => {
      if (res.data) {
        const { data } = res;
        schedule.value = data;
      }
    })
    .catch(err => {
      message.warning(err);
    })
    .finally(() => {
      loading.value = false;
      loadingBar.finish();
    });
};
const searchReset = () => {
  searchParams.value = {
    keyword: '',
    date: `${new Date().getFullYear()}-${new Date().getMonth() + 1}`
  };
  searchQuery();
};

const handlePanelChange = (info: { year: number; month: number }) => {
  const m = info.month < 10 ? `0${info.month}` : info.month;
  searchParams.value.date = `${info.year}-${m}`;
  searchQuery();
};
const handleUpdateValue = (_: number, { year, month, date }: { year: number; month: number; date: number }) => {
  // 点击事件
  console.debug(`点击${year}`);
};

onMounted(() => {
  searchQuery();
});
</script>
<style scoped></style>
