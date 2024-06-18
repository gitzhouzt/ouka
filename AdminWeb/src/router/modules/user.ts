const user: AuthRoute.Route = {
  name: 'user',
  path: '/user',
  component: 'basic',
  children: [
    {
      name: 'user_master',
      path: '/user/master',
      component: 'self',
      meta: {
        title: 'スタッフ',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:person'
      }
    },
    // {
    //   name: 'user_schedule',
    //   path: '/user/schedule',
    //   component: 'self',
    //   meta: {
    //     title: 'スケジュール',
    //     // permissions: ['ADMIN', 'STAFF', 'user'],
    //     requiresAuth: true,
    //     icon: 'material-symbols:person'
    //   }
    // },
    {
      name: 'user_rest',
      path: '/user/rest',
      component: 'self',
      meta: {
        title: '休暇',
        permissions: ['Super', 'Admin', 'Operator', 'Accounting', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:person'
      }
    },
    {
      name: 'user_dict',
      path: '/user/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    }
  ],
  meta: {
    title: 'スタッフ管理',
    permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'Accounting', 'OperationManager'],
    icon: 'material-symbols:person',
    order: 7
  }
};

export default user;
