/** 携帯 */
export const REGEXP_PHONE = /^\d{10,11}$/;

/** メール */
export const REGEXP_EMAIL = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;

/** パスワード(6-18/文字列/シンボル) */
export const REGEXP_PWD =
	/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[()])+$)(?!^.*[\u4E00-\u9FA5].*$)([^(0-9a-zA-Z)]|[()]|[a-z]|[A-Z]|[0-9]){6,18}$/;

/** 6実数 検証コード */
export const REGEXP_CODE_SIX = /^\d{6}$/;

/** 4実数  検証コード */
export const REGEXP_CODE_FOUR = /^\d{4}$/;

/** url リンク */
export const REGEXP_URL =
	/(((^https?:(?:\/\/)?)(?:[-;:&=+$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-;:&=+$,\w]+@)[A-Za-z0-9.-]+)((?:\/[+~%/.\w-_]*)?\??(?:[-+=&;%@.\w_]*)#?(?:[\w]*))?)$/;

/** 郵便番号 */
export const REGEXP_POST = /^\d{3}-\d{4}$/;
