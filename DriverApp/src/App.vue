<template>
	<van-config-provider :theme-vars="themeVars">
		<router-view /></van-config-provider>
</template>

<script setup lang="ts">
import { Locale } from 'vant';
import type { ConfigProviderThemeVars } from 'vant';
import jaJP from 'vant/es/locale/lang/ja-JP';

const themeVars: ConfigProviderThemeVars = {
	dropdownMenuTitleFontSize: '18px',
	cellFontSize: '16px',
	cellGroupTitleFontSize: '16px',
	pickerOptionFontSize: '18px',
	pickerActionFontSize: '16px',
};
const messages = {
	'ja-JP': {
		// vanPicker: {
		// 	confirm: '閉じる', // 将'确认'修改为'关闭'
		// },
		// vanDialog: {
		// 	confirm: '閉じる', // 将'确认'修改为'关闭'
		// },
	},
};

Locale.use('ja-JP', jaJP);
Locale.add(messages);
</script>
<style>
body {
	font-size: 24px;
	font-family: 'ヒラギノ角ゴ Pro W3', 'Hiragino Kaku Gothic Pro', 'メイリオ',
		Meiryo, Osaka, 'ＭＳ Ｐゴシック', 'MS PGothic', sans-serif;
	background-color: #f8f8f8;
	-webkit-font-smoothing: antialiased;
}

:root {
	--van-base-font: 'ヒラギノ角ゴ Pro W3', 'Hiragino Kaku Gothic Pro', 'メイリオ',
		Meiryo, Osaka, 'ＭＳ Ｐゴシック', 'MS PGothic', sans-serif;
}
</style>
