import axios from 'axios';
import type { AxiosRequestConfig, AxiosInstance, AxiosError } from 'axios';
import { REFRESH_TOKEN_CODE, LOGIN_LOGOUT_CODE, NOT_LOGIN_CODE } from '@/config';
import { useAuthStore } from '@/store';
import {
  getToken,
  transformRequestData,
  handleAxiosError,
  handleResponseError,
  handleBackendError,
  handleServiceResult
} from '@/utils';
import { handleRefreshToken } from './helpers';

/**
 * Encapsulate axios request class
 * @author Soybean<honghuangdc@gmail.com>
 */
export default class CustomAxiosInstance {
  instance: AxiosInstance;

  backendConfig: Service.BackendResultConfig;

  /**
   *
   * @param axiosConfig - axios config
   * @param backendConfig - Data configuration returned by the backend
   */
  constructor(
    axiosConfig: AxiosRequestConfig,
    backendConfig: Service.BackendResultConfig = {
      codeKey: 'code',
      dataKey: 'data',
      msgKey: 'message',
      successCode: 200
    }
  ) {
    this.backendConfig = backendConfig;
    this.instance = axios.create(axiosConfig);
    this.setInterceptor();
  }

  /** Set up a request interceptor */
  setInterceptor() {
    this.instance.interceptors.request.use(
      async config => {
        const handleConfig = { ...config };
        if (handleConfig.headers) {
          // data conversion
          const contentType = handleConfig.headers['Content-Type'] as string;
          handleConfig.data = await transformRequestData(handleConfig.data, contentType);
          // set token
          handleConfig.headers.Authorization = getToken();
        }
        return handleConfig;
      },
      (axiosError: AxiosError) => {
        const error = handleAxiosError(axiosError);
        return handleServiceResult(error, null);
      }
    );
    this.instance.interceptors.response.use(
      async response => {
        const { status } = response;
        if (status === 200 || status < 300 || status === 304) {
          const backend = response.data;

          const { codeKey, dataKey, successCode } = this.backendConfig;

          // blob
          if (response.headers['content-disposition']) {
            const str = response.headers['content-disposition'];
            const params = {
              file: backend,
              filename: str.split('filename=')[1]
            };
            return handleServiceResult(null, params);
          }

          // request succeeded
          if (backend[codeKey] === successCode || LOGIN_LOGOUT_CODE === backend[codeKey]) {
            return handleServiceResult(null, backend[dataKey]);
          }

          // The token is invalid, refresh the token
          if (REFRESH_TOKEN_CODE === backend[codeKey]) {
            const config = await handleRefreshToken(response);
            if (config) {
              return handleServiceResult(null, backend[dataKey]);
              // return this.instance.request(config);
            }
          }

          if (NOT_LOGIN_CODE.includes(backend[codeKey])) {
            const auth = useAuthStore();
            auth.resetAuthStore();
          }

          const error = handleBackendError(backend, this.backendConfig);
          return handleServiceResult(error, null);
        }
        const error = handleResponseError(response);
        return handleServiceResult(error, null);
      },
      (axiosError: AxiosError) => {
        const error = handleAxiosError(axiosError);
        return handleServiceResult(error, null);
      }
    );
  }
}
