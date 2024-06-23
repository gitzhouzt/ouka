import { request } from '../request';

/**
 *  dictItem
 * @param dictCode
 */
export function getDictItems(dictCode: string) {
	return request.post<MyModel.MyResultArray<MyModel.DictItem>>(
		`/base/dictItem/list`,
		{
			dictCode,
			pageSize: 999,
		}
	);
}
