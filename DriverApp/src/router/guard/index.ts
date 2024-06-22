import type { Router } from 'vue-router';
import { closeToast, showLoadingToast } from 'vant';
import { useTitle } from '@vueuse/core';
import { createPermissionGuard } from './permission';
/**
 * RouterGuard function
 * @param router - routing instance
 */
export function createRouterGuard(router: Router) {
	router.beforeEach(async (to, from, next) => {
		// start loadingBar
		showLoadingToast({
			forbidClick: true,
		});
		// Page jump permission processing
		await createPermissionGuard(to, from, next);
	});
	router.afterEach((to) => {
		// set document title
		useTitle(to.meta.title);
		// end loadingBar
		closeToast();
	});
}
