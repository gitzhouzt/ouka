/** Get the regex for the dynamic routing of the login page module */
export function getLoginModuleRegExp() {
	const modules: EnumType.LoginModuleKey[] = ['user-login', 'staff-login'];
	return modules.join('|');
}
