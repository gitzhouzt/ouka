/** Permission Routing Related Types */
declare namespace AuthRoute {
	/** Multi-level routing split notation */
	type RouteSplitMark = '_';

	/** routing key */
	type RouteKey =
		// fixed route
		| 'root' // root route
		| 'login'
		| 'not-found'
		| 'no-permission'
		| 'service-error'
		| 'not-found-page' // Catch routes with invalid paths
		// custom routing
		| 'home'
		| 'order'
		| 'order_list'
		| 'order_details'
		| 'order_advance'
		| 'call'
		| 'call_details'
		| 'car'
		| 'car_list'
		| 'car_details'
		| 'car_report'
		| 'log'
		| 'log_list'
		| 'log_details'
		| 'log_write'
		| 'mypage'
		| 'mypage_reset'
		| 'mypage_details'
		| 'log'
		| 'log_list'
		| 'log_details'
		| 'checkIn'
		| 'checkIn_list'
		| 'checkIn_check';

	/** RoutePath */
	type RoutePath =
		| '/'
		| Exclude<KeyToPath<RouteKey>, '/root' | 'not-found-page'>
		| SingleRouteParentPath
		| '/:pathMatch(.*)*';

	/**
	 * RouteComponent
	 * - basic - basic layout
	 * - blank - blank layout
	 * - multi - multi route layout(For the third-level routing or above, except the first-level routing and the last-level routing, the rest use this layout)
	 * - self - As a sub-route, use its own layout (as the last-level route, no sub-routes)
	 */
	type RouteComponent = 'basic' | 'multi' | 'self';

	/** Route Meta */
	type RouteMeta = {
		/** title(Can be used as document.title or the name of the menu) */
		title: string;
		/** The dynamic path of the route (pages that require a dynamic path need to add the path to the generic parameter) */
		dynamicPath?: PathToDynamicPath<'/login'>;
		/** Route layout component as parent of single level route */
		singleLayout?: Extract<RouteComponent, 'basic'>;
		/** Login permission required */
		requiresAuth?: boolean;
		/**
		 * Which types of routes can only be accessed by users with permissions (empty means no permissions)
		 * @description The back-end dynamic routing data does not need this attribute, and the back-end directly returns the routing data of the corresponding permissions according to the user role.
		 */
		permissions?: Auth.RoleType[];
		/** cache page */
		keepAlive?: boolean;
		/** Icons for menus and breadcrumbs */
		icon?: string;
		/** Whether to hide in the menu (the details pages of some lists and tables need to be jumped by parameters, so they cannot be displayed in the menu) */
		hide?: boolean;
		/** External link */
		href?: string;
		/** Routing order, which can be used to sort the menu */
		order?: number;
		/** Indicates whether it is an intermediate-level route of multi-level routing (the identifier used to filter multi-level routing when converting routing data, and does not need to be filled in when defining a route) */
		multi?: boolean;
		/** The menu item that needs to be selected for the current route (used to jump to a route that is not displayed on the left menu and a menu needs to be highlighted) */
		activeMenu?: RouteKey;
	};

	/** The type structure of a single route (dynamic routing mode: the backend returns a route of this type structure) */
	interface Route {
		/** Route name (route unique identifier) */
		name: RouteKey;
		/** routing path */
		path: RoutePath;
		/** route redirection */
		redirect?: RoutePath;
		/**
		 * RouteComponent
		 * - basic - basic layout
		 * - blank - blank layout
		 * - multi - multi route layout(For the third-level routing or above, except the first-level routing and the last-level routing, the rest use this layout)
		 * - self - As a sub-route, use its own layout (as the last-level route, no sub-routes)
		 */
		component?: RouteComponent;
		/** sub route */
		children?: Route[];
		/** RouteMeta */
		meta: RouteMeta;
		/** route props */
		props?: boolean | Record<string, any> | ((to: any) => Record<string, any>);
	}

	/** Front-end imported routing module */
	type RouteModule = Record<string, { default: AuthRoute.Route }>;

	/** The key of a separate first-level route (a separate route needs to add a parent route to apply the layout component) */
	type SingleRouteKey = Exclude<
		GetSingleRouteKey<RouteKey>,
		GetRouteFirstParentKey<RouteKey> | 'root' | 'not-found-page'
	>;
	/** Route the parent routing key separately */
	type SingleRouteParentKey = `${SingleRouteKey}-parent`;

	/** Separate route parent route path */
	type SingleRouteParentPath = KeyToPath<SingleRouteParentKey>;

	/** Routing key converts routing path */
	type KeyToPath<Key extends string> =
		Key extends `${infer Left}_${infer Right}`
			? KeyToPath<`${Left}/${Right}`>
			: `/${Key}`;

	/** Routing path conversion dynamic path */
	type PathToDynamicPath<Path extends RoutePath> =
		| `${Path}/:${string}`
		| `${Path}/:${string}(${string})`
		| `${Path}/:${string}(${string})?`;

	/** Obtain first-level routes (including first-level routes with sub-routes) */
	type GetSingleRouteKey<Key extends RouteKey> =
		Key extends `${infer _Left}${RouteSplitMark}${infer _Right}` ? never : Key;

	/** Get the first-level parent route of a child route */
	type GetRouteFirstParentKey<Key extends RouteKey> =
		Key extends `${infer Left}${RouteSplitMark}${infer _Right}` ? Left : never;
}
