const mypage: AuthRoute.Route = {
	name: 'mypage',
	path: '/mypage',
	component: 'basic',
	children: [
		{
			name: 'mypage_details',
			path: '/mypage/details',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'mypage_reset',
			path: '/mypage/reset',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
	],
	meta: {
		title: 'ドライバーアプリ',
		// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
		requiresAuth: true,
		icon: 'material-symbols:share',
		order: 1,
	},
};

export default mypage;
