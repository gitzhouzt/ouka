import { EnumUserRole, EnumOrderStatus } from '@/enum';

export function useMyTags() {
	const TAG_TYPE = 'default' as
		| 'default'
		| 'danger'
		| 'primary'
		| 'success'
		| 'warning';

	function userRoleTagType(userRoleKey: MyEnumType.EnumUserRoleKey) {
		const role = EnumUserRole[userRoleKey];
		let tagType = TAG_TYPE;
		switch (role) {
			case EnumUserRole.Admin:
				tagType = 'danger';
				break;
			case EnumUserRole.Driver:
				tagType = 'primary';
				break;
			default:
				tagType = 'default';
				break;
		}
		return tagType;
	}

	function orderStatusTagType(orderStatusKey: MyEnumType.EnumOrderStatusKey) {
		const status = EnumOrderStatus[orderStatusKey];
		let tagType = TAG_TYPE;
		switch (status) {
			case EnumOrderStatus.Filling:
				tagType = 'default';
				break;
			case EnumOrderStatus.Assigning:
			case EnumOrderStatus.Sending:
			case EnumOrderStatus.Check:
				tagType = 'warning';
				break;
			case EnumOrderStatus.Booked:
				tagType = 'primary';
				break;
			case EnumOrderStatus.Completed:
				tagType = 'success';
				break;
			case EnumOrderStatus.Cancelled:
				tagType = 'danger';
				break;
			default:
				tagType = 'default';
				break;
		}
		return tagType;
	}

	return {
		userRoleTagType,
		orderStatusTagType,
	};
}
