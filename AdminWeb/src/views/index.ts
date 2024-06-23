import type { Component } from 'vue';

type ViewComponent = Record<string, () => Promise<Component>>;

const importViews = import.meta.glob('./**/index.vue');

const COMPONENTS_KEY = 'components';
const PREFIX = './';
const SUFFIX = '/index.vue';
const PATH_SPLIT_MARK = '/';
const ROUTE_KEY_SPLIT_MARK = '_';

/**
 * The built-in route of the system,
 * the folder name is not used as RouteKey
 * */
const SYSTEM_VIEW = 'system-view_';

/** filter component  */
const viewKeys = Object.keys(importViews).filter(key => !key.includes(COMPONENTS_KEY));

function getViewComponent() {
  const components: ViewComponent = {};
  viewKeys.forEach(key => {
    const routeKey = key
      .replace(PREFIX, '')
      .replace(SUFFIX, '')
      .replace(new RegExp(PATH_SPLIT_MARK, 'g'), ROUTE_KEY_SPLIT_MARK)
      .replace(SYSTEM_VIEW, '');
    components[routeKey] = importViews[key];
  });
  return components;
}

export const views = getViewComponent();
