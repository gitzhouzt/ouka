const driver: AuthRoute.Route = {
  name: 'driver',
  path: '/driver',
  component: 'basic',
  children: [
    {
      name: 'driver_master',
      path: '/driver/master',
      component: 'self',
      meta: {
        title: 'ドライバー',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver'],
        requiresAuth: true,
        icon: 'healthicons:truck-driver'
      }
    },
    {
      name: 'driver_log',
      path: '/driver/log',
      component: 'self',
      meta: {
        title: '仕事日記',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver'],
        requiresAuth: true,
        icon: 'mdi:book-edit'
      }
    },
    {
      name: 'driver_train',
      path: '/driver/train',
      component: 'self',
      meta: {
        title: 'トレーニング',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver'],
        requiresAuth: true,
        icon: 'mdi:book-edit'
      }
    },

    {
      name: 'driver_dict',
      path: '/driver/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin', 'Operator'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    }
  ],
  meta: {
    title: 'ドライバー管理',
    permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver'],
    icon: 'healthicons:truck-driver',
    order: 5
  }
};

export default driver;
