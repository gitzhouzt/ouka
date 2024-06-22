import { useRouter } from 'vue-router';
import type { RouteLocationRaw } from 'vue-router';
import { router as globalRouter, routeName } from '@/router';

/**
 * route jump
 * @param inSetup - Whether it is called in the setup of the vue page/component, useRouter and useRoute cannot be used in axios
 */
export function useRouterPush(inSetup = true) {
	const router = inSetup ? useRouter() : globalRouter;
	const route = globalRouter.currentRoute;

	/**
	 * route jump
	 * @param to - Routes that need to be jumped
	 * @param newTab - Whether to open in a new browser tab
	 */
	function routerPush(to: RouteLocationRaw, newTab = false) {
		if (newTab) {
			const routerData = router.resolve(to);
			window.open(routerData.href, '_blank');
		} else {
			router.push(to);
		}
	}

	/** Return to the previous route */
	function routerBack() {
		router.go(-1);
	}

	/**
	 * Jump to Home
	 * @param newTab - Opens in a new browser tab
	 */
	function toHome(newTab = false) {
		routerPush({ name: routeName('root') }, newTab);
	}

	/**
	 * Jump to login page
	 * @param loginModule - Displayed login module
	 * @param redirectUrl - Redirection address (address to jump after successful login), the default undefined means to take the current address as the redirection address
	 */
	function toLogin(
		loginModule?: EnumType.LoginModuleKey,
		redirectUrl?: string
	) {
		const module: EnumType.LoginModuleKey = loginModule || 'staff-login';
		const routeLocation: RouteLocationRaw = {
			name: routeName('login'),
			params: { module },
		};
		const redirect = redirectUrl || route.value.fullPath;
		Object.assign(routeLocation, { query: { redirect } });
		routerPush(routeLocation);
	}

	/**
	 * Login page to switch other modules
	 * @param module - Switched login module
	 */
	function toLoginModule(module: EnumType.LoginModuleKey) {
		const { query } = route.value;
		routerPush({ name: routeName('login'), params: { module }, query });
	}

	/**
	 * The redirected address after successful login
	 */
	function toLoginRedirect() {
		const { query } = route.value;

		if (query?.redirect) {
			routerPush(query.redirect as string);
		} else {
			toHome();
		}
	}

	return {
		routerPush,
		routerBack,
		toHome,
		toLogin,
		toLoginModule,
		toLoginRedirect,
	};
}
