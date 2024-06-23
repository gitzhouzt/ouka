import type { GlobalThemeOverrides } from 'naive-ui';
import { cloneDeep } from 'lodash-es';
import { getThemeColor, getColorPalette, addColorAlpha } from '@/utils';
import { themeSetting } from '@/settings';

/** getThemeSettings */
export function getThemeSettings() {
  const themeColor = getThemeColor() || themeSetting.themeColor;
  const info = themeSetting.isCustomizeInfoColor ? themeSetting.otherColor.info : getColorPalette(themeColor, 7);
  const otherColor = { ...themeSetting.otherColor, info };
  const setting = cloneDeep({ ...themeSetting, themeColor, otherColor });
  return setting;
}

type ColorType = 'primary' | 'info' | 'success' | 'warning' | 'error';
type ColorScene = '' | 'Suppl' | 'Hover' | 'Pressed' | 'Active';
type ColorKey = `${ColorType}Color${ColorScene}`;
type ThemeColor = Partial<Record<ColorKey, string>>;

interface ColorAction {
  scene: ColorScene;
  handler: (color: string) => string;
}

/** Get the colors corresponding to various scenes of the theme color */
function getThemeColors(colors: [ColorType, string][]) {
  const colorActions: ColorAction[] = [
    { scene: '', handler: color => color },
    { scene: 'Suppl', handler: color => color },
    { scene: 'Hover', handler: color => getColorPalette(color, 5) },
    { scene: 'Pressed', handler: color => getColorPalette(color, 7) },
    { scene: 'Active', handler: color => addColorAlpha(color, 0.1) }
  ];

  const themeColor: ThemeColor = {};

  colors.forEach(color => {
    colorActions.forEach(action => {
      const [colorType, colorValue] = color;
      const colorKey: ColorKey = `${colorType}Color${action.scene}`;
      themeColor[colorKey] = action.handler(colorValue);
    });
  });

  return themeColor;
}

/** getNaiveThemeOverrides */
export function getNaiveThemeOverrides(colors: Record<ColorType, string>): GlobalThemeOverrides {
  const { primary, success, warning, error } = colors;

  const info = themeSetting.isCustomizeInfoColor ? colors.info : getColorPalette(primary, 7);

  const themeColors = getThemeColors([
    ['primary', primary],
    ['info', info],
    ['success', success],
    ['warning', warning],
    ['error', error]
  ]);

  const colorLoading = primary;

  return {
    common: {
      ...themeColors
    },
    LoadingBar: {
      colorLoading
    }
  };
}
