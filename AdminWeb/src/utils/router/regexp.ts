/** Get the regex for the dynamic routing of the login page module */
export function getLoginModuleRegExp() {
  const modules: EnumType.LoginModuleKey[] = ['pwd-login', 'reset-pwd'];
  return modules.join('|');
}
