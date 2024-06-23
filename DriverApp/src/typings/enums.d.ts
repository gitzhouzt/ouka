declare namespace MyEnumType {
	/** アクセス ソート */
	type EnumAccessSortKey = keyof typeof import('@/enum').EnumAccessSort;

	/** 利用者 */
	type EnumUserRoleKey = keyof typeof import('@/enum').EnumUserRole;
	type EnumSexKey = keyof typeof import('@/enum').EnumSex;

	/** 注文 */
	type EnumOrderStatusKey = keyof typeof import('@/enum').EnumOrderStatus;
	type EnumOrderTypeKey = keyof typeof import('@/enum').EnumOrderType;
}
