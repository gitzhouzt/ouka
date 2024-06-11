import type { RouteRecordRaw } from 'vue-router';
import { getLayoutComponent, getViewComponent } from './component';

/**
 * Get the name collection of all fixed routes
 * @param routes - fixed route
 */
export function getConstantRouteNames(routes: AuthRoute.Route[]) {
  return routes.map(route => getConstantRouteName(route)).flat(1);
}

/**
 * Convert permission routing to vue routing
 * @param routes - Authority routing
 * @description All multi-level routes will be converted to second-level routes
 */
export function transformAuthRoutesToVueRoutes(routes: AuthRoute.Route[]) {
  return routes.map(route => transformAuthRouteToVueRoute(route)).flat(1);
}

/**
 * Convert permission routing into menu data for search
 * @param routes - Authority routing
 * @param treeMap
 */
export function transformAuthRoutesToSearchMenus(routes: AuthRoute.Route[], treeMap: AuthRoute.Route[] = []) {
  if (routes && routes.length === 0) return [];
  return routes.reduce((acc, cur) => {
    if (!cur.meta?.hide) {
      acc.push(cur);
    }
    if (cur.children && cur.children.length > 0) {
      transformAuthRoutesToSearchMenus(cur.children, treeMap);
    }
    return acc;
  }, treeMap);
}

/** Convert route name to route path */
export function transformRouteNameToRoutePath(
  name: Exclude<AuthRoute.RouteKey, 'not-found-page'>
): AuthRoute.RoutePath {
  const rootPath: AuthRoute.RoutePath = '/';
  if (name === 'root') return rootPath;

  const splitMark: AuthRoute.RouteSplitMark = '_';
  const pathSplitMark = '/';
  const path = name.split(splitMark).join(pathSplitMark);

  return (pathSplitMark + path) as AuthRoute.RoutePath;
}

/** Convert route path to route name */
export function transformRoutePathToRouteName(
  path: Exclude<AuthRoute.RoutePath, '/not-found-page' | '/:pathMatch(.*)*'>
): AuthRoute.RouteKey {
  if (path === '/') return 'root';

  const pathSplitMark = '/';
  const routeSplitMark: AuthRoute.RouteSplitMark = '_';

  const name = path.split(pathSplitMark).slice(1).join(routeSplitMark) as AuthRoute.RouteKey;

  return name;
}

/**
 * Get the name collection of all fixed routes
 * @param route - fixed route
 */
function getConstantRouteName(route: AuthRoute.Route) {
  const names = [route.name];
  if (hasChildren(route)) {
    names.push(...route.children!.map(item => getConstantRouteName(item)).flat(1));
  }
  return names;
}

type ComponentAction = Record<AuthRoute.RouteComponent, () => void>;

/**
 * Convert a single permission route to a vue route
 * @param item - Single Permission Routing
 */
function transformAuthRouteToVueRoute(item: AuthRoute.Route) {
  const resultRoute: RouteRecordRaw[] = [];

  const itemRoute = { ...item } as RouteRecordRaw;

  // dynamic path
  if (hasDynamicPath(item)) {
    Object.assign(itemRoute, { path: item.meta.dynamicPath });
  }

  // Outbound routing
  if (hasHref(item)) {
    Object.assign(itemRoute, { component: getViewComponent('not-found-page') });
  }

  // routing component
  if (hasComponent(item)) {
    const action: ComponentAction = {
      basic() {
        itemRoute.component = getLayoutComponent('basic');
      },
      blank() {
        itemRoute.component = getLayoutComponent('blank');
      },
      multi() {
        // Multi-level routing must have sub-routing
        if (hasChildren(item)) {
          Object.assign(itemRoute, { meta: { ...itemRoute.meta, multi: true } });
          delete itemRoute.component;
        } else {
          window.console.error('Multi-level routing is missing sub-routing: ', item);
        }
      },
      self() {
        itemRoute.component = getViewComponent(item.name);
      }
    };
    try {
      if (item.component) {
        action[item.component]();
      } else {
        window.console.error('Route component parsing failed: ', item);
      }
    } catch {
      window.console.error('Route component parsing failed: ', item);
    }
  }

  // Note: separate routes have no children
  if (isSingleRoute(item)) {
    if (hasChildren(item)) {
      window.console.error('A single route should not have child routes: ', item);
    }

    // Special handling for catching invalid routes
    if (item.name === 'not-found-page') {
      itemRoute.children = [
        {
          path: '',
          name: item.name,
          component: getViewComponent('not-found-page')
        }
      ];
    } else {
      const parentPath = `${itemRoute.path}-parent` as AuthRoute.SingleRouteParentPath;

      const layout = item.meta.singleLayout === 'basic' ? getLayoutComponent('basic') : getLayoutComponent('blank');

      const parentRoute: RouteRecordRaw = {
        path: parentPath,
        component: layout,
        redirect: item.path,
        children: [itemRoute]
      };

      return [parentRoute];
    }
  }

  // sub route
  if (hasChildren(item)) {
    const children = (item.children as AuthRoute.Route[]).map(child => transformAuthRouteToVueRoute(child)).flat();

    // Find the first sub-routing path that is not an intermediate level of multi-level routing as a redirection path
    const redirectPath: AuthRoute.RoutePath = (children.find(v => !v.meta?.multi)?.path || '/') as AuthRoute.RoutePath;
    if (redirectPath === '/') {
      window.console.error('This multilevel route has no valid subpaths', item);
    }

    if (item.component === 'multi') {
      // Multi-level routing, extracting sub-routes to become the same level
      resultRoute.push(...children);
      delete itemRoute.children;
    } else {
      itemRoute.children = children;
    }
    itemRoute.redirect = redirectPath;
  }

  resultRoute.push(itemRoute);

  return resultRoute;
}

/**
 * Whether there is an external link
 * @param item - Authority routing
 */
function hasHref(item: AuthRoute.Route) {
  return Boolean(item.meta.href);
}

/**
 * Whether there is a dynamic routing path
 * @param item - Authority routing
 */
function hasDynamicPath(item: AuthRoute.Route) {
  return Boolean(item.meta.dynamicPath);
}

/**
 * Is there a routing component
 * @param item - Authority routing
 */
function hasComponent(item: AuthRoute.Route) {
  return Boolean(item.component);
}

/**
 * Whether there are sub-routes
 * @param item - Authority routing
 */
function hasChildren(item: AuthRoute.Route) {
  return Boolean(item.children && item.children.length);
}

/**
 * Whether it is a single-level routing
 * @param item - Authority routing
 */
function isSingleRoute(item: AuthRoute.Route) {
  return Boolean(item.meta.singleLayout);
}
