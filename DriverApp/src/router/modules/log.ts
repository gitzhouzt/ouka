const log: AuthRoute.Route = {
	name: 'log',
	path: '/log',
	component: 'basic',
	children: [
		{
			name: 'log_list',
			path: '/log/list',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'log_details',
			path: '/log/details',
			component: 'self',
			meta: {
				title: 'ドライバーアプリ',
				// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
				requiresAuth: true,
				icon: 'material-symbols:share',
			},
		},
		{
			name: 'log_write',
			path: '/log/write',
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

export default log;
