interface Window {
  $loadingBar?: import('naive-ui').LoadingBarProviderInst;
  $dialog?: import('naive-ui').DialogProviderInst;
  $message?: import('naive-ui').MessageProviderInst;
  $notification?: import('naive-ui').NotificationProviderInst;
}

/** generic type */
declare namespace Common {
  /**
   * strategy mode
   * [state, callback function to execute when true]
   */
  type StrategyAction = [boolean, () => void];
}

/** build time */
declare const PROJECT_BUILD_TIME: string;
