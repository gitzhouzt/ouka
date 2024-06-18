const sys: AuthRoute.Route = {
  name: 'sys',
  path: '/sys',
  component: 'basic',
  children: [],
  meta: {
    title: 'システム管理',
    permissions: ['Super', 'Admin'],
    hide: true,
    icon: 'material-symbols:settings-outline',
    order: 9
  }
};

export default sys;
