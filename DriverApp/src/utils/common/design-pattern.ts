/** execution strategy pattern */
export function exeStrategyActions(actions: Common.StrategyAction[]) {
	actions.some((item) => {
		const [flag, action] = item;
		if (flag) {
			action();
		}
		return flag;
	});
}
