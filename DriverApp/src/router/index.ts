import type { App } from 'vue';
import {
	createRouter,
	createWebHistory,
	createWebHashHistory,
} from 'vue-router';
import {
	transformAuthRoutesToVueRoutes,
	transformRouteNameToRoutePath,
} from '@/utils';
import { constantRoutes } from './routes';
import { createRouterGuard } from './guard';

const { VITE_HASH_ROUTE = 'false', VITE_BASE_URL } = import.meta.env;

export const router = createRouter({
	history:
		VITE_HASH_ROUTE === 'true'
			? createWebHashHistory(VITE_BASE_URL)
			: createWebHistory(VITE_BASE_URL),
	routes: transformAuthRoutesToVueRoutes(constantRoutes),
});

/** setup vue router. - */
export async function setupRouter(app: App) {
	app.use(router);
	createRouterGuard(router);
	await router.isReady();
}

/** routeName */
export const routeName = (key: AuthRoute.RouteKey) => key;
/** routePath */
export const routePath = (key: Exclude<AuthRoute.RouteKey, 'not-found-page'>) =>
	transformRouteNameToRoutePath(key);

export * from './routes';
export * from './modules';
