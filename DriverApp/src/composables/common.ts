export function useMyCommon() {
	function isMobile() {
		return navigator?.userAgent?.indexOf('Mobile') >= 0;
	}

	function toCDB(str: string) {
		let tmp = '';
		for (let i = 0; i < str.length; ) {
			if (str.charCodeAt(i) > 65248 && str.charCodeAt(i) < 65375) {
				tmp += String.fromCharCode(str.charCodeAt(i) - 65248);
			} else {
				tmp += String.fromCharCode(str.charCodeAt(i));
			}
			i += 1;
		}
		return tmp;
	}
	return {
		isMobile: isMobile(),
		toCDB,
	};
}
