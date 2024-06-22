import type { AxiosError, AxiosResponse } from 'axios';
import {
	DEFAULT_REQUEST_ERROR_CODE,
	DEFAULT_REQUEST_ERROR_MSG,
	NETWORK_ERROR_CODE,
	NETWORK_ERROR_MSG,
	REQUEST_TIMEOUT_CODE,
	REQUEST_TIMEOUT_MSG,
	ERROR_STATUS,
} from '@/config';
import { exeStrategyActions } from '../common';
import { showErrorMsg } from './msg';

type ErrorStatus = keyof typeof ERROR_STATUS;

/**
 * Handling axios request failed errors
 * @param axiosError
 */
export function handleAxiosError(axiosError: AxiosError) {
	const error: Service.RequestError = {
		type: 'axios',
		code: DEFAULT_REQUEST_ERROR_CODE,
		msg: DEFAULT_REQUEST_ERROR_MSG,
	};

	const actions: Common.StrategyAction[] = [
		[
			// network error
			!window.navigator.onLine || axiosError.message === 'Network Error',
			() => {
				Object.assign(error, {
					code: NETWORK_ERROR_CODE,
					msg: NETWORK_ERROR_MSG,
				});
			},
		],
		[
			// timeout error
			axiosError.code === REQUEST_TIMEOUT_CODE &&
				axiosError.message.includes('timeout'),
			() => {
				Object.assign(error, {
					code: REQUEST_TIMEOUT_CODE,
					msg: REQUEST_TIMEOUT_MSG,
				});
			},
		],
		[
			// request failed error
			Boolean(axiosError.response),
			() => {
				const errorCode: ErrorStatus =
					(axiosError.response?.status as ErrorStatus) || 'DEFAULT';
				const msg = ERROR_STATUS[errorCode];
				Object.assign(error, { code: errorCode, msg });
			},
		],
	];

	exeStrategyActions(actions);

	showErrorMsg(error);

	return error;
}

/**
 * Handle errors after successful request
 * @param response
 */
export function handleResponseError(response: AxiosResponse) {
	const error: Service.RequestError = {
		type: 'axios',
		code: DEFAULT_REQUEST_ERROR_CODE,
		msg: DEFAULT_REQUEST_ERROR_MSG,
	};

	if (!window.navigator.onLine) {
		// network error
		Object.assign(error, { code: NETWORK_ERROR_CODE, msg: NETWORK_ERROR_MSG });
	} else {
		// The status code of the request is not 200 error
		const errorCode: ErrorStatus = response.status as ErrorStatus;
		const msg = ERROR_STATUS[errorCode] || DEFAULT_REQUEST_ERROR_MSG;
		Object.assign(error, { type: 'backend', code: errorCode, msg });
	}

	showErrorMsg(error);

	return error;
}

/**
 * Handle errors returned by the backend (business errors)
 * @param backendResult - Response data of backend interface
 */
export function handleBackendError(
	backendResult: Record<string, any>,
	config: Service.BackendResultConfig
) {
	const { codeKey, msgKey } = config;
	const error: Service.RequestError = {
		type: 'backend',
		code: backendResult[codeKey],
		msg: backendResult[msgKey],
	};

	showErrorMsg(error);

	return error;
}
