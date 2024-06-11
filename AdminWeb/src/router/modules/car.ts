const sys: AuthRoute.Route = {
  name: 'car',
  path: '/car',
  component: 'basic',
  children: [
    {
      name: 'car_master',
      path: '/car/master',
      component: 'self',
      meta: {
        title: '車両',
        permissions: ['Super', 'Admin', 'Operator', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:directions-car-outline'
      }
    },
    {
      name: 'car_check',
      path: '/car/check',
      component: 'self',
      meta: {
        title: '車両点検',
        permissions: ['Super', 'Admin', 'Operator', 'OperationManager'],
        requiresAuth: true,
        icon: 'ic:outline-car-crash'
      }
    },
    // {
    //   name: 'car_repair',
    //   path: '/car/repair',
    //   component: 'self',
    //   meta: {
    //     title: '車両修理',
    //     // permissions: ['ADMIN', 'STAFF', 'DRIVER'],
    //     requiresAuth: true,
    //     icon: 'mdi:hammer-screwdriver'
    //   }
    // },
    {
      name: 'car_dict',
      path: '/car/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin', 'Operator', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:directions-car-outline'
      }
    }
  ],
  meta: {
    title: '車両管理',
    permissions: ['Super', 'Admin', 'Operator', 'OperationManager'],
    icon: 'material-symbols:directions-car-outline',
    order: 4
  }
};

export default sys;
