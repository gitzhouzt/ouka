import type { App } from 'vue';
import { createPinia } from 'pinia';

/** setup vue store plugin: pinia. - [install plugin：pinia] */
export function setupStore(app: App) {
  const store = createPinia();
  app.use(store);
}

export * from './modules';
export * from './subscribe';
