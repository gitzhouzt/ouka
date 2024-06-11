import { request } from '../request';

/**
 *
 * @param dictCode
 * @param itemCode
 * @returns
 */
export function fetchDictByCode(dictCode: string, itemCode: string) {
  const params = {
    dictCode,
    itemCode
  };
  return request.post<MyModel.DictItem>(`/base/dictItem/getByCode`, params);
}
