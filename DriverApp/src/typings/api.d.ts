// The data type returned by the backend interface

/** User Auth types returned by the backend */
declare namespace ApiAuth {
	interface LoginUser {
		tokenVO: Token;
		userVO: Auth.UserInfo;
	}

	/** Returned token and refresh token */
	interface Token {
		token: string;
		tokenRefresh: string;
	}
}

/** Route-related types returned by the backend */
declare namespace ApiRoute {
	/** The route data type returned by the backend */
	interface Route {
		/** dynamic routing */
		routes: AuthRoute.Route[];
		/** The key corresponding to the routing home page */
		home: AuthRoute.RouteKey;
	}
}

declare namespace ApiDemo {
	interface DataWithAdapter {
		dataId: string;
		dataName: string;
	}
}
