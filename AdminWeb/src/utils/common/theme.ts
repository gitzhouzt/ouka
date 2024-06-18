import { EnumStorageKey } from '@/enum';

/**
 * cache theme color
 * @param color
 */
export function setThemeColor(color: string) {
  window.localStorage.setItem(EnumStorageKey['theme-color'], color);
}

/**
 * get cache theme color
 */
export function getThemeColor() {
  return window.localStorage.getItem(EnumStorageKey['theme-color']);
}
