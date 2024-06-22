import type { RouteRecordRaw } from 'vue-router';

/**
 * Get the name of the component corresponding to the cached route
 * @param routes - The converted vue route
 */
export function getCacheRoutes(routes: RouteRecordRaw[]) {
	const cacheNames: string[] = [];
	routes.forEach((route) => {
		// Only need to get the component name of the cache of the secondary route
		if (hasChildren(route)) {
			(route.children as RouteRecordRaw[]).forEach((item) => {
				if (isKeepAlive(item)) {
					cacheNames.push(item.name as string);
				}
			});
		}
	});
	return cacheNames;
}

/**
 * Whether the route is cached
 * @param route
 */
function isKeepAlive(route: RouteRecordRaw) {
	return Boolean(route?.meta?.keepAlive);
}
/**
 * Is there a secondary route
 * @param route
 */
function hasChildren(route: RouteRecordRaw) {
	return Boolean(route.children && route.children.length);
}
