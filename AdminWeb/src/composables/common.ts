export function useMyCommon() {
  function isMobile() {
    return navigator?.userAgent?.indexOf('Mobile') >= 0;
  }

  function isWrap() {
    return document.documentElement.offsetWidth < 2000;
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

  function addSeparator(number: number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }
  return {
    isMobile: isMobile(),
    isWrap: isWrap(),
    addSeparator,
    toCDB
  };
}
