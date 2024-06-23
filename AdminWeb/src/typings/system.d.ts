/** enumerated key type */
declare namespace EnumType {
  /** layout component name */
  type LayoutComponentName = keyof typeof import('@/enum').EnumLayoutComponentName;

  /** layout mode */
  type ThemeLayoutMode = keyof typeof import('@/enum').EnumThemeLayoutMode;

  /** Multi-tab style */
  type ThemeTabMode = keyof typeof import('@/enum').EnumThemeTabMode;

  /** Menu position for horizontal mode */
  type ThemeHorizontalMenuPosition = keyof typeof import('@/enum').EnumThemeHorizontalMenuPosition;

  /** transition animation */
  type ThemeAnimateMode = keyof typeof import('@/enum').EnumThemeAnimateMode;

  /** login module */
  type LoginModuleKey = keyof typeof import('@/enum').EnumLoginModule;
}

/** Relevant type of request */
declare namespace Service {
  /** request environment type
   * - test:test environment
   * - prod:prod environment */
  type HttpEnv = 'test' | 'prod';

  /**
   * Requested error type:
   * - axios: axios error: network error, request timed out, default bottom line error
   * - http: The request is successful, the response status code is not 200 error
   * - backend: The request is successful, the status code of the response is 200, the business error defined by the backend
   */
  type RequestErrorType = 'axios' | 'http' | 'backend';

  /** request error */
  interface RequestError {
    /** Error type of service requested */
    type: RequestErrorType;
    /** error code */
    code: string | number;
    /** error message */
    msg: string;
  }

  /** Data structure configuration returned by the backend interface */
  interface BackendResultConfig {
    /** Attribute field representing the status code of the backend request */
    codeKey: string;
    /** Attribute field representing backend request data */
    dataKey: string;
    /** Property fields representing backend messages */
    msgKey: string;
    /** The status of a successful request as defined on the backend business */
    successCode: number | string;
  }

  /** Custom request success result */
  interface SuccessResult<T = any> {
    /** request error */
    error: null;
    /** request data */
    data: T;
  }

  /** Custom request failure result */
  interface FailedResult {
    /** request error */
    error: RequestError;
    /** request data */
    data: null;
  }

  /** custom request result */
  type RequestResult<T = any> = SuccessResult<T> | FailedResult;

  /** mock example interface type: the type of data returned by the backend interface */
  interface MockServiceResult<T = any> {
    /** status code */
    code: string | number;
    /** interface data */
    data: T;
    /** interface message */
    message: string;
  }

  /** mock response option */
  interface MockOption {
    url: Record<string, any>;
    body: Record<string, any>;
    query: Record<string, any>;
    headers: Record<string, any>;
  }
}

/** Subject related type */
declare namespace Theme {
  /** Theme configuration */
  interface Setting {
    /** darkMode */
    darkMode: boolean;
    /** followSystemTheme */
    followSystemTheme: boolean;
    /** layout */
    layout: Layout;
    /** themeColor */
    themeColor: string;
    /** themeColorList */
    themeColorList: string[];
    /** otherColor */
    otherColor: OtherColor;
    /** Whether to customize the color of info (by default, a color that is one level darker than the theme color) */
    isCustomizeInfoColor: boolean;
    /** Fixed header and multiple tabs */
    fixedHeaderAndTab: boolean;
    /** Show reload button */
    showReload: boolean;
    /** head style */
    header: Header;
    /** Multi-tab style */
    tab: Tab;
    /** sidebar style */
    sider: Sider;
    /** menu style */
    menu: Menu;
    /** bottom style */
    footer: Footer;
    /** page style */
    page: Page;
  }
  /** layout style */
  interface Layout {
    /** minimum width */
    minWidth: number;
    /** layout mode */
    mode: EnumType.ThemeLayoutMode;
    /** List of layout modes */
    modeList: LayoutModeList[];
  }
  interface LayoutModeList {
    value: EnumType.ThemeLayoutMode;
    label: import('@/enum').EnumThemeLayoutMode;
  }

