import { serviceAdapter } from '@/utils';
import { request, mockRequest } from '../request';
import { adapterOfFetchDataWithAdapter } from '../adapter';

/** Request with adapter (data processing of request result) */
export async function fetchDataWithAdapter() {
  const res = await mockRequest.post<ApiDemo.DataWithAdapter>('/apiDemoWithAdapter');
  return serviceAdapter(adapterOfFetchDataWithAdapter, res);
}

/** Test the request behind the proxy */
export function testRequestWithProxy() {
  return request.get('/test-proxy'); // Make sure `${url}/test-proxy` composed of .env-config url and current address is a valid backend interface
}
