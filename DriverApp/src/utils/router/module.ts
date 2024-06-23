/**
 * Permission Routing Sort
 * @param routes - Authority routing
 */
function sortRoutes(routes: AuthRoute.Route[]) {
	return routes.sort(
		(next, pre) => Number(next.meta?.order) - Number(pre.meta?.order)
	);
}

/**
 * Process all imported routing modules
 * @param modules - routing module
 */
export function handleModuleRoutes(modules: AuthRoute.RouteModule) {
	const routes: AuthRoute.Route[] = [];

	Object.keys(modules).forEach((key) => {
		const item = modules[key].default;
		if (item) {
			routes.push(item);
		} else {
			window.console.error(`Routing module parsing error: key = ${key}`);
		}
	});

	return sortRoutes(routes);
}
