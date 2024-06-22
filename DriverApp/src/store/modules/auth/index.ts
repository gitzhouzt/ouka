import { unref } from 'vue';
import { defineStore } from 'pinia';
import { showNotify } from 'vant';
import { router } from '@/router';
import { useRouterPush } from '@/composables';
import { fetchLogin } from '@/service';
import {
	getUserInfo,
	getToken,
	setUserInfo,
	setToken,
	setRefreshToken,
	clearAuthStorage,
} from '@/utils';
import { useRouteStore } from '../route';

interface AuthState {
	/** user info */
	userInfo: Auth.UserInfo;
	/** user token */
	token: string;
	/** login Loading */
	loginLoading: boolean;
}

export const useAuthStore = defineStore('admin-auth-store', {
	state: (): AuthState => ({
		userInfo: getUserInfo(),
		token: getToken(),
		loginLoading: false,
	}),
	getters: {
		/** isLogin */
		isLogin(state) {
			return Boolean(state.token);
		},
	},
	actions: {
		/** reset auth */
		async resetAuthStore() {
			const { toLogin } = useRouterPush(false);
			const { resetRouteStore } = useRouteStore();

			const route = unref(router.currentRoute);

			clearAuthStorage();
			this.$reset();

			resetRouteStore();

			if (route.meta.requiresAuth) {
				toLogin();
			}
		},

		async resetAuthStoreToHome() {
			const { toHome } = useRouterPush(false);
			const { resetRouteStore } = useRouteStore();

			clearAuthStorage();
			this.$reset();

			resetRouteStore();

			toHome();
		},
		/**
		 * Logic to handle success or failure after login
		 * @param backendToken - token
		 */
		async handleActionAfterLogin(backendToken: ApiAuth.LoginUser) {
			const { toLoginRedirect } = useRouterPush(false);

			const loginSuccess = await this.loginByToken(backendToken);

			if (loginSuccess) {
				// Jump to the address after login
				toLoginRedirect();

				// A welcome prompt pops up after successful login
				showNotify({
					type: 'success',
					message: 'ログインしました', // `お帰り，${this.userInfo.userName}!`,
				});

				return;
			}

			// If unsuccessful, reset the state
			this.resetAuthStore();
		},
		/**
		 * Login by token
		 * @param backendToken - token
		 */
		async loginByToken(backendToken: ApiAuth.LoginUser) {
			let successFlag = false;

			// First store the token in the cache (the request header of the following interface requires the token)
			const { tokenVO, userVO } = backendToken;
			setToken(tokenVO.token);
			setRefreshToken(tokenVO.tokenRefresh);

			// get user info
			if (userVO) {
				// After success, store user information in the cache
				setUserInfo(userVO);

				// update status
				this.userInfo = userVO;
				this.token = tokenVO.token;

				successFlag = true;
			}

			return successFlag;
		},
		/**
		 * ログイン
		 * @param userName - ユーザー
		 * @param password - パスワード
		 */
		async login(userName: string, password: string) {
			this.loginLoading = true;
			const { data } = await fetchLogin(userName, password);
			if (data) {
				await this.handleActionAfterLogin(data);
			}
			this.loginLoading = false;
		},
		/**
		 * Change user permissions (switch accounts)
		 * @param userRole
		 */
		async updateUserRole(userRole: Auth.RoleType) {
			const { resetRouteStore, initAuthRoute } = useRouteStore();

			const accounts: Record<
				Auth.RoleType,
				{ userName: string; password: string }
			> = {
				Super: {
					userName: 'Super',
					password: 'super123',
				},
				Admin: {
					userName: 'Admin',
					password: 'admin123',
				},
				Driver: {
					userName: 'User01',
					password: 'user01123',
				},
			};
			const { userName, password } = accounts[userRole];
			const { data } = await fetchLogin(userName, password);
			if (data) {
				await this.loginByToken(data);
				resetRouteStore();
				initAuthRoute();
			}
		},
	},
});
