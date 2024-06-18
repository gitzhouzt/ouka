const order: AuthRoute.Route = {
  name: 'order',
  path: '/order',
  component: 'basic',
  children: [
    {
      name: 'order_master',
      path: '/order/master',
      component: 'self',
      meta: {
        title: '注文',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    },
    {
      name: 'order_deploy',
      path: '/order/deploy',
      component: 'self',
      meta: {
        title: '配車',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    },
    {
      name: 'order_dict',
      path: '/order/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    }
    // {
    //   name: 'order_template',
    //   path: '/order/template',
    //   component: 'self',
    //   meta: {
    //     title: '看板テンプレート',
    //     // permissions: ['ADMIN', 'STAFF', 'DRIVER'],
    //     requiresAuth: true,
    //     icon: 'material-symbols:library-books-outline'
    //   }
    // }
  ],
  meta: {
    title: '注文管理',
    permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
    icon: 'material-symbols:important-devices-rounded',
    order: 2
  }
};

export default order;
