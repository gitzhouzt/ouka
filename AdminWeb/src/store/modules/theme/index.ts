import { defineStore } from 'pinia';
import { darkTheme } from 'naive-ui';
import { getThemeSettings, getNaiveThemeOverrides } from './helpers';

type ThemeState = Theme.Setting;

export const useThemeStore = defineStore('theme-store', {
  state: (): ThemeState => getThemeSettings(),
  getters: {
    /** Theme configuration for naiveUI */
    naiveThemeOverrides(state) {
      const overrides = getNaiveThemeOverrides({ primary: state.themeColor, ...state.otherColor });
      return overrides;
    },
    /** naive-ui darkMode */
    naiveTheme(state) {
      return state.darkMode ? darkTheme : undefined;
    },
    /** Page animation mode */
    pageAnimateMode(state) {
      return state.page.animate ? state.page.animateMode : undefined;
    }
  },
  actions: {
    /** reset theme state */
    resetThemeStore() {
      this.$reset();
    },
    /** set dark mode */
    setDarkMode(darkMode: boolean) {
      this.darkMode = darkMode;
    },
    /** Set auto follow system theme */
    setFollowSystemTheme(visible: boolean) {
      this.followSystemTheme = visible;
    },
    /** Automatically follow system theme */
    setAutoFollowSystemMode(darkMode: boolean) {
      if (this.followSystemTheme) {
        this.darkMode = darkMode;
      }
    },
    /** Toggle/disable dark mode */
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
    },
    /** Set layout minimum width */
    setLayoutMinWidth(minWidth: number) {
      this.layout.minWidth = minWidth;
    },
    /** set layout mode */
    setLayoutMode(mode: EnumType.ThemeLayoutMode) {
      this.layout.mode = mode;
    },
    /** Set the sidebar inversion color */
    setSiderInverted(isInverted: boolean) {
      this.sider.inverted = isInverted;
    },
    /** Set head inversion color */
    setHeaderInverted(isInverted: boolean) {
      this.header.inverted = isInverted;
    },
    /** Set system theme color */
    setThemeColor(themeColor: string) {
      this.themeColor = themeColor;
    },
    /** Set up fixed headers and multiple tabs */
    setIsFixedHeaderAndTab(isFixed: boolean) {
      this.fixedHeaderAndTab = isFixed;
    },
    /** Set the reload button visible state */
    setReloadVisible(visible: boolean) {
      this.showReload = visible;
    },
    /** set head height */
    setHeaderHeight(height: number | null) {
      if (height) {
        this.header.height = height;
      }
    },
    /** Set header breadcrumbs visible */
    setHeaderCrumbVisible(visible: boolean) {
      this.header.crumb.visible = visible;
    },
    /** Set the header breadcrumb icon to be visible */
    setHeaderCrumbIconVisible(visible: boolean) {
      this.header.crumb.showIcon = visible;
    },
    /** Make multiple tabs visible */
    setTabVisible(visible: boolean) {
      this.tab.visible = visible;
    },
    /** Set multi-tab height */
    setTabHeight(height: number | null) {
      if (height) {
        this.tab.height = height;
      }
    },
    /** Set multi-tab style */
    setTabMode(mode: EnumType.ThemeTabMode) {
      this.tab.mode = mode;
    },
    /** Set up multi-tab caching */
    setTabIsCache(isCache: boolean) {
      this.tab.isCache = isCache;
    },
    /** sidebar width */
    setSiderWidth(width: number | null) {
      if (width) {
        this.sider.width = width;
      }
    },
    /** The width of the sidebar when it is collapsed */
    setSiderCollapsedWidth(width: number) {
      this.sider.collapsedWidth = width;
    },
    /** vertical-mix模式下侧边栏宽度 */
    setMixSiderWidth(width: number | null) {
      if (width) {
        this.sider.mixWidth = width;
      }
    },
    /** vertical-The width of the sidebar when the sidebar is collapsed in mix mode */
    setMixSiderCollapsedWidth(width: number) {
      this.sider.mixCollapsedWidth = width;
    },
    /** vertical-The width of the submenu displayed by the sidebar in mix mode */
    setMixSiderChildMenuWidth(width: number) {
      this.sider.mixChildMenuWidth = width;
    },
    /** Sets the position of the menu for horizontal mode */
    setHorizontalMenuPosition(position: EnumType.ThemeHorizontalMenuPosition) {
      this.menu.horizontalPosition = position;
    },
    /** Set whether the bottom is fixed */
    setFooterIsFixed(isFixed: boolean) {
      this.footer.fixed = isFixed;
    },
    /** set bottom height */
    setFooterHeight(height: number) {
      this.footer.height = height;
    },
    /** Set whether to transition animation when switching pages */
    setPageIsAnimate(animate: boolean) {
      this.page.animate = animate;
    },
    /** Set page transition animation type */
    setPageAnimateMode(mode: EnumType.ThemeAnimateMode) {
      this.page.animateMode = mode;
    }
  }
});
