/**
 * Filter routes based on user permissions
 * @param routes - Authority routing
 * @param permission ‐ Authority
 */
export function filterAuthRoutesByUserPermission(routes: AuthRoute.Route[], permission: string) {
  return routes.map(route => filterAuthRouteByUserPermission(route, permission)).flat(1);
}

/**
 * Filter routes based on single user permission
 * @param route - Single Permission Routing
 * @param permission - Authority
 */
function filterAuthRouteByUserPermission(route: AuthRoute.Route, permission: string): AuthRoute.Route[] {
  const filterRoute = { ...route };
  const hasPermission =
    !route.meta.permissions ||
    permission.indexOf('Super') >= 0 ||
    route.meta.permissions.map(i => permission.indexOf(i) >= 0).length > 0;

  if (filterRoute.children) {
    const filterChildren = filterRoute.children.map(item => filterAuthRouteByUserPermission(item, permission)).flat(1);
    Object.assign(filterRoute, { children: filterChildren });
  }
  return hasPermission ? [filterRoute] : [];
}
