import type { Router, RouteLocationNormalizedLoaded } from 'vue-router';
import { defineStore } from 'pinia';
import { useRouterPush } from '@/composables';
import { getTabRoutes, clearTabRoutes } from '@/utils';
import { useThemeStore } from '../theme';
import { getTabRouteByVueRoute, isInTabRoutes, getIndexInTabRoutes } from './helpers';

interface TabState {
  /** Multi-tab data */
  tabs: GlobalTabRoute[];
  /** Multi-tab homepage */
  homeTab: GlobalTabRoute;
  /** The currently active tab (routing path) */
  activeTab: string;
}

export const useTabStore = defineStore('tab-store', {
  state: (): TabState => ({
    tabs: [],
    homeTab: {
      name: 'root',
      path: '/',
      meta: {
        title: 'Root'
      },
      scrollPosition: {
        left: 0,
        top: 0
      }
    },
    activeTab: ''
  }),
  getters: {
    /** Tab index of the currently active state */
    activeTabIndex(state) {
      const { tabs, activeTab } = state;
      return tabs.findIndex(tab => tab.path === activeTab);
    }
  },
  actions: {
    /** reset tab state */
    resetTabStore() {
      clearTabRoutes();
      this.$reset();
    },
    /**
     * Set the tab corresponding to the current route to the active state
     * @param path - route path
     */
    setActiveTab(path: string) {
      this.activeTab = path;
    },
    /**
     * Initialize the home page tab routing
     * @param routeHomeName - The name of the routing home page
     * @param router - routing instance
     */
    initHomeTab(routeHomeName: string, router: Router) {
      const routes = router.getRoutes();
      const findHome = routes.find(item => item.name === routeHomeName);
      if (findHome && !findHome.children.length) {
        // A sub-route cannot be used as a Tab
        this.homeTab = getTabRouteByVueRoute(findHome);
      }
    },
    /**
     * Add multiple tabs
     * @param route - routing
     */
    addTab(route: RouteLocationNormalizedLoaded) {
      if (!isInTabRoutes(this.tabs, route.path)) {
        const tab = getTabRouteByVueRoute(route);
        this.tabs.push(tab);
      }
    },
    /**
     * Delete multiple tabs
     * @param path - routing
     */
    removeTab(path: string) {
      const { routerPush } = useRouterPush(false);

      const isActive = this.activeTab === path;
      const updateTabs = this.tabs.filter(tab => tab.path !== path);
      this.tabs = updateTabs;
      if (isActive && updateTabs.length) {
        const activePath = updateTabs[updateTabs.length - 1].path;
        this.setActiveTab(activePath);
        routerPush(activePath);
      }
    },
    /**
     * Clear multi-tabs (multi-tab homepage is reserved)
     * @param excludes - reserved multi-tab paths
     */
    clearTab(excludes: string[] = []) {
      const { routerPush } = useRouterPush(false);

      const homePath = this.homeTab.path;
      const remain = [homePath, ...excludes];
      const hasActive = remain.includes(this.activeTab);
      const updateTabs = this.tabs.filter(tab => remain.includes(tab.path));
      this.tabs = updateTabs;
      if (!hasActive && updateTabs.length) {
        const activePath = updateTabs[updateTabs.length - 1].path;
        this.setActiveTab(activePath);
        routerPush(activePath);
      }
    },
    /**
     * Clear left multiple tabs
     * @param path - routing
     */
    clearLeftTab(path: string) {
      const index = getIndexInTabRoutes(this.tabs, path);
      if (index > -1) {
        const excludes = this.tabs.slice(index).map(item => item.path);
        this.clearTab(excludes);
      }
    },
    /**
     * Clear multiple tabs on the right
     * @param path - routing
     */
    clearRightTab(path: string) {
      const index = getIndexInTabRoutes(this.tabs, path);
      if (index > -1) {
        const excludes = this.tabs.slice(0, index + 1).map(item => item.path);
        this.clearTab(excludes);
      }
    },
    /**
     * Click on a single tab
     * @param path - routing
     */
    handleClickTab(path: string) {
      const { routerPush } = useRouterPush(false);

      const isActive = this.activeTab === path;
      if (!isActive) {
        this.setActiveTab(path);
        routerPush(path);
      }
    },
    /**
     * record tab scroll position
     * @param path - routing
     * @param position - The scroll position of the current page of the tab
     */
    recordTabScrollPosition(path: string, position: { left: number; top: number }) {
      const index = getIndexInTabRoutes(this.tabs, path);
      if (index > -1) {
        this.tabs[index].scrollPosition = position;
      }
    },
    /**
     * Get tab scroll position
     * @param path - routing
     */
    getTabScrollPosition(path: string) {
      const position = {
        left: 0,
        top: 0
      };
      const index = getIndexInTabRoutes(this.tabs, path);
      if (index > -1) {
        Object.assign(position, this.tabs[index].scrollPosition);
      }
      return position;
    },
    /** Initialize Tab state */
    iniTabStore(currentRoute: RouteLocationNormalizedLoaded) {
      const theme = useThemeStore();

      const tabs: GlobalTabRoute[] = theme.tab.isCache ? getTabRoutes() : [];

      const hasHome = isInTabRoutes(tabs, this.homeTab.path);
      if (!hasHome && this.homeTab.name !== 'root') {
        tabs.unshift(this.homeTab);
      }

      const isHome = currentRoute.path === this.homeTab.path;
      const hasCurrent = isInTabRoutes(tabs, currentRoute.path);
      if (!isHome && !hasCurrent) {
        const currentTab = getTabRouteByVueRoute(currentRoute);
        tabs.push(currentTab);
      }

      this.tabs = tabs;
      this.setActiveTab(currentRoute.path);
    }
  }
});
