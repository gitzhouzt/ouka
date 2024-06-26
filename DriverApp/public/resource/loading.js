/**
 * svg logo
 * @param {string} id - el id
 */
function initSvgLogo(id) {
	const svgStr = `<svg width="128px" height="128px" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px"
	y="0px" viewBox="0 0 158.9 158.9" style="enable-background:new 0 0 158.9 158.9;" xml:space="preserve">
	<path style="fill:none" d="M0,158.9C0,106.3,0,53.7,0,1.1C0,0.2,0.2,0,1.1,0c52.2,0,104.5,0,156.7,0c0.9,0,1.1,0.2,1.1,1.1
	c0,52.2,0,104.5,0,156.7c0,0.9-0.2,1.1-1.1,1.1C105.2,158.8,52.6,158.8,0,158.9z" />
	<path style="fill:currentColor" d="M81.3,55.9c-0.1-11.7-2.9-22.5-9.4-32.4c-1-1.5-2.1-2.9-2.5-4.7c-0.7-3.4,0.9-6.9,4-8.6c3-1.7,6.8-1.2,9.3,1.2
	c2.4,2.6,4.4,5.6,5.9,8.8c4.7,8.9,7.6,18.6,8.4,28.6c1,12.5-0.7,25-5.2,36.7c-0.9,2.5-1.9,4.9-3,7.3c-0.3,0.4-0.3,1,0,1.4
	c9.6,13.3,21.8,23,37.8,27.2c6.4,1.7,13.1,2.3,19.7,1.6c4.2-0.4,7.9,2.7,8.4,6.9c0.7,4.3-2.3,8.3-6.6,9c0,0,0,0-0.1,0
	c-7.7,0.9-15.5,0.5-23-1.3c-13.9-3.1-26.7-10-36.9-19.9c-4.4-4.2-8.4-8.8-11.9-13.7c-0.5-0.8-1.4-1.2-2.3-1.1
	c-9.5,0.7-18.8,3.3-27.4,7.6c-11.6,6-20.7,14.6-26.4,26.4c-0.7,1.9-2,3.5-3.7,4.7c-2.9,1.7-6.6,1.5-9.2-0.7c-2.8-2.2-3.8-6-2.4-9.3
	c2.2-5.2,5.1-10.1,8.7-14.5c12.2-15.4,28.2-24.6,47.3-28.6c4-0.8,8.1-1.4,12.2-1.6c0.5,0,1-0.3,1.2-0.8c3.3-7.1,5.5-14.6,6.5-22.3
	C81.1,61.2,81.3,58.6,81.3,55.9z" />
	<path style="fill:currentColor" d="M136.3,108.3c-3.8-0.5-7.6-1.4-11.1-2.9c-7.7-2.8-14.4-7.5-19.7-13.8c-2.9-3.3-2.5-8.4,0.8-11.3
	c1.4-1.2,3.1-1.9,4.9-1.9c2.5-0.1,5,1,6.5,2.9c4.9,5.6,11.6,9.4,18.9,10.8c1.5,0.2,3.1,0.6,4.5,1.2c3.2,1.8,4.8,5.6,3.8,9.2
	C144,106.1,140.8,108.4,136.3,108.3z" />
	<path style="fill:currentColor" d="M55.7,33.3c3,0.2,5.6,2.2,6.6,5c2.2,5.4,3.4,11.2,3.6,17c0.3,5.9-0.6,11.7-2.5,17.3c-2,5.8-8.2,7.8-12.9,4.2
	c-2.6-2.2-3.6-5.8-2.4-9c1.4-4,1.9-8.2,1.7-12.4c-0.2-3.8-1-7.5-2.4-11C45.3,38.9,49.2,33.3,55.7,33.3z" />
	<path style="fill:currentColor" d="M77.9,126.6c0,3.9-2.8,7.2-6.7,7.9c-7.8,1.5-14.8,5.9-19.7,12.2c-2.7,3.5-7.6,4.2-11.2,1.6
	c-3.6-2.6-4.3-7.6-1.7-11.2c0.1-0.1,0.2-0.3,0.3-0.4c4.1-5.2,9.3-9.6,15.1-12.8c4.4-2.5,9.1-4.2,14-5.1
	C73.3,117.7,77.9,121.3,77.9,126.6z" />
	</svg>`;
	const appEl = document.querySelector(id);
	const div = document.createElement('div');
	div.innerHTML = svgStr;
	if (appEl) {
		appEl.appendChild(div);
	}
}

function addThemeColorCssVars() {
	const key = '__THEME_COLOR__';
	const defaultColor = '#fb8c00';
	const themeColor = window.localStorage.getItem(key) || defaultColor;
	console.log('themeColor: ', themeColor);
	const cssVars = `--primary-color: ${themeColor}`;
	document.documentElement.style.cssText = cssVars;
}

addThemeColorCssVars();

initSvgLogo('#loadingLogo');
