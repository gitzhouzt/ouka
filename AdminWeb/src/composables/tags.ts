import { EnumOrderStatus, EnumCarStatus, EnumUserRole, EnumUserStatus, EnumStatus, EnumFinanceStatus } from '@/enum';

export function useMyTags() {
  const TAG_TYPE = 'default' as 'default' | 'error' | 'primary' | 'info' | 'success' | 'warning';

  function orderStatusTagType(key: MyEnumType.EnumOrderStatusKey) {
    const status = EnumOrderStatus[key];
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
        tagType = 'info';
        break;
      case EnumOrderStatus.Completed:
        tagType = 'success';
        break;
      case EnumOrderStatus.Cancelled:
        tagType = 'error';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  function userRoleTagType(key: MyEnumType.EnumUserRoleKey) {
    const role = EnumUserRole[key];
    let tagType = TAG_TYPE;
    switch (role) {
      case EnumUserRole.Super:
      case EnumUserRole.Admin:
        tagType = 'error';
        break;
      case EnumUserRole.Driver:
      case EnumUserRole.Seller:
      case EnumUserRole.Operator:
      case EnumUserRole.OperationManager:
        tagType = 'info';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  function userStatusTagType(key: MyEnumType.EnumUserStatusKey) {
    const status = EnumUserStatus[key];
    let tagType = TAG_TYPE;
    switch (status) {
      case EnumUserStatus.Retirement:
      case EnumUserStatus.Ban:
        tagType = 'error';
        break;
      case EnumUserStatus.Resting:
        tagType = 'warning';
        break;
      case EnumUserStatus.Working:
        tagType = 'info';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  function carStatusTagType(key: MyEnumType.EnumCarStatusKey) {
    const status = EnumCarStatus[key];
    let tagType = TAG_TYPE;
    switch (status) {
      case EnumCarStatus.Sell:
      case EnumCarStatus.Scrapped:
        tagType = 'error';
        break;
      case EnumCarStatus.Check:
      case EnumCarStatus.Repair:
        tagType = 'warning';
        break;
      case EnumCarStatus.Standing:
        tagType = 'success';
        break;
      case EnumCarStatus.Working:
        tagType = 'info';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  function statusTagType(key: MyEnumType.EnumStatusKey) {
    const status = EnumStatus[key];
    let tagType = TAG_TYPE;
    switch (status) {
      case EnumStatus.Filling:
        tagType = 'default';
        break;
      case EnumStatus.Completed:
        tagType = 'success';
        break;
      case EnumStatus.Waiting:
        tagType = 'info';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  function financeStatusTagType(key: MyEnumType.EnumFinanceStatusKey) {
    const status = EnumFinanceStatus[key];
    let tagType = TAG_TYPE;
    switch (status) {
      case EnumFinanceStatus.Completed:
        tagType = 'success';
        break;
      case EnumFinanceStatus.Waiting:
        tagType = 'warning';
        break;
      default:
        tagType = 'default';
        break;
    }
    return tagType;
  }

  return {
    orderStatusTagType,
    userRoleTagType,
    userStatusTagType,
    carStatusTagType,
    statusTagType,
    financeStatusTagType
  };
}
