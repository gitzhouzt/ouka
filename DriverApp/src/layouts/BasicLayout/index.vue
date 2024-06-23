<template>
	<van-sticky :offset-top="0">
		<van-nav-bar :title="title">
			<template #left>
				<van-icon v-if="!isLogin && !isHome" color="#fb8c00" name="arrow-left" size="22" @click="routerBack" />
			</template>
			<template #right>
				<div v-if="!isLogin">
					<van-icon v-if="!isHome" color="#fb8c00" name="wap-home-o" size="22" @click="toHome(false)" />
					<van-icon v-else class="ml-10px" color="#fb8c00" name="cross" size="22" @click="logout" />
				</div>
			</template>
		</van-nav-bar>
	</van-sticky>
	<router-view v-slot="{ Component, route }">
		<transition name="fade" mode="out-in" :appear="true" @before-leave="handleBeforeLeave"
			@after-enter="handleAfterEnter">
			<keep-alive :include="routeStore.cacheRoutes">
				<component :is="Component" v-if="app.reloadFlag" :key="route.path" />
			</keep-alive>
		</transition>
	</router-view>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { showConfirmDialog } from 'vant';
import { routeName } from '@/router';
import { useAuthStore, useAppStore, useRouteStore } from '@/store';
import { useRouterPush } from '@/composables';
import { fetchLogout } from '@/service/api';

interface Emits {
	/** hide main overflow */
	(e: 'hide-main-overflow', hidden: boolean): void;
}

const emit = defineEmits<Emits>();

const app = useAppStore();
const routeStore = useRouteStore();

function handleBeforeLeave() {
	emit('hide-main-overflow', true);
}
function handleAfterEnter() {
	emit('hide-main-overflow', false);
}

const auth = useAuthStore();
const { toHome, routerBack } = useRouterPush(false);
const title = ref();
const logout = () => {
	showConfirmDialog({
		title: 'ログアウト',
		message: 'ログアウトでよろしいですか？',
	})
		.then(async () => {
			await fetchLogout();
			auth.resetAuthStoreToHome();
		})
		.catch(() => {
			//
		});
};
const isHome = ref(false);
const isLogin = ref(false);
const router = useRouter();
const showNavbar = () => {
	isHome.value = false;
	isLogin.value = false;
	const currentRoute = router.currentRoute.value;
	switch (currentRoute.name) {
		case routeName('login'):
			isLogin.value = true;
			break;
		case routeName('home'):
			isHome.value = true;
			break;
		default:
			break;
	}
	title.value = currentRoute.meta.title;
};
watch(
	() => router.currentRoute.value.name,
	() => showNavbar(),
	{ immediate: true, deep: true }
);
</script>
<style scoped></style>
