import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router';
import { routeName } from '@/router';
import { useRouteStore } from '@/store';
import { getToken } from '@/utils';

/**
 * DynamicRoute
 */
export async function createDynamicRouteGuard(
	to: RouteLocationNormalized,
	_from: RouteLocationNormalized,
	next: NavigationGuardNext
) {
	const route = useRouteStore();
	const isLogin = Boolean(getToken());
	// isInitAuthRoute
	if (!route.isInitAuthRoute) {
		// If you are not logged in, go back to the login page directly,
		// and then load the permission routing after successful login.
		if (!isLogin) {
			if (to.name === routeName('login')) {
				next();
			} else {
				const redirect = to.fullPath;
				next({ name: routeName('login'), query: { redirect } });
			}
			return false;
		}

		await route.initAuthRoute();

		if (to.fullPath.indexOf('support?id=') > 0) {
			const id = to.fullPath.split('support?id=')[1];
			next({ name: routeName('staff_support'), query: { id } });
			return false;
		}

		if (to.name === routeName('not-found-page')) {
			// The dynamic route is not loaded and is captured by the not-found-page route,
			// wait for the permission route to be loaded, and return to the previous route
			// next({ path: to.fullPath, replace: true, query: to.query });
			next({ name: routeName('home') });
			return false;
		}
	}

	// Permission routing has been loaded, still not found, redirect to not-found
	if (to.name === routeName('not-found-page')) {
		// next({ name: routeName('not-found'), replace: true });
		next({ name: routeName('home') });
		return false;
	}

	return true;
}
