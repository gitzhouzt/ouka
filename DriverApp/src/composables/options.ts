import { EnumUserRole, EnumAccessSort } from '@/enum';

export function useMyOptions() {
	function accessSortOptions() {
		const keys = Object.keys(EnumAccessSort) as MyEnumType.EnumAccessSortKey[];
		const options: any = [
			{
				value: '',
				text: 'ソート',
			},
		];
		keys.forEach((key: MyEnumType.EnumAccessSortKey) => {
			options.push({
				value: key,
				text: EnumAccessSort[key],
			});
		});
		return options;
	}

	function userRoleOptions() {
		const keys = Object.keys(EnumUserRole) as MyEnumType.EnumUserRoleKey[];
		const options: any = [
			{
				value: '',
				text: '選択してください',
			},
		];
		keys.forEach((key: MyEnumType.EnumUserRoleKey) => {
			options.push({
				value: key,
				text: EnumUserRole[key],
			});
		});
		return options;
	}

	function addUserRoleOptions() {
		const keys = ['User', 'Driver'] as MyEnumType.EnumUserRoleKey[];
		const options: any = [
			{
				value: '',
				text: '選択してください',
			},
		];
		keys.forEach((key: MyEnumType.EnumUserRoleKey) => {
			options.push({
				value: key,
				text: EnumUserRole[key],
			});
		});
		return options;
	}

	return {
		userRoleOptions: userRoleOptions(),
		addUserRoleOptions: addUserRoleOptions(),
		accessSortOptions: accessSortOptions(),
	};
}
