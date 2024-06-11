/** timeout time */
export const REQUEST_TIMEOUT = 60 * 1000;

/** error msg duration time */
export const ERROR_MSG_DURATION = 3 * 1000;

/** default error code */
export const DEFAULT_REQUEST_ERROR_CODE = 'DEFAULT';

/** default error msg */
export const DEFAULT_REQUEST_ERROR_MSG = 'リクエスト エラー';

/** timeout code (DEFAULT：ECONNABORTED) */
export const REQUEST_TIMEOUT_CODE = 'ECONNABORTED';

/** timeout msg */
export const REQUEST_TIMEOUT_MSG = 'タイムアウト';

/** network error  code */
export const NETWORK_ERROR_CODE = 'NETWORK_ERROR';

/** network error msg */
export const NETWORK_ERROR_MSG = 'ネットワーク エラー';

/** ERROR STATUS */
export const ERROR_STATUS = {
  400: '400: リクエストに構文エラー',
  401: '401: ユーザーが許可されていません',
  403: '403: サーバーがアクセスを拒否しました',
  404: '404: 見つかりませんでした',
  405: '405: リクエスト方法は許可されていません',
  408: '408: リクエストがタイムアウト',
  500: '500: システムエラーです。しばらくお待ちください。',
  501: '501: システムエラーです。しばらくお待ちください。',
  502: '502: システムエラーです。しばらくお待ちください。',
  503: '503: サービスはご利用いただけません',
  504: '504: ゲートウェイがタイムアウト',
  505: '505: HTTPバージョンはリクエストをサポートしていません',
  [DEFAULT_REQUEST_ERROR_CODE]: DEFAULT_REQUEST_ERROR_MSG
};

/** NO_ERROR_MSG_CODE */
export const NO_ERROR_MSG_CODE: (string | number)[] = [2000, 1000];

/** REFRESH_TOKEN_CODE */
export const REFRESH_TOKEN_CODE: string | number = 2000;

/** LOGIN_LOGOUT_CODE */
export const LOGIN_LOGOUT_CODE: string | number = 1000;

/** NOT_LOGIN_CODE */
export const NOT_LOGIN_CODE: (string | number)[] = [999];
