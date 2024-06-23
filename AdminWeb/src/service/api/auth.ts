import { mockRequest, request } from '../request';

/**
 * 検証コード
 * @param mail - メール
 * @returns - boolean
 */
export function fetchSmsCode(mail: string) {
  return mockRequest.post<boolean>('/getSmsCode', { mail });
}

/**
 * ログイン
 * @param loginName - ユーザー
 * @param loginPass - パスワード
 */
export function fetchLogin(loginName: string, loginPass: string) {
  return request.post<ApiAuth.LoginUser>('/login', { loginName, loginPass });
}

/** ユーザー 情報 */
export function fetchUserInfo() {
  // return mockRequest.get<ApiAuth.UserInfo>('/getUserInfo');
}

/**
 * ユーザー ルート
 * @param userRoles - ユーザー id
 * @description ユーザー メニュー
 */
export function fetchUserRoutes(userRoles: string) {
  return mockRequest.post<ApiRoute.Route>('/getUserRoutes', { userRoles });
}

/**
 * refresh Token
 * @param tokenRefresh
 */
export function fetchUpdateToken(tokenRefresh: string) {
  return request.get<ApiAuth.Token>(`/updateToken/${tokenRefresh}`);
}

/**
 * logout
 */
export function fetchLogout() {
  return request.post<ApiAuth.Token>('/logout');
}
