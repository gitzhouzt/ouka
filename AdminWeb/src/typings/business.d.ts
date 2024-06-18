/** User related modules */
declare namespace Auth {
  /**
   * User role type (front-end static routing uses role type to control routing permissions)
   * - super: Super administrator (this permission has all routing data)
   * - admin: administrator
   * - user: user
   * - custom: custom role
   */
  type RoleType = keyof typeof import('@/enum').EnumUserRole;

  /** user info */
  interface UserInfo {
    /** id */
    id: string;
    /** user name */
    userName: string;
    /** user email */
    userEmail: string;
    /** user email */
    userPhone: string;
    /** user avatar */
    userAvatar: string;
    /** user role */
    userRoles: string;
  }
}

declare namespace Demo {
  interface DataWithAdapter {
    id: string;
    name: string;
  }
}
