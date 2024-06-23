import { iconifyRender } from '../common';

/** Routing does not convert menus */
function hideInMenu(route: AuthRoute.Route) {
	return Boolean(route.meta.hide);
}

/** Add optional attributes to menus */
function addPartialProps(
	menuItem: GlobalMenuOption,
	icon?: string,
	children?: GlobalMenuOption[]
) {
	const item = { ...menuItem };
	if (icon) {
		Object.assign(item, { icon: iconifyRender(icon) });
	}
	if (children) {
		Object.assign(item, { children });
	}
	return item;
}

/**
 * Convert permission routes into menus
 * @param routes
 */
export function transformAuthRouteToMenu(
	routes: AuthRoute.Route[]
): GlobalMenuOption[] {
	const globalMenu: GlobalMenuOption[] = [];
	routes.forEach((route) => {
		const { name, path, meta } = route;
		const routeName = name as string;
		let menuChildren: GlobalMenuOption[] | undefined;
		if (route.children) {
			menuChildren = transformAuthRouteToMenu(route.children);
		}
		const menuItem: GlobalMenuOption = addPartialProps(
			{
				key: routeName,
				label: meta.title,
				routeName,
				routePath: path,
			},
			meta?.icon,
			menuChildren
		);

		if (!hideInMenu(route)) {
			globalMenu.push(menuItem);
		}
	});

	return globalMenu;
}

/**
 * Get the paths of the menu data where the current route is located
 * @param activeKey - the key of the current route
 * @param menus - menu data
 */
export function getActiveKeyPathsOfMenus(
	activeKey: string,
	menus: GlobalMenuOption[]
) {
	const keys = menus
		.map((menu) => getActiveKeyPathsOfMenu(activeKey, menu))
		.flat(1);
	return keys;
}

function getActiveKeyPathsOfMenu(activeKey: string, menu: GlobalMenuOption) {
	const keys: string[] = [];
	if (activeKey.includes(menu.routeName)) {
		keys.push(menu.routeName);
	}
	if (menu.children) {
		keys.push(
			...menu.children
				.map((item) => getActiveKeyPathsOfMenu(activeKey, item))
				.flat(1)
		);
	}
	return keys;
}
