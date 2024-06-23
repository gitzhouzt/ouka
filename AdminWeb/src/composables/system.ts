import UAParser from 'ua-parser-js';
import { useAuthStore } from '@/store';
import { isArray, isString } from '@/utils';

interface AppInfo {
  /** プロジェクト 名前 */
  name: string;
  /** プロジェクト タイトル */
  title: string;
  /** プロジェクト 説明 */
  desc: string;
}

/** プロジェクト 情報 */
export function useAppInfo(): AppInfo {
  const { VITE_APP_NAME: name, VITE_APP_TITLE: title, VITE_APP_DESC: desc } = import.meta.env;

  return {
    name,
    title,
    desc
  };
}

/** デバイス 情報 */
export function useDeviceInfo() {
  const parser = new UAParser();
  const result = parser.getResult();
  return result;
}

/** 許可判断 */
export function usePermission() {
  const auth = useAuthStore();

  function hasPermission(permission: Auth.RoleType | Auth.RoleType[]) {
    const { userRole } = auth.userInfo;

    let has = userRole === 'super';
    if (!has) {
      if (isArray(permission)) {
        has = (permission as Auth.RoleType[]).includes(userRole);
      }
      if (isString(permission)) {
        has = (permission as Auth.RoleType) === userRole;
      }
    }
    return has;
  }

  return {
    hasPermission
  };
}
