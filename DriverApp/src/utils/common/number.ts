/**
 * Get the corresponding Chinese character according to the number
 * @param num - number(0-10)
 */
export function getHanByNumber(num: number) {
	const HAN_STR = '零一二三四五六七八九十';
	return HAN_STR.charAt(num);
}

/**
 * Convert total seconds to minutes:seconds
 * @param seconds - s
 */
export function transformToTimeCountDown(seconds: number) {
	const SECONDS_A_MINUTE = 60;
	function fillZero(num: number) {
		return num.toString().padStart(2, '0');
	}
	const minuteNum = Math.floor(seconds / SECONDS_A_MINUTE);
	const minute = fillZero(minuteNum);
	const second = fillZero(seconds - minuteNum * SECONDS_A_MINUTE);
	return `${minute}: ${second}`;
}

/**
 * Get a random integer in the specified integer range
 * @param start - start range
 * @param end - end range
 */
export function getRandomInteger(end: number, start = 0) {
	const range = end - start;
	const random = Math.floor(Math.random() * range + start);
	return random;
}
