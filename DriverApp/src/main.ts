import { createApp } from 'vue';
import { setupStore } from './store';
import { setupAssets } from './plugins';
import { setupRouter } from './router';
import { setupDirectives } from './directives';
import App from './App.vue';

import 'vant/lib/index.css';
import 'vant/es/toast/style';
import 'vant/es/notify/style';
import 'vant/es/dialog/style';
import 'vant/es/popup/style';

async function setupApp() {
	// import assets: js、css
	setupAssets();

	const app = createApp(App);

	// store plugin: pinia
	setupStore(app);

	// vue custom directives
	setupDirectives(app);

	// vue router
	await setupRouter(app);

	// mount app
	app.mount('#app');
}

setupApp();
