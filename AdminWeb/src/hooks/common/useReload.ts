import { nextTick } from 'vue';
import useBoolean from './useBoolean';

/** reload */
export default function useReload() {
  // reload flag
  const { bool: reloadFlag, setTrue, setFalse } = useBoolean(true);
  /**
   * reload
   * @param duration - delay s
   */
  async function handleReload(duration = 0) {
    setFalse();
    await nextTick();
    setTimeout(() => {
      setTrue();
    }, duration);
  }

  return {
    reloadFlag,
    handleReload
  };
}
