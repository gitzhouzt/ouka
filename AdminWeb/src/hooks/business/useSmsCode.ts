import { computed } from 'vue';
import { REGEXP_EMAIL } from '@/config';
import { fetchSmsCode } from '@/service';
import { useLoading } from '../common';
import useCountDown from './useCountDown';

export default function useSmsCode() {
  const { loading, startLoading, endLoading } = useLoading();
  const { counts, start, isCounting } = useCountDown(60);
  const initLabel = '確認コード';
  const countingLabel = (second: number) => `${second}s后再取得`;
  const label = computed(() => {
    let text = initLabel;
    if (loading.value) {
      text = '';
    }
    if (isCounting.value) {
      text = countingLabel(counts.value);
    }
    return text;
  });

  /** メールの形式を検証 */
  function isMailValid(mail: string) {
    let valid = true;
    if (mail.trim() === '') {
      window.$message?.error('メールを入力してください');
      valid = false;
    } else if (!REGEXP_EMAIL.test(mail)) {
      window.$message?.error('メールを正しく入力してください');
      valid = false;
    }
    return valid;
  }

  /**
   * メール検証コード
   * @param mail - メール
   */
  async function getSmsCode(mail: string) {
    const valid = isMailValid(mail);
    if (!valid || loading.value) return;
    startLoading();
    const { data } = await fetchSmsCode(mail);
    if (data) {
      window.$message?.success('送信しました');
      start();
    }
    endLoading();
  }

  return {
    label,
    start,
    isCounting,
    getSmsCode,
    loading
  };
}
