import type { PluginOption } from 'vite';
import vue from './vue';
import html from './html';
import unplugin from './unplugin';
import unocss from './unocss';
// import visualizer from './visualizer';
import compress from './compress';

/**
 * vite plugins
 * @param viteEnv - env
 * @param srcPath - src path
 */
export function setupVitePlugins(viteEnv: ImportMetaEnv, srcPath: string): (PluginOption | PluginOption[])[] {
  const plugins = [...vue, html(viteEnv), ...unplugin(srcPath), unocss];

  // if (viteEnv.VITE_VISUALIZER === 'true') {
  //   plugins.push(visualizer);
  // }
  if (viteEnv.VITE_COMPRESS === 'true') {
    plugins.push(compress(viteEnv));
  }

  return plugins;
}
