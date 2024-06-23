export function getParamsUrl(params: any) {
	let strParams = '?';
	Object.keys(params).forEach((p) => {
		if (params[p] !== '') {
			strParams += `${p}=${params[p]}&`;
		}
	});
	return `${strParams.substring(0, strParams.length - 1)}`;
}
