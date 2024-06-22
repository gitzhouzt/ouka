import qs from 'qs';
import FormData from 'form-data';
import { EnumContentType } from '@/enum';
import { isArray } from '../common';

/**
 * Transformation of request data
 * @param requestData - request data
 * @param contentType - request header Content-Type
 */
export async function transformRequestData(
	requestData: any,
	contentType?: string
) {
	// application/json types are not processed
	let data = requestData;
	// form type conversion
	if (contentType === EnumContentType.formUrlencoded) {
		data = qs.stringify(requestData);
	}
	// form-data type conversion
	if (contentType === EnumContentType.formData) {
		const key = Object.keys(requestData)[0];
		const file = requestData.data[key];
		data = await transformFile(file, key);
	}
	return data;
}

/**
 * Data conversion when the interface is the type of the uploaded file
 * @param file - single file or multiple files
 * @param key - file attribute name
 */
async function transformFile(file: File[] | File, key: string) {
	const formData = new FormData();
	if (isArray(file)) {
		// multiple files
		await Promise.all(
			(file as File[]).map((item) => {
				formData.append(key, item);
				return true;
			})
		);
	} else {
		// single file
		await formData.append(key, file);
	}
	return formData;
}
