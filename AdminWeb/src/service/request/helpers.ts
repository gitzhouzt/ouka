import type { AxiosRequestConfig } from 'axios';
import { useAuthStore } from '@/store';
import { getRefreshToken, setToken, setRefreshToken } from '@/utils';
import { fetchUpdateToken } from '../api';

/**
 * Refresh Token
 * @param axiosConfig - token Request configuration on failure
 */
export async function handleRefreshToken(axiosConfig: AxiosRequestConfig) {
  const { resetAuthStore } = useAuthStore();
  const refreshToken = getRefreshToken();
  const { data } = await fetchUpdateToken(refreshToken);
  if (data) {
    setToken(data.token);
    setRefreshToken(data.tokenRefresh);
    const config = { ...axiosConfig };
    if (config.headers) {
      config.headers.Authorization = data.token;
    }
    return config;
  }

  resetAuthStore();
  return null;
}
