import type { RouteRecordNormalized, RouteLocationNormalizedLoaded } from 'vue-router';

/**
 *	Get tab routing based on vue routing
 * @param route
 */
export function getTabRouteByVueRoute(route: RouteRecordNormalized | RouteLocationNormalizedLoaded) {
  const tabRoute: GlobalTabRoute = {
    name: route.name,
    path: route.path,
    meta: route.meta,
    scrollPosition: {
      left: 0,
      top: 0
    }
  };
  return tabRoute;
}

/**
 * Get the index of the tab in the multi-tab data
 * @param tabs - Multi-tab data
 * @param path - the path to the tab
 */
export function getIndexInTabRoutes(tabs: GlobalTabRoute[], path: string) {
  return tabs.findIndex(tab => tab.path === path);
}

/**
 * Determine whether the tab is in multi-tab data
 * @param tabs - Multi-tab data
 * @param path - the path to the tab
 */
export function isInTabRoutes(tabs: GlobalTabRoute[], path: string) {
  return getIndexInTabRoutes(tabs, path) > -1;
}
