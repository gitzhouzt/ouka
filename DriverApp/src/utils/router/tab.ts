import { EnumStorageKey } from '@/enum';
import { setLocal, getLocal } from '../storage';

/** Cache multi-tab data */
export function setTabRoutes(data: GlobalTabRoute[]) {
	setLocal(EnumStorageKey['tab-routes'], data);
}

/** Get cached multi-tab data */
export function getTabRoutes() {
	const routes: GlobalTabRoute[] = [];
	const data = getLocal<GlobalTabRoute[]>(EnumStorageKey['tab-routes']);
	if (data) {
		const defaultTabRoutes = data.map((item) => ({
			...item,
			scrollPosition: {
				left: 0,
				top: 0,
			},
		}));
		routes.push(...defaultTabRoutes);
	}
	return routes;
}

/** Clear multi-tab data */
export function clearTabRoutes() {
	setTabRoutes([]);
}
