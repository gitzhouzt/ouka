declare namespace MyModel {
  interface Root {
    id: string;
    createTime?: Date;
    createBy?: string;
    createByName?: string;
    updateTime?: Date;
    updateBy?: string;
    deleteTime?: Date;
    deleteBy?: string;
    deleteByName?: string;
    isDeleted?: boolean; // false
    auditTime?: Date;
    auditBy?: string;
    auditByName?: string;
    isAudit?: boolean | string; // false
  }

  interface MyResultArray<T = MyModel.Root> {
    list: T[];
    page: number;
    pageSize?: number;
    itemCount?: number;
    pageCount?: number;
  }

  interface User extends Root {
    // 利用者
    userName: string;
    user_name?: string;
    userEmail: string;
    user_email?: string;
    userSex: string;
    userRoles: string;
    selUserRoles: string[];

    userId?: string;
    userNo?: string;
    user_no?: string;
    status?: MyEnumType.EnumUserStatusKey;

    countryNum?: number;
    country_num?: number;

    userPhone?: string;
    user_phone?: string;
    userAvatar?: string;
    userAddress?: string;
    userPost?: string;
    remark?: string;

    userLine?: string;
    userWechat?: string;
    userWhatsApp?: string;

    // パスワード
    userPass?: string;
    userNewPass?: string;
  }

  interface Driver extends Root {
    userId: string;
    userVO?: MyModel.User;
    userName?: string;
    userNo?: string;
    driverLicense: string;
    licenseType: string;
    licenseTypeCode: string;
    driverType: string;
    driverTypeCode: string;
    expiryDate: Date | number | string;
    healthyDate?: Date | number | string;
  }

  interface Train extends Root {
    userId: string;
    userVO?: MyModel.User;
    trainYear: Date | number | string;
    trainDate1?: Date | number | string;
    trainDate2?: Date | number | string;
    trainDate3?: Date | number | string;
    trainDate4?: Date | number | string;
  }

  interface UserRest extends Root {
    userId: string;
    userNo: string;
    userName: string;
    restType: string;
    restTypeCode: string;
    userRoles: string;
    remark: string;
    startTime: Date | number | string;
    endTime: Date | number | string;
  }

  interface Area extends Root {
    areaId?: string;
    areaCode: string;
    areaName: string;
    cityCode?: string;
    cityName?: string;
  }

  interface Order extends Root {
    /*
     * 注文情報
     */
    orderNo?: string;
    orderSource?: string;
    orderSourceCode?: string;
    orderKey?: string;
    city?: string;
    orderType?: MyEnumType.EnumOrderTypeKey;
    airportType?: MyEnumType.EnumAirportTypeKey;
    orderDays?: number;
    startTime?: Date | number | string;
    endTime?: Date | number | string;
    adultNum?: number;
    childrenNum?: number;
    luggageNum?: number;
    orderStatus?: MyEnumType.EnumOrderStatusKey;
    orderPrice?: number;
    orderFrom?: string;
    orderTo?: string;
    orderFromDetails?: string;
    orderToDetails?: string;
    flightNo?: string;
    airport?: MyEnumType.EnumAirportTypeKey;

    feeType?: string;
    isCash?: boolean | string;
    isOutTimeCash?: boolean | string;
    outTimeAmount?: number;
    isLodgingTips?: boolean | string;

    /*
     * お客さん情報
     */
    customerName?: string;
    billingAddress?: string;
    customerRemark?: string;
    companyRemark?: string;
    contactMethod1?: string;
    contactMethod2?: string;
    contactMethod3?: string;
    contactContent1?: string;
    contactContent2?: string;
    contactContent3?: string;

    /*
     * 車両情報
     */
    carId?: string;
    carNo?: string;
    carName?: string;
    carType?: string;
    specifyCarType?: string;
    carSeat?: number;

    /*
     * 運転者情報
     */
    driverId?: string;
    driverNo?: string;
    driverName?: string;
    driverPhoto?: string;

    /*
     * 営業情報 责任人
     */
    sellerId?: string;
    sellerNo?: string;
    sellerName?: string;
    sellerPhoto?: string;
  }

  interface PayRecord extends Root {
    orderId?: string;
    orderVO?: Order;
    payMethod?: string;
    payMethodCode?: string;
    payItem?: string;
    payItemCode?: string;
    financeType?: MyEnumType.EnumFinanceTypeKey;
    bank?: string;
    amount?: number;
    currencyAmount?: number;
    currency?: string;
    currencyCode?: string;
    sellerName?: string;
    status?: MyEnumType.EnumFinanceStatusKey;
    remark?: string;
  }

  interface Advance extends Root {
    orderId?: string;
    orderVO?: Order;
    profitAmount: number;
    advanceAmount: number;
    inAmount: number;
    mealAmount: number;
    hotelAmount: number;
    admissionAmount: number;
    parkingAmount: number;
    etcAmount: number;
    roadAmount: number;
    ticketAmount: number;
    waterAmount: number;
    overtimeAmount: number;
    otherAmount: number;

    status?: MyEnumType.EnumFinanceStatusKey;
    remark?: string;
  }

  interface Earnings extends Root {
    orderId?: string;
    orderVO?: Order;
    orderNo?: string;
    handlingFee: number;
    oilFee: number;
    etc: number;
    entrustSalary: number;
    salary: number;
    entrust: number;
    parking: number;
    profit: number;
    profitRate: number;
    entrustReason: string;
  }

  interface PayItem extends Root {
    orderId?: string;
    orderNo?: string;
    payItem?: string;
    payItemCode?: string;
    amount: number;
    remark?: string;
  }

  interface DeployRecord extends Root {
    orderId?: string;
    orderNo?: string;
    newId?: string;
    newNo?: string;
    newName?: string;
    targetType?: MyEnumType.EnumTargetTypeKey;
    remark?: string;
  }

  interface DeployDetails extends Root {
    orderId?: string;
    orderNo?: string;

    driverId?: string;
    driverNo?: string;
    driverName?: string;
    driverPhoto?: string;
    driverRemark?: string;

    carId?: string;
    carNo?: string;
    carName?: string;
    carType?: string;
    carSeat?: number;
    specifyCarType?: string;
    carRemark?: string;

    companyRemark?: string;

    deployRecordVOs?: DeployRecord[];
  }

  interface OrderDetails extends Root {
    contactId: string;
    contactVO?: Contact;
    driverId: string;
    driverVO?: Staff;
    orderVO?: Order;
  }

  interface OrderFile extends Root {
    orderId: string;
    orderNo: string;
    fileName: string;
    fileUrl: string;
    share: Boolean;
    remark: string;
  }

  interface OrderContact extends Root {
    orderNo: string;
    contactMethod: string;
    contactMethodCode: string;
    content: string;
  }

  interface OrderGoods extends Root {
    orderId: string;
    orderNo: string;
    goodsType: string;
    goodsTypeCode: string;
    amount: number;
    remark?: string;
  }

  interface Car extends Root {
    carNo?: string;
    carName: string;
    carPhoto?: string;
    carType?: string;
    carSeat?: number;
    plateNum?: string;
    carPark?: string;
    parkingFee?: number;
    status?: string;
    remark?: string;
  }

  interface CarCheck extends Root {
    carId?: string;
    carNo?: string;
    year?: string;
    yearCheckDate?: Date | string | number;
    yearCheckStatus?: Boolean;
    monthCheckDate1?: Date | string | number;
    checkStatus1?: Boolean;
    monthCheckDate2?: Date | string | number;
    checkStatus2?: Boolean;
    monthCheckDate3?: Date | string | number;
    checkStatus3?: Boolean;
    lastYearCheckDate?: Date | string | number;
    lastMonthCheckDate?: Date | string | number;
  }

  interface CarRepair extends Root {
    carId?: string;
    carNo?: string;
    carName: string;
    status?: string;
    remark?: string;
    repairTime?: Date | string | number;
    repairType?: string;
  }

  interface Schedule extends Root {
    targetId: string;
    targetNo: string;
    targetName: string;
    targetType: MyEnumType.EnumTargetTypeKey;
    actionId: string;
    actionType: MyEnumType.EnumActionTypeKey;
    workDate: string;
    workTime?: Date | string | number;
    remark?: string;
  }

  interface Accident extends Root {
    carId?: string;
    carNo?: string;
    carName?: string;
    driverId?: string;
    driverNo?: string;
    driverName?: string;

    accidentType?: string;
    accidentTypeCode?: string;
    details?: string;
    confirmBy?: string;
    accidentTime?: Date | number | string;
    noticeTime?: Date | number | string;
    images?: string;

    repairTime?: Date | number | string;
    repairBy?: string;

    proportion?: string;
    responsible?: string;

    insuranceAmount?: number;
    driverAmount?: number;
    amount?: number;
    companyAmount?: number;

    financeAmount?: number;
    financeNoticeTime?: Date | number | string;
    financeTime?: Date | number | string;
    financeBy?: string;
    financeByName?: string;

    status?: MyEnumType.EnumStatusKey;
    remark?: string;
  }

  interface Call extends Root {
    date?: string;
    carId?: string;
    carNo?: string;
    carName?: string;
    driverId?: string;
    driverNo?: string;
    driverName?: string;

    amCallMethod?: string;
    amCallTime?: Date | number | string;
    am1?: string;
    am2?: string;
    am3?: string;
    am4?: string;
    amConfirmBy?: string;
    amRemark?: string;

    pmCallMethod?: string;
    pmCallTime?: Date | number | string;
    pm1?: string;
    pm2?: string;
    pm3?: string;
    pm4?: string;
    pmConfirmBy?: string;
    pmRemark?: string;

    weather?: string;
    manager?: string;
    helper?: string;
  }

  interface Log extends Root {
    /*
     * ドライバー
     */
    driver1Id?: string;
    driver1No?: string;
    driver1Name?: string;
    driver2Id?: string;
    driver2No?: string;
    driver2Name?: string;

    /*
     * 車両
     */
    carId?: string;
    carNo?: string;
    carName?: string;

    /*
     *
     */
    date?: string;
    startDistance?: number;
    endDistance?: number;
    diffDistance?: number;
    jisshaDistance?: number;
    kaisouDistance?: number;
    refueling?: string;
    startAddress?: string;
    endAddress?: string;
    remark?: string;

    startBy?: string;
    endBy?: string;
    personNum?: number;
    weather?: string;
    manager?: string;

    /**
     * 運転席点検
     */
    a1?: boolean;
    a2?: boolean;
    a3?: boolean;
    a4?: boolean;
    a5?: boolean;
    a6?: boolean;
    a7?: boolean;

    /**
     * ルーム点検・エンジン
     */
    b1?: boolean;
    b2?: boolean;
    b3?: boolean;
    b4?: boolean;
    b5?: boolean;
    b6?: boolean;

    /**
     * 車の周りの点検
     */
    c1?: boolean;
    c2?: boolean;
    c3?: boolean;
    c4?: boolean;
    c5?: boolean;
    c6?: boolean;
    c7?: boolean;
    c8?: boolean;

    /**
     * その他
     */
    d1?: boolean;
    d2?: boolean;
    checkRemark?: string;
  }

  interface LogRecord extends Root {
    logId?: string;
    start?: string;
    startTime?: string;
    end?: string;
    endTime?: string;
    distance?: string;
    jissha?: string;
    kaisou?: string;
    rest?: string;
    restNum?: string;
    remark?: string;
  }

  interface Dict extends Root {
    dictName: string;
    dictCode: string;
    remark?: string;
  }

  interface DictItem extends Root {
    dictId: string;
    dictCode: string;
    itemName: string;
    itemCode: string;
    remark?: string;
  }
}
