<template>
	<router-view v-slot="{ Component, route }">
		<transition
			name="pageAnimateMode"
			mode="out-in"
			:appear="true"
			@before-leave="handleBeforeLeave"
			@after-enter="handleAfterEnter"
		>
			<keep-alive :include="routeStore.cacheRoutes">
				<component :is="Component" v-if="app.reloadFlag" :key="route.path" />
			</keep-alive>
		</transition>
	</router-view>
</template>

<script setup lang="ts">
import { useAppStore, useRouteStore } from '@/store';

interface Props {
	/** show padding */
	showPadding?: boolean;
}

interface Emits {
	/** hide main overflow */
	(e: 'hide-main-overflow', hidden: boolean): void;
}

withDefaults(defineProps<Props>(), {
	showPadding: true,
});

const emit = defineEmits<Emits>();

const app = useAppStore();
const routeStore = useRouteStore();

function handleBeforeLeave() {
	emit('hide-main-overflow', true);
}
function handleAfterEnter() {
	emit('hide-main-overflow', false);
}
</script>
<style scoped></style>
