const sys: AuthRoute.Route = {
  name: 'operate',
  path: '/operate',
  component: 'basic',
  children: [
    {
      name: 'operate_call',
      path: '/operate/call',
      component: 'self',
      meta: {
        title: '運管点呼',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'OperationManager'],
        requiresAuth: true,
        icon: 'mingcute:computer-line'
      }
    },
    {
      name: 'operate_accident',
      path: '/operate/accident',
      component: 'self',
      meta: {
        title: '事故',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'OperationManager'],
        requiresAuth: true,
        icon: 'healthicons:accident-and-emergency'
      }
    },
    {
      name: 'operate_csku',
      path: '/operate/csku',
      component: 'self',
      meta: {
        title: '車両日程',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'OperationManager'],
        requiresAuth: true,
        icon: 'ic:outline-calendar-month'
      }
    },
    {
      name: 'operate_dsku',
      path: '/operate/dsku',
      component: 'self',
      meta: {
        title: 'ドライバー日程',
        permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'OperationManager'],
        requiresAuth: true,
        icon: 'ic:outline-calendar-month'
      }
    },
    {
      name: 'operate_dict',
      path: '/operate/dict',
      component: 'self',
      meta: {
        title: 'データ設定',
        permissions: ['Super', 'Admin', 'Operator', 'OperationManager'],
        requiresAuth: true,
        icon: 'material-symbols:library-books-outline'
      }
    }
  ],
  meta: {
    title: '運営管理',
    permissions: ['Super', 'Admin', 'Operator', 'Seller', 'Driver', 'OperationManager'],
    icon: 'mingcute:computer-line',
    order: 3
  }
};

export default sys;
