/**
 * Filter routes based on user permissions
 * @param routes - Authority routing
 * @param permission ‐ Authority
 */
export function filterAuthRoutesByUserPermission(
	routes: AuthRoute.Route[],
	permission: string
) {
	return routes
		.map((route) => filterAuthRouteByUserPermission(route, permission))
		.flat(1);
}

/**
 * Filter routes based on single user permission
 * @param route - Single Permission Routing
 * @param permission - Authority
 */
function filterAuthRouteByUserPermission(
	route: AuthRoute.Route,
	permission: string
): AuthRoute.Route[] {
	const filterRoute = { ...route };
	const ps = permission.split(',');
	let flag = false;
	ps?.forEach((p) => {
		route.meta.permissions?.forEach((pp) => {
			if (p === pp) {
				flag = true;
			}
		});
	});
	const hasPermission = !route.meta.permissions || ps.includes('SUPER') || flag;

	if (filterRoute.children) {
		const filterChildren = filterRoute.children
			.map((item) => filterAuthRouteByUserPermission(item, permission))
			.flat(1);
		Object.assign(filterRoute, { children: filterChildren });
	}
	return hasPermission ? [filterRoute] : [];
}
