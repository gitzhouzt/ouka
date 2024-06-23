const checkIn: AuthRoute.Route = {
	name: 'checkIn',
	path: '/checkIn',
	component: 'basic',
	children: [
		{
			name: 'checkIn_list',
			path: '/checkIn/list',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'checkIn_check',
			path: '/checkIn/check',
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

export default checkIn;
