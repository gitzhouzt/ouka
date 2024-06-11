import { colord, extend } from 'colord';
import mixPlugin from 'colord/plugins/mix';
import type { HsvColor } from 'colord';

extend([mixPlugin]);

type ColorIndex = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10;

const hueStep = 2;
const saturationStep = 16;
const saturationStep2 = 5;
const brightnessStep1 = 5;
const brightnessStep2 = 15;
const lightColorCount = 5;
const darkColorCount = 4;

/**
 * Get palette color according to color (light to dark color from left to right, 6 is the main color number)
 * @param color - color
 * @param index - The corresponding color number of the palette (6 is the main color number)
 * @description The algorithm implementation is borrowed from the ant-design palette algorithm https://github.com/ant-design/ant-design/blob/master/components/style/color/colorPalette.less
 */
export function getColorPalette(color: string, index: ColorIndex) {
  if (index === 6) return color;

  const isLight = index < 6;
  const hsv = colord(color).toHsv();
  const i = isLight ? lightColorCount + 1 - index : index - lightColorCount - 1;

  const newHsv: HsvColor = {
    h: getHue(hsv, i, isLight),
    s: getSaturation(hsv, i, isLight),
    v: getValue(hsv, i, isLight)
  };

  return colord(newHsv).toHex();
}

/**
 * Get palette color all colors based on color
 * @param color - color
 */
export function getAllColorPalette(color: string) {
  const indexs: ColorIndex[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  return indexs.map(index => getColorPalette(color, index));
}

/**
 * get hue gradient
 * @param hsv - hsv format color value
 * @param i - relative distance from 6
 * @param isLight - Is it a bright color
 */
function getHue(hsv: HsvColor, i: number, isLight: boolean) {
  let hue: number;
  if (hsv.h >= 60 && hsv.h <= 240) {
    // Cool colors
    // Dodge and lighten Hue rotate clockwise for warmer
    // darker and darker Hue counter-clockwise is cooler
    hue = isLight ? hsv.h - hueStep * i : hsv.h + hueStep * i;
  } else {
    // warm colors
    // Dodge and lighten Hue counter-clockwise for warmer
    // darker and darker, the hue rotates clockwise and is cooler
    hue = isLight ? hsv.h + hueStep * i : hsv.h - hueStep * i;
  }
  if (hue < 0) {
    hue += 360;
  } else if (hue >= 360) {
    hue -= 360;
  }
  return hue;
}

/**
 * get saturation gradient
 * @param hsv - hsv format color value
 * @param i - relative distance from 6
 * @param isLight - Is it a bright color
 */
function getSaturation(hsv: HsvColor, i: number, isLight: boolean) {
  let saturation: number;
  if (isLight) {
    saturation = hsv.s - saturationStep * i;
  } else if (i === darkColorCount) {
    saturation = hsv.s + saturationStep;
  } else {
    saturation = hsv.s + saturationStep2 * i;
  }
  if (saturation > 100) {
    saturation = 100;
  }
  if (isLight && i === lightColorCount && saturation > 10) {
    saturation = 10;
  }
  if (saturation < 6) {
    saturation = 6;
  }
  return saturation;
}

/**
 * Get lightness gradient
 * @param hsv - hsv format color value
 * @param i - relative distance from 6
 * @param isLight - Is it a bright color
 */
function getValue(hsv: HsvColor, i: number, isLight: boolean) {
  let value: number;
  if (isLight) {
    value = hsv.v + brightnessStep1 * i;
  } else {
    value = hsv.v - brightnessStep2 * i;
  }
  if (value > 100) {
    value = 100;
  }
  return value;
}

/**
 * Add transparency to color
 * @param color - color
 * @param alpha - alpha(0 - 1)
 */
export function addColorAlpha(color: string, alpha: number) {
  return colord(color).alpha(alpha).toHex();
}

/**
 * color mixing
 * @param firstColor - firstColor
 * @param secondColor - secondColor
 * @param ratio - second color ratio
 */
export function mixColor(firstColor: string, secondColor: string, ratio: number) {
  return colord(firstColor).mix(secondColor, ratio).toHex();
}

/**
 * Is it white
 * @param color - color
 */
export function isWhiteColor(color: string) {
  return colord(color).isEqual('#ffffff');
}
