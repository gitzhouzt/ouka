declare namespace MyEnumType {
  /** 注文 */
  type EnumOrderStatusKey = keyof typeof import('@/enum').EnumOrderStatus;
  type EnumOrderTypeKey = keyof typeof import('@/enum').EnumOrderType;

  /** 利用者 */
  type EnumUserRoleKey = keyof typeof import('@/enum').EnumUserRole;
  type EnumSexKey = keyof typeof import('@/enum').EnumSex;
  type EnumUserStatusKey = keyof typeof import('@/enum').EnumUserStatus;

  type EnumDateTypeKey = keyof typeof import('@/enum').EnumDateType;
  type EnumStatusKey = keyof typeof import('@/enum').EnumStatus;
  type EnumFinanceStatusKey = keyof typeof import('@/enum').EnumFinanceStatus;
  type EnumCarStatusKey = keyof typeof import('@/enum').EnumCarStatus;
  type EnumFinanceTypeKey = keyof typeof import('@/enum').EnumFinanceType;
  type EnumTargetTypeKey = keyof typeof import('@/enum').EnumTargetType;
}
