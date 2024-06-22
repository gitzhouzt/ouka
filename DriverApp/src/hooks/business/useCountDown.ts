import { ref, computed } from 'vue';
import { useBoolean } from '../common';

/**
 * カウントダウン
 * @param second - 秒(s)
 */
export default function useCountDown(second: number) {
	if (second <= 0 && second % 1 !== 0) {
		throw Error('カウントダウンは正整数型での必要があります');
	}
	const { bool: isComplete, setTrue, setFalse } = useBoolean(false);

	const counts = ref(0);
	const isCounting = computed(() => Boolean(counts.value));

	let intervalId: any;

	/**
	 * はじめる
	 * @param updateSecond - 時間
	 */
	function start(updateSecond: number = second) {
		if (!counts.value) {
			setFalse();
			counts.value = updateSecond;
			intervalId = setInterval(() => {
				counts.value -= 1;
				if (counts.value <= 0) {
					clearInterval(intervalId);
					setTrue();
				}
			}, 1000);
		}
	}

	/**
	 * 止める
	 */
	function stop() {
		intervalId = clearInterval(intervalId);
		counts.value = 0;
	}

	return {
		counts,
		isCounting,
		start,
		stop,
		isComplete,
	};
}
