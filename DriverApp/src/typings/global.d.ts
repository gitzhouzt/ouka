interface Window {
	$loadingBar?: import('vant').LoadingBarProviderInst;
	$dialog?: import('vant').DialogProviderInst;
	$message?: import('vant').MessageProviderInst;
	$notification?: import('vant').Notify;
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
