import { encrypto, decrypto } from '../crypto';

interface StorageData {
	value: unknown;
	expire: number | null;
}

/** The default cache period is 7 days */
const DEFAULT_CACHE_TIME = 60 * 60 * 24 * 7;

export function setLocal(
	key: string,
	value: unknown,
	expire: number | null = DEFAULT_CACHE_TIME
) {
	const storageData: StorageData = {
		value,
		expire: expire !== null ? new Date().getTime() + expire * 1000 : null,
	};
	const json = encrypto(storageData);
	window.localStorage.setItem(key, json);
}

export function getLocal<T>(key: string) {
	const json = window.localStorage.getItem(key);
	if (json) {
		let storageData: StorageData | null = null;
		try {
			storageData = decrypto(json);
		} catch {
			// Prevent parsing failure
		}
		if (storageData) {
			const { value, expire } = storageData;
			// Return directly within the validity period
			if (expire === null || expire >= Date.now()) {
				return value as T;
			}
		}
		removeLocal(key);
		return null;
	}
	return null;
}

export function removeLocal(key: string) {
	window.localStorage.removeItem(key);
}

export function clearLocal() {
	window.localStorage.clear();
}
