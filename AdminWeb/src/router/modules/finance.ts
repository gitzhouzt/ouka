const sys: AuthRoute.Route = {
  name: 'finance',
  path: '/finance',
  component: 'basic',
  children: [
    {
      name: 'finance_cashIn',
      path: '/finance/cashIn',
      component: 'self',
      meta: {
        title: '現金入金',
        permissions: ['Super', 'Admin', 'Accounting', 'Driver', 'Seller', 'OperationManager'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },
    // {
    //   name: 'finance_in',
    //   path: '/finance/in',
    //   component: 'self',
    //   meta: {
    //     title: '入金記録',
    //     permissions: ['Super', 'Admin', 'Accounting', 'Driver', 'Seller', 'OperationManager'],
    //     requiresAuth: true,
    //     icon: 'icon-park-outline:finance'
    //   }
    // },
    {
      name: 'finance_out',
      path: '/finance/out',
      component: 'self',
      meta: {
        title: '出金記録',
        permissions: ['Super', 'Admin', 'Accounting', 'Driver', 'Seller', 'OperationManager'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },
    {
      name: 'finance_mae',
      path: '/finance/mae',
      component: 'self',
      meta: {
        title: '前受金',
        permissions: ['Super', 'Admin', 'Accounting', 'Driver', 'Seller', 'OperationManager'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },

    // {
    //   name: 'finance_adv',
    //   path: '/finance/adv',
    //   component: 'self',
    //   meta: {
    //     title: '立替記録',
    //     permissions: ['Super', 'Admin', 'Accounting', 'Driver', 'Seller', 'OperationManager'],
    //     requiresAuth: true,
    //     icon: 'icon-park-outline:finance'
    //   }
    // },
    {
      name: 'finance_advance',
      path: '/finance/advance',
      component: 'self',
      meta: {
        title: '立替精算',
        permissions: ['Super', 'Admin', 'Accounting', 'Seller'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },
    {
      name: 'finance_earnings',
      path: '/finance/earnings',
      component: 'self',
      meta: {
        title: '営業額',
        permissions: ['Super', 'Admin', 'Accounting'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },
    {
      name: 'finance_costAccounting',
      path: '/finance/costAccounting',
      component: 'self',
      meta: {
        title: '原価精算',
        permissions: ['Super', 'Admin', 'Accounting'],
        requiresAuth: true,
        icon: 'icon-park-outline:finance'
      }
    },
    // {
    //   name: 'finance_accident',
    //   path: '/finance/accident',
    //   component: 'self',
    //   meta: {
    //     title: '事故決算',
    //     permissions: ['Super', 'Admin', 'Accounting'],
    //     requiresAuth: true,
    //     icon: 'icon-park-outline:finance'
    //   }
    // },
    {
      name: 'finance_dict',
      path: '/finance/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin', 'Accounting'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    }
  ],
  meta: {
    title: '経理管理',
    permissions: ['Super', 'Admin', 'Accounting'],
    icon: 'icon-park-outline:finance',
    order: 6
  }
};

export default sys;
