declare namespace MySearch {
  interface SearchParams {
    keyword?: string;
    page?: number;
    pageSize?: number;
    selTime?: number[];
    beginTime?: Date | number | string;
    endTime?: Date | number | string;
    isAudit?: boolean;
    sortType?: Sorter;
    sort?: string;
  }

  interface UserSearchParams extends SearchParams {
    userRole?: string;
    restType?: string;
  }

  interface DriveSearchParams extends SearchParams {
    userRole?: string;
    driverType?: string;
  }

  interface TrainSearchParams extends SearchParams {
    trainYear?: Date | number | string;
  }

  interface CarSearchParams extends SearchParams {
    carType?: string;
    carType?: string;
  }

  interface DictSearchParams extends SearchParams {
    dictName?: string;
    dictCode?: string;
  }

  interface OrderSearchParams extends SearchParams {
    orderStatus?: MyEnumType.EnumOrderStatusKey[];
    orderDays?: number;
    orderStartTime?: string;
    orderSource?: string;
    orderKey?: string;
    sellerName?: string;
    driverName?: string;
    orderKey?: string;
    carNo?: string;
    city?: string;
    startBeginTime?: Date | number | string;
    startEndTime?: Date | number | string;
  }

  interface SkuSearchParams extends SearchParams {
    dateType?: 'day' | 'month' | 'year' | 'week' | 'week_prev' | 'week_next';
    workDate?: Date | number | string;
    targetType: MyEnumType.EnumTargetTypeKey;
  }

  interface AccidentSearchParams extends SearchParams {
    accidentType?: string;
    accidentTypeCode?: string;
    status?: MyEnumType.EnumStatusKey[];
  }

  interface AreaSearchParams extends SearchParams {}

  interface PaySearchParams extends SearchParams {
    orderId?: '';
    financeType?: MyEnumType.EnumFinanceTypeKey[];
    orderType?: '';
    payItem?: string;
    payMethod?: string[];
    driverName?: string;
    sellerName?: string;
    startBeginTime?: Date | number | string;
    startEndTime?: Date | number | string;
  }
}
