/** http header content-type */
export enum EnumContentType {
  json = 'application/json',
  formUrlencoded = 'application/x-www-form-urlencoded',
  formData = 'multipart/form-data'
}

/** storage key */
export enum EnumStorageKey {
  /** theme color */
  'theme-color' = '__THEME_COLOR__',
  /** user token */
  'token' = '__TOKEN__',
  /** user refresh token */
  'refresh-token' = '__REFRESH_TOKEN__',
  /** user info */
  'user-info' = '__USER_INFO__',
  /** tab routes */
  'tab-routes' = '__TAB_ROUTES__'
}

/** data type */
export enum EnumDataType {
  number = '[object Number]',
  string = '[object String]',
  boolean = '[object Boolean]',
  null = '[object Null]',
  undefined = '[object Undefined]',
  object = '[object Object]',
  array = '[object Array]',
  date = '[object Date]',
  regexp = '[object RegExp]',
  set = '[object Set]',
  map = '[object Map]'
}
