/** request env */
type ServiceEnv = Record<EnvType, EnvConfig>;

/** env config */
const serviceEnvConfig: ServiceEnv = {
  dev: {
    url: 'http://localhost:8010/api',
    proxy: '/api',
    static: 'http://133.125.50.67'
  },
  test: {
    url: 'http://localhost:8010/api',
    proxy: '/api',
    static: 'http://133.125.50.67'
  },
  prod: {
    url: 'http://133.125.50.67/api',
    proxy: '/api',
    static: 'http://133.125.50.67'
  }
};

/**
 * get env
 * @param env env
 */
export function getEnvConfig(env: ImportMetaEnv) {
  const { VITE_ENV_TYPE = 'dev' } = env;

  const envConfig = serviceEnvConfig[VITE_ENV_TYPE];

  return envConfig;
}
