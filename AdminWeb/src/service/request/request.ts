import { ref } from 'vue';
import type { Ref } from 'vue';
import type { AxiosInstance, AxiosRequestConfig } from 'axios';
import { useLoading, useBoolean } from '@/hooks';
import CustomAxiosInstance from './instance';

type RequestMethod = 'get' | 'post' | 'put' | 'delete';

interface RequestParam {
  url: string;
  method?: RequestMethod;
  data?: any;
  axiosConfig?: AxiosRequestConfig;
}

/**
 * create request
 * @param axiosConfig - axios Config
 * @param backendConfig - backendConfig
 */
export function createRequest(axiosConfig: AxiosRequestConfig, backendConfig?: Service.BackendResultConfig) {
  const customInstance = new CustomAxiosInstance(axiosConfig, backendConfig);

  /**
   * Asynchronous promise request
   * @param param - request parameters
   * - url: request address
   * - method: request method (default get)
   * - data: The data of the requested body
   * - axiosConfig: axiosConfig
   */
  async function asyncRequest<T>(param: RequestParam): Promise<Service.RequestResult<T>> {
    const { url } = param;
    const method = param.method || 'get';
    const { instance } = customInstance;
    const res = (await getRequestResponse(
      instance,
      method,
      url,
      param.data,
      param.axiosConfig
    )) as Service.RequestResult<T>;

    return res;
  }

  /**
   * get request
   * @param url - request address
   * @param config - axiosConfig
   */
  function get<T>(url: string, config?: AxiosRequestConfig) {
    return asyncRequest<T>({ url, method: 'get', axiosConfig: config });
  }

  /**
   * post request
   * @param url - request address
   * @param data - The data of the requested body
   * @param config - axiosConfig
   */
  function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return asyncRequest<T>({ url, method: 'post', data, axiosConfig: config });
  }
  /**
   * put request
   * @param url - request address
   * @param data - The data of the requested body
   * @param config - axiosConfig
   */
  function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return asyncRequest<T>({ url, method: 'put', data, axiosConfig: config });
  }

  /**
   * delete request
   * @param url - request address
   * @param config - axiosConfig
   */
  function handleDelete<T>(url: string, config?: AxiosRequestConfig) {
    return asyncRequest<T>({ url, method: 'delete', axiosConfig: config });
  }

  return {
    get,
    post,
    put,
    delete: handleDelete
  };
}

type RequestResultHook<T = any> = {
  data: Ref<T | null>;
  error: Ref<Service.RequestError | null>;
  loading: Ref<boolean>;
  network: Ref<boolean>;
};

/**
 * Create hooks request
 * @param axiosConfig - axiosConfig
 * @param backendConfig - backendConfig
 */
export function createHookRequest(axiosConfig: AxiosRequestConfig, backendConfig?: Service.BackendResultConfig) {
  const customInstance = new CustomAxiosInstance(axiosConfig, backendConfig);

  /**
   * hooks request
   * @param param - request parameters
   * - url: request address
   * - method: request method (default get)
   * - data: The data of the requested body
   * - axiosConfig: axiosConfig
   */
  function useRequest<T>(param: RequestParam): RequestResultHook<T> {
    const { loading, startLoading, endLoading } = useLoading();
    const { bool: network, setBool: setNetwork } = useBoolean(window.navigator.onLine);

    startLoading();
    const data = ref<T | null>(null) as Ref<T | null>;
    const error = ref<Service.RequestError | null>(null);

    function handleRequestResult(response: any) {
      const res = response as Service.RequestResult<T>;
      data.value = res.data;
      error.value = res.error;
      endLoading();
      setNetwork(window.navigator.onLine);
    }

    const { url } = param;
    const method = param.method || 'get';
    const { instance } = customInstance;

    getRequestResponse(instance, method, url, param.data, param.axiosConfig).then(handleRequestResult);

    return {
      data,
      error,
      loading,
      network
    };
  }

  /**
   * get request
   * @param url - request address
   * @param config - axiosConfig
   */
  function get<T>(url: string, config?: AxiosRequestConfig) {
    return useRequest<T>({ url, method: 'get', axiosConfig: config });
  }

  /**
   * post request
   * @param url - request address
   * @param data - The data of the requested body
   * @param config - axiosConfig
   */
  function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return useRequest<T>({ url, method: 'post', data, axiosConfig: config });
  }
  /**
   * put request
   * @param url - request address
   * @param data - The data of the requested body
   * @param config - axiosConfig
   */
  function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
    return useRequest<T>({ url, method: 'put', data, axiosConfig: config });
  }

  /**
   * delete request
   * @param url - request address
   * @param config - axiosConfig
   */
  function handleDelete<T>(url: string, config: AxiosRequestConfig) {
    return useRequest<T>({ url, method: 'delete', axiosConfig: config });
  }

  return {
    get,
    post,
    put,
    delete: handleDelete
  };
}

async function getRequestResponse(
  instance: AxiosInstance,
  method: RequestMethod,
  url: string,
  data?: any,
  config?: AxiosRequestConfig
) {
  let res: any;
  if (method === 'get' || method === 'delete') {
    res = await instance[method](url, config);
  } else {
    res = await instance[method](url, data, config);
  }
  return res;
}
