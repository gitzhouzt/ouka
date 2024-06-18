import { getLoginModuleRegExp } from '@/utils';

/** fixed route */
export const constantRoutes: AuthRoute.Route[] = [
  {
    name: 'root',
    path: '/',
    redirect: import.meta.env.VITE_ROUTE_HOME_PATH,
    meta: {
      title: 'Root'
    }
  },
  {
    name: 'login',
    path: '/login',
    component: 'self',
    props: route => {
      const moduleType = (route.params.module as EnumType.LoginModuleKey) || 'pwd-login';
      return {
        module: moduleType
      };
    },
    meta: {
      title: 'ログイン',
      dynamicPath: `/login/:module(${getLoginModuleRegExp()})?`,
      singleLayout: 'blank'
    }
  },
  {
    name: 'no-permission',
    path: '/no-permission',
    component: 'self',
    meta: {
      title: '許可なし',
      singleLayout: 'blank'
    }
  },
  {
    name: 'not-found',
    path: '/not-found',
    component: 'self',
    meta: {
      title: '見つからない',
      singleLayout: 'blank'
    }
  },
  {
    name: 'service-error',
    path: '/service-error',
    component: 'self',
    meta: {
      title: 'サーバーエラー',
      singleLayout: 'blank'
    }
  },
  // 無効なパスに一致するルート
  {
    name: 'not-found-page',
    path: '/:pathMatch(.*)*',
    component: 'blank',
    meta: {
      title: '見つからない',
      singleLayout: 'blank'
    }
  }
];
