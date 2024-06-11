import type { Component } from 'vue';
import { EnumLayoutComponentName } from '@/enum';
import { BasicLayout, BlankLayout } from '@/layouts';
import { views } from '@/views';

type LayoutComponent = Record<EnumType.LayoutComponentName, () => Promise<Component>>;

/**
 * Get the vue file imported by the page (lazy loading method)
 * @param layoutType - layout type
 */
export function getLayoutComponent(layoutType: EnumType.LayoutComponentName) {
  const layoutComponent: LayoutComponent = {
    basic: BasicLayout,
    blank: BlankLayout
  };
  return () => setViewComponentName(layoutComponent[layoutType], EnumLayoutComponentName[layoutType]);
}

/**
 * Get the vue file imported by the page (lazy loading method)
 * @param routeKey - routing key
 */
export function getViewComponent(routeKey: AuthRoute.RouteKey) {
  if (!views[routeKey]) {
    window.console.error(`There is no corresponding component file for route "${routeKey}"!`);
  }
  return () => setViewComponentName(views[routeKey], routeKey) as Promise<Component>;
}

/** Set a name for the page component */
async function setViewComponentName(asyncComponent: () => Promise<Component>, name: string) {
  const component = (await asyncComponent()) as { default: Component };
  Object.assign(component.default, { name });
  return component;
}
