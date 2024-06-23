import { h } from 'vue';
import { Icon } from '@iconify/vue';

/**
 * Dynamic rendering iconify
 * @param icon - icon name
 * @param color - icon color
 * @param size - icon size
 */
export function iconifyRender(icon: string, color?: string, size?: number) {
  const style: { color?: string; size?: string } = {};
  if (color) {
    style.color = color;
  }
  if (size) {
    style.size = `${size}px`;
  }
  return () => h(Icon, { icon, style });
}
