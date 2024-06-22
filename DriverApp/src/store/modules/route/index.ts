import { defineStore } from 'pinia';
import { router, constantRoutes, routes as staticRoutes } from '@/router';
import { fetchUserRoutes } from '@/service';
import {
	getUserInfo,
	transformAuthRouteToMenu,
	transformAuthRoutesToVueRoutes,
	transformAuthRoutesToSearchMenus,
	getCacheRoutes,
	filterAuthRoutesByUserPermission,
	transformRoutePathToRouteName,
	getConstantRouteNames,
} from '@/utils';
import { useAuthStore } from '../auth';

interface RouteState {
	/**
	 * Privilege Routing Mode:
	 * - static - Front-end declared static
	 * - dynamic - The dynamic returned by the backend
	 */
	authRouteMode: ImportMetaEnv['VITE_AUTH_ROUTE_MODE'];
	/** Whether the permission routing is initialized */
	isInitAuthRoute: boolean;
	/** Routing home page name (valid for front-end static routing, this value will be overwritten by the value returned by back-end dynamic routing) */
	routeHomeName: AuthRoute.RouteKey;
	/** menu */
	menus: GlobalMenuOption[];
	/** search menu */
	searchMenus: AuthRoute.Route[];
	/** cacheRoutes name */
	cacheRoutes: string[];
}

export const useRouteStore = defineStore('admin-route-store', {
	state: (): RouteState => ({
		authRouteMode: import.meta.env.VITE_AUTH_ROUTE_MODE,
		isInitAuthRoute: false,
		routeHomeName: transformRoutePathToRouteName(
			import.meta.env.VITE_ROUTE_HOME_PATH
		),
		menus: [],
		searchMenus: [],
		cacheRoutes: [],
	}),
	actions: {
		/** resetRouteStore */
		resetRouteStore() {
			this.resetRoutes();
			this.$reset();
		},
		/** Reset routing data, keep fixed routing */
		resetRoutes() {
			const routes = router.getRoutes();
			const constantRouteNames = getConstantRouteNames(constantRoutes);
			routes.forEach((route) => {
				const name: AuthRoute.RouteKey = (route.name ||
					'root') as AuthRoute.RouteKey;
				if (!constantRouteNames.includes(name)) {
					router.removeRoute(name);
				}
			});
		},
		/**
		 * Handling permission routing
		 * @param routes - Authority routing
		 */
		handleAuthRoutes(routes: AuthRoute.Route[]) {
			this.menus = transformAuthRouteToMenu(routes);
			this.searchMenus = transformAuthRoutesToSearchMenus(routes);

			const vueRoutes = transformAuthRoutesToVueRoutes(routes);

			vueRoutes.forEach((route) => {
				router.addRoute(route);
			});

			this.cacheRoutes = getCacheRoutes(vueRoutes);
		},
		/** Initialize dynamic routing */
		async initDynamicRoute() {
			const { userRoles } = getUserInfo();
			const { data } = await fetchUserRoutes(userRoles);
			if (data) {
				this.routeHomeName = data.home;
				this.handleAuthRoutes(data.routes);
			}
		},
		/** Initialize static routes */
		async initStaticRoute() {
			const auth = useAuthStore();
			const routes = filterAuthRoutesByUserPermission(
				staticRoutes,
				auth.userInfo.userRoles
			);
			this.handleAuthRoutes(routes);
		},
		/** Initialize permission routing */
		async initAuthRoute() {
			const { id } = getUserInfo();

			if (!id) return;

			const isDynamicRoute = this.authRouteMode === 'dynamic';
			if (isDynamicRoute) {
				await this.initDynamicRoute();
			} else {
				await this.initStaticRoute();
			}

			this.isInitAuthRoute = true;
		},
	},
});
