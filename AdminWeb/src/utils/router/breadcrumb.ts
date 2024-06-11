/**
 * Get breadcrumb data
 * @param activeKey - The key of the current page route
 * @param menus - menu data
 * @param rootPath - root routing path
 */
export function getBreadcrumbByRouteKey(activeKey: string, menus: GlobalMenuOption[], rootPath: string) {
  const breadcrumbMenu = getBreadcrumbMenu(activeKey, menus);
  const breadcrumb = breadcrumbMenu.map(item => transformBreadcrumbMenuToBreadcrumb(item, rootPath));
  return breadcrumb;
}

/**
 * Get menu in breadcrumb format based on menu data
 * @param activeKey - The key of the current page route
 * @param menus - menu data
 */
function getBreadcrumbMenu(activeKey: string, menus: GlobalMenuOption[]) {
  const breadcrumbMenu: GlobalMenuOption[] = [];
  menus.some(menu => {
    const flag = activeKey.includes(menu.routeName);
    if (flag) {
      breadcrumbMenu.push(...getBreadcrumbMenuItem(activeKey, menu));
    }
    return flag;
  });
  return breadcrumbMenu;
}

/**
 * Get a menu in breadcrumb format based on a single menu data
 * @param activeKey - The key of the current page route
 * @param menus - menu data
 */
function getBreadcrumbMenuItem(activeKey: string, menu: GlobalMenuOption) {
  const breadcrumbMenu: GlobalMenuOption[] = [];
  if (activeKey === menu.routeName) {
    breadcrumbMenu.push(menu);
  }
  if (activeKey.includes(menu.routeName) && menu.children && menu.children.length) {
    breadcrumbMenu.push(menu);
    breadcrumbMenu.push(
      ...menu.children.map(item => getBreadcrumbMenuItem(activeKey, item as GlobalMenuOption)).flat(1)
    );
  }

  return breadcrumbMenu;
}

/**
 * Convert menu data in breadcrumb format to breadcrumb data
 * @param menus - menu data
 * @param rootPath - root routing path
 */
function transformBreadcrumbMenuToBreadcrumb(menu: GlobalMenuOption, rootPath: string) {
  const hasChildren = Boolean(menu.children && menu.children.length);
  const breadcrumb: GlobalBreadcrumb = {
    key: menu.routeName,
    label: menu.label as string,
    routeName: menu.routeName,
    disabled: menu.routePath === rootPath,
    hasChildren
  };
  if (menu.icon) {
    breadcrumb.icon = menu.icon;
  }
  if (hasChildren) {
    breadcrumb.children = menu.children?.map(item =>
      transformBreadcrumbMenuToBreadcrumb(item as GlobalMenuOption, rootPath)
    );
  }
  return breadcrumb;
}
