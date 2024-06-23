/**
 * env environment type
 * - dev: Backend development environment
 * - test: Backend test environment
 * - prod: Backend production environment
 */
type EnvType = 'dev' | 'test' | 'prod';

/** env environment configuration */
interface EnvConfig {
	/** The request address of the backend */
	url: string;
	/**
	 * Proxy ID, used to intercept address forwarding proxy
	 * (eg: "/api", this has nothing to do with whether the backend path has a "/api" path)
	 * */
	proxy: string;
	/**
	 * static resources url
	 * */
	static: string;
}

interface ImportMetaEnv {
	/** Project base address */
	readonly VITE_BASE_URL: string;
	/** project name */
	readonly VITE_APP_NAME: string;
	/** project title */
	readonly VITE_APP_TITLE: string;
	/** project description */
	readonly VITE_APP_DESC: string;
	/**
	 * Privilege Routing Mode:
	 * - static - Front-end declared static
	 * - dynamic - The dynamic returned by the backend
	 */
	readonly VITE_AUTH_ROUTE_MODE: 'static' | 'dynamic';
	/** Route the path of the home page */
	readonly VITE_ROUTE_HOME_PATH: Exclude<
		AuthRoute.RoutePath,
		'/not-found-page' | '/:pathMatch(.*)*'
	>;
	/** vite environment type */
	readonly VITE_ENV_TYPE?: EnvType;
	/** Enable request proxy */
	readonly VITE_HTTP_PROXY?: 'true' | 'false';
	/** Whether to enable analysis of package file size results */
	readonly VITE_VISUALIZER?: 'true' | 'false';
	/** Whether to enable packaging and compression */
	readonly VITE_COMPRESS?: 'true' | 'false';
	/** Compression Algorithm Type */
	readonly VITE_COMPRESS_TYPE?:
		| 'gzip'
		| 'brotliCompress'
		| 'deflate'
		| 'deflateRaw';
	/** hash routing mode */
	readonly VITE_HASH_ROUTE?: 'true' | 'false';
}

interface ImportMeta {
	readonly env: ImportMetaEnv;
}
