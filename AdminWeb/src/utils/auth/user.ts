import { EnumStorageKey } from '@/enum';
import { setLocal, getLocal, removeLocal } from '../storage';

/** set token */
export function setToken(token: string) {
  setLocal(EnumStorageKey.token, token);
}

/** get token */
export function getToken() {
  return getLocal<string>(EnumStorageKey.token) || '';
}

/** remove token */
export function removeToken() {
  removeLocal(EnumStorageKey.token);
}

/** get refresh token */
export function getRefreshToken() {
  return getLocal<string>(EnumStorageKey['refresh-token']) || '';
}

/** set refresh token */
export function setRefreshToken(token: string) {
  setLocal(EnumStorageKey['refresh-token'], token);
}

/** remove refresh token */
export function removeRefreshToken() {
  removeLocal(EnumStorageKey['refresh-token']);
}

/** get user info */
export function getUserInfo() {
  const emptyInfo: Auth.UserInfo = {
    id: '',
    userName: '',
    userEmail: '',
    userPhone: '',
    userAvatar: '',
    userRoles: ''
  };
  const userInfo: Auth.UserInfo = getLocal<Auth.UserInfo>(EnumStorageKey['user-info']) || emptyInfo;
  return userInfo;
}

/** set user info  */
export function setUserInfo(userInfo: Auth.UserInfo) {
  setLocal(EnumStorageKey['user-info'], userInfo);
}

/** remove uesr info  */
export function removeUserInfo() {
  removeLocal(EnumStorageKey['user-info']);
}

/** clear user cache */
export function clearAuthStorage() {
  removeToken();
  removeRefreshToken();
  removeUserInfo();
}
