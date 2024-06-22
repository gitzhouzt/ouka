import type { ProxyOptions } from 'vite';

/**
 * network proxy
 * @param isOpenProxy -
 * @param envConfig - env config
 */
export function createViteProxy(isOpenProxy: boolean, envConfig: EnvConfig) {
	if (!isOpenProxy) return undefined;

	const proxy: Record<string, string | ProxyOptions> = {
		[envConfig.proxy]: {
			target: envConfig.url,
			changeOrigin: true,
			rewrite: (path) => path.replace(new RegExp(`^${envConfig.proxy}`), ''),
		},
	};

	return proxy;
}
