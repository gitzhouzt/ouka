import { watch, onUnmounted } from 'vue';
import { useOsTheme } from 'naive-ui';
import type { GlobalThemeOverrides } from 'naive-ui';
import { useElementSize } from '@vueuse/core';
import { kebabCase } from 'lodash-es';
import { setThemeColor } from '@/utils';
import { useThemeStore } from '../modules';

/** subscribe theme store */
export default function subscribeThemeStore() {
  const theme = useThemeStore();
  const osTheme = useOsTheme();
  const { width } = useElementSize(document.documentElement);
  const { addDarkClass, removeDarkClass } = handleCssDarkMode();

  // watch Theme Color
  const stopThemeColor = watch(
    () => theme.themeColor,
    newValue => {
      setThemeColor(newValue);
    },
    { immediate: true }
  );

  // watch naiveUI themeOverrides
  const stopThemeOverrides = watch(
    () => theme.naiveThemeOverrides,
    newValue => {
      if (newValue.common) {
        addThemeCssVarsToHtml(newValue.common);
      }
    },
    { immediate: true }
  );

  // watch DarkMode
  const stopDarkMode = watch(
    () => theme.darkMode,
    newValue => {
      if (newValue) {
        addDarkClass();
      } else {
        removeDarkClass();
      }
    },
    {
      immediate: true
    }
  );

  // watch OsTheme
  const stopOsTheme = watch(
    osTheme,
    newValue => {
      const isDark = newValue === 'dark';
      theme.setAutoFollowSystemMode(isDark);
    },
    { immediate: true }
  );

  // Disable horizontal scrolling
  // (when the page is switched, the transition animation will generate a horizontal scroll bar,
  // when it is less than the minimum width, it is not prohibited)
  const stopWidth = watch(width, newValue => {
    if (newValue < theme.layout.minWidth) {
      document.documentElement.style.overflowX = 'auto';
    } else {
      document.documentElement.style.overflowX = 'hidden';
    }
  });

  onUnmounted(() => {
    stopThemeColor();
    stopThemeOverrides();
    stopDarkMode();
    stopOsTheme();
    stopWidth();
  });
}

/** css DarkMode */
function handleCssDarkMode() {
  const DARK_CLASS = 'dark';
  function addDarkClass() {
    document.documentElement.classList.add(DARK_CLASS);
  }
  function removeDarkClass() {
    document.documentElement.classList.remove(DARK_CLASS);
  }
  return {
    addDarkClass,
    removeDarkClass
  };
}

type ThemeVars = Exclude<GlobalThemeOverrides['common'], undefined>;
type ThemeVarsKeys = keyof ThemeVars;

/** add css vars to html */
function addThemeCssVarsToHtml(themeVars: ThemeVars) {
  const keys = Object.keys(themeVars) as ThemeVarsKeys[];
  const style: string[] = [];
  keys.forEach(key => {
    style.push(`--${kebabCase(key)}: ${themeVars[key]}`);
  });
  const styleStr = style.join(';');
  document.documentElement.style.cssText += styleStr;
}
