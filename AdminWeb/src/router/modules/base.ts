const sys: AuthRoute.Route = {
  name: 'base',
  path: '/base',
  component: 'basic',
  children: [
    {
      name: 'base_dict',
      path: '/base/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin'],
        requiresAuth: true,
        icon: 'material-symbols:menu-book'
      }
    }
    // {
    //   name: 'base_config',
    //   path: '/base/config',
    //   component: 'self',
    //   meta: {
    //     title: '基礎設定',
    //     // permissions: ['SUPER', 'ADMIN'],
    //     requiresAuth: true,
    //     icon: 'material-symbols:settings-outline'
    //   }
    // },
    // {
    //   name: 'base_area',
    //   path: '/base/area',
    //   component: 'self',
    //   meta: {
    //     title: '地域',
    //     // permissions: ['SUPER', 'ADMIN'],
    //     requiresAuth: true,
    //     icon: 'ri:road-map-line'
    //   }
    // }
  ],
  meta: {
    title: '基礎データ',
    permissions: ['Super', 'Admin'],
    requiresAuth: true,
    icon: 'material-symbols:dataset',
    order: 8
  }
};

export default sys;
