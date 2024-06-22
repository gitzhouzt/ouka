/** fixed route */
export const constantRoutes: AuthRoute.Route[] = [
	{
		name: 'root',
		path: '/',
		redirect: import.meta.env.VITE_ROUTE_HOME_PATH,
		meta: {
			title: 'Root',
		},
	},
	{
		name: 'login',
		path: '/login',
		component: 'self',
		props: (route) => {
			const moduleType =
				(route.params.module as EnumType.LoginModuleKey) || 'pwd-login';
			return {
				module: moduleType,
			};
		},
		meta: {
			title: 'ドライバーアプリ',
			singleLayout: 'basic',
		},
	},
	{
		name: 'no-permission',
		path: '/no-permission',
		component: 'self',
		meta: {
			title: '許可なし',
			singleLayout: 'basic',
		},
	},
	{
		name: 'not-found',
		path: '/not-found',
		component: 'self',
		meta: {
			title: '見つからない',
			singleLayout: 'basic',
		},
	},
	{
		name: 'service-error',
		path: '/service-error',
		component: 'self',
		meta: {
			title: 'サーバーエラー',
			singleLayout: 'basic',
		},
	},
	// 無効なパスに一致するルート
	{
		name: 'not-found-page',
		path: '/:pathMatch(.*)*',
		component: 'self',
		meta: {
			title: '見つからない',
			singleLayout: 'basic',
		},
	},
];
