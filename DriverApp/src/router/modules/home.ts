const home: AuthRoute.Route = {
	name: 'home',
	path: '/home',
	component: 'self',
	meta: {
		title: 'ドライバーアプリ',
		// permissions: ['SUPER', 'ADMIN', 'DRIVER'],
		requiresAuth: true,
		icon: 'material-symbols:share',
		order: 1,
	},
};

export default home;
