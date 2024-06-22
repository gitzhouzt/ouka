import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router';
import { routeName } from '@/router';
import { useAuthStore } from '@/store';
import { exeStrategyActions, getToken } from '@/utils';
import { createDynamicRouteGuard } from './dynamic';

/** Permission to handle routing pages */
export async function createPermissionGuard(
	to: RouteLocationNormalized,
	from: RouteLocationNormalized,
	next: NavigationGuardNext
) {
	// dynamic routing
	const permission = await createDynamicRouteGuard(to, from, next);
	if (!permission) return;

	// Outbound route, open in new tab, return to previous route
	if (to.meta.href) {
		window.open(to.meta.href);
		next({ path: from.fullPath, replace: true, query: from.query });
		return;
	}

	const auth = useAuthStore();
	const isLogin = Boolean(getToken());
	const permissions = to.meta.permissions || [];
	const needLogin =
		Boolean(to.meta?.requiresAuth) || Boolean(permissions.length);

	const ps = auth.userInfo.userRoles.split(',');
	let flag = false;
	ps?.forEach((p) => {
		to.meta.permissions?.forEach((pp) => {
			if (p === pp) {
				flag = true;
			}
		});
	});
	const hasPermission = !permissions.length || flag;

	const actions: Common.StrategyAction[] = [
		// Logged in status Jump to the login page, jump to the home page
		[
			isLogin && to.name === routeName('login'),
			() => {
				next({ name: routeName('root') });
			},
		],
		// Direct access to pages that do not require login permissions
		[
			!needLogin,
			() => {
				next();
			},
		],
		// Not logged in to enter the page that requires login permission
		[
			!isLogin && needLogin,
			() => {
				const redirect = to.fullPath;
				next({ name: routeName('login'), query: { redirect } });
			},
		],
		// Login status Enter the page that requires login permission, and have permission to pass directly
		[
			isLogin && needLogin && hasPermission,
			() => {
				next();
			},
		],
		[
			// The login state enters the page that requires login permission, no permission, redirects to the page without permission
			isLogin && needLogin && !hasPermission,
			() => {
				next({ name: routeName('no-permission') });
			},
		],
	];

	exeStrategyActions(actions);
}
