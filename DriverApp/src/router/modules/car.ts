const car: AuthRoute.Route = {
	name: 'car',
	path: '/car',
	component: 'basic',
	children: [
		{
			name: 'car_list',
			path: '/car/list',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'car_details',
			path: '/car/details',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'car_report',
			path: '/car/report',
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

export default car;
