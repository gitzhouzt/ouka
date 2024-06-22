const order: AuthRoute.Route = {
	name: 'order',
	path: '/order',
	component: 'basic',
	children: [
		{
			name: 'order_list',
			path: '/order/list',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'order_details',
			path: '/order/details',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'order_advance',
			path: '/order/advance',
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

export default order;