  /** Other theme colors */
  interface OtherColor {
    /** info */
    info: string;
    /** success */
    success: string;
    /** warning */
    warning: string;
    /** error */
    error: string;
  }

  /** Header style */
  interface Header {
    /** head inversion */
    inverted: boolean;
    /** head height */
    height: number;
    /** breadcrumb style */
    crumb: Crumb;
  }
  /** breadcrumb style */
  interface Crumb {
    /** Breadcrumbs are visible */
    visible: boolean;
    /** showIcon */
    showIcon: boolean;
  }

  /** Multi-tab style */
  export interface Tab {
    /** Multiple tabs are visible */
    visible: boolean;
    /** Multiple tab height */
    height: number;
    /** Multiple tab mode */
    mode: EnumType.ThemeTabMode;
    /** Multi-tab style list */
    modeList: ThemeTabModeList[];
    /** Enable multi-tab caching */
    isCache: boolean;
  }

  /** Multi-tab style list */
  interface ThemeTabModeList {
    value: EnumType.ThemeTabMode;
    label: import('@/enum').EnumThemeTabMode;
  }

  /** sidebar style */
  interface Sider {
    /** Sidebar Invert Color */
    inverted: boolean;
    /** sidebar width */
    width: number;
    /** The width of the sidebar when it is collapsed */
    collapsedWidth: number;
    /** vertical-Sidebar width in mix mode */
    mixWidth: number;
    /** vertical-The width of the sidebar when the sidebar is collapsed in mix mode */
    mixCollapsedWidth: number;
    /** vertical-The width of the submenu of the sidebar in mix mode */
    mixChildMenuWidth: number;
  }

  /** menu style */
  interface Menu {
    /** The position of the menu in horizontal mode */
    horizontalPosition: EnumType.ThemeHorizontalMenuPosition;
    /** List of positions for menus in horizontal mode */
    horizontalPositionList: HorizontalMenuPositionList[];
  }
  /** List of positions for menus in horizontal mode */
  interface HorizontalMenuPositionList {
    value: EnumType.ThemeHorizontalMenuPosition;
    label: import('@/enum').EnumThemeHorizontalMenuPosition;
  }

  /** bottom style */
  interface Footer {
    /** Whether to fix the bottom */
    fixed: boolean;
    /** Bottom height */
    height: number;
  }

  /** page style */
  interface Page {
    /** Whether the page has animation enabled */
    animate: boolean;
    /** type of animation */
    animateMode: EnumType.ThemeAnimateMode;
    /** List of animation types */
    animateModeList: AnimateModeList[];
  }
  /** List of animation types */
  interface AnimateModeList {
    value: EnumType.ThemeAnimateMode;
    label: import('@/enum').EnumThemeAnimateMode;
  }
}

/** global header properties */
interface GlobalHeaderProps {
  /** show logo */
  showLogo: boolean;
  /** show Header Menu */
  showHeaderMenu: boolean;
  /** show Menu Collapse */
  showMenuCollapse: boolean;
}

/** GlobalMenuOption */
type GlobalMenuOption = {
  key: string;
  label: string;
  routeName: string;
  routePath: string;
  icon?: () => import('vue').VNodeChild;
  children?: GlobalMenuOption[];
};

/** GlobalBreadcrumb */
type GlobalBreadcrumb = import('naive-ui').DropdownOption & {
  key: string;
  label: string;
  disabled: boolean;
  routeName: string;
  hasChildren: boolean;
  children?: GlobalBreadcrumb[];
};

/** GlobalTabRoute */
interface GlobalTabRoute extends Pick<import('vue-router').RouteLocationNormalizedLoaded, 'name' | 'path' | 'meta'> {
  /** scrollPosition */
  scrollPosition: {
    left: number;
    top: number;
  };
}
