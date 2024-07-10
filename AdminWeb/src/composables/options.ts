import {
  EnumUserRole,
  EnumOrderStatus,
  EnumOrderType,
  EnumAirportType,
  EnumUserStatus,
  EnumFinanceType,
  EnumSex,
  EnumStatus
} from '@/enum';

export function useMyOptions() {
  function userRoleOptions() {
    const keys = Object.keys(EnumUserRole) as MyEnumType.EnumUserRoleKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumUserRoleKey) => {
      options.push({
        value: key,
        label: EnumUserRole[key]
      });
    });
    return options;
  }

  function addUserRoleOptions() {
    const keys = ['Driver', 'Seller', 'Operator', 'Accounting', 'OperationManager'] as MyEnumType.EnumUserRoleKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumUserRoleKey) => {
      options.push({
        value: key,
        label: EnumUserRole[key]
      });
    });
    return options;
  }

  function radioSexOptions() {
    const keys = Object.keys(EnumSex) as MyEnumType.EnumSexKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumSexKey) => {
      options.push({
        value: key,
        label: EnumSex[key]
      });
    });
    return options;
  }

  function userStatusOptions() {
    const keys = Object.keys(EnumUserStatus) as MyEnumType.EnumUserStatusKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumUserStatusKey) => {
      options.push({
        value: key,
        label: EnumUserStatus[key]
      });
    });
    return options;
  }

  function orderStatusOptions() {
    const keys = Object.keys(EnumOrderStatus) as MyEnumType.EnumOrderStatusKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumOrderStatusKey) => {
      options.push({
        value: key,
        label: EnumOrderStatus[key]
      });
    });
    return options;
  }

  function orderTypeOptions() {
    const keys = Object.keys(EnumOrderType) as MyEnumType.EnumOrderTypeKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumOrderTypeKey) => {
      options.push({
        value: key,
        label: EnumOrderType[key]
      });
    });
    return options;
  }

  function airportTypeOptions() {
    const keys = Object.keys(EnumAirportType) as MyEnumType.EnumAirportTypeKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumAirportTypeKey) => {
      options.push({
        value: key,
        label: EnumAirportType[key]
      });
    });
    return options;
  }

  function selectFinanceTypeOptions() {
    const keys = Object.keys(EnumFinanceType) as MyEnumType.EnumFinanceTypeKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumFinanceTypeKey) => {
      options.push({
        value: key,
        label: EnumFinanceType[key]
      });
    });
    return options;
  }

  function selectStatusOptions() {
    const keys = Object.keys(EnumStatus) as MyEnumType.EnumStatusKey[];
    const options: any = [];
    keys.forEach((key: MyEnumType.EnumStatusKey) => {
      options.push({
        value: key,
        label: EnumStatus[key]
      });
    });
    return options;
  }

  return {
    userRoleOptions: userRoleOptions(),
    addUserRoleOptions: addUserRoleOptions(),
    userStatusOptions: userStatusOptions(),
    radioSexOptions: radioSexOptions(),

    orderStatusOptions: orderStatusOptions(),
    orderTypeOptions: orderTypeOptions(),
    airportTypeOptions: airportTypeOptions(),

    selectStatusOptions: selectStatusOptions(),
    selectFinanceTypeOptions: selectFinanceTypeOptions()
  };
}
