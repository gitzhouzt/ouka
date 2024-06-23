declare namespace MyModel {
	interface Root {
		id: string;
		createTime?: Date | string;
		createBy?: string;
		updateTime?: Date | string;
		updateBy?: string;
		deleteTime?: Date | string;
		deleteBy?: string;
		isDeleted?: boolean; // false
		auditTime?: Date | string;
		auditBy?: string;
		isAudit?: boolean | string; // false
	}

	interface MyResultArray<T = MyModel.Root> {
		list: T[];
		page: number;
		pageSize: number;
		itemCount: number;
		pageCount: number;
	}

	interface User extends Root {
		// 利用者
		userName?: string;
		user_name?: string;
		userEmail?: string;
		user_email?: string;
		userSex?: string;
		userRoles?: string;
		selUserRoles?: string[];

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

		// パスワード
		userPass?: string;
		userNewPass?: string;
	}

	interface UserPwd {
		pwd: string;
		newPwd: string;
		confirmPwd: string;
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
		orderTypeName?: string;
		orderDays?: number;
		startTime?: Date | number | string;
		endTime?: Date | number | string;
		adultNum?: number;
		childrenNum?: number;
		luggageNum?: number;
		orderStatus?: MyEnumType.EnumOrderStatusKey;
		orderStatusName?: string;
		orderPrice?: number;
		orderFrom?: string;
		orderTo?: string;
		orderFromDetails?: string;
		orderToDetails?: string;
		flightNo?: string;
		airport?: string;

		feeType?: string;
		isCash?: boolean | string;
		isOutTimeCash?: boolean | string;
		outTimeAmount?: number;
		isETC?: boolean | string;

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

	interface OrderDetails extends Root {
		orderVO: Order;
		orderGoodsVOs: OrderGoods[];
		payRecordVOs: PayRecord[];
		orderFileVOs: OrderFile[];
	}

	interface OrderGoods extends Root {
		orderNo?: string;
		goodsType?: string;
		goodsTypeCode?: string;
		amount?: number;
		remark?: string;
	}

	interface OrderFile extends Root {
		orderNo?: string;
		fileName?: string;
		fileUrl?: string;
		share?: number;
		remark?: string;
	}

	interface UserDriver extends Root {
		userId: string;
		userNo: string;
		userVO?: MyModel.User;
		driverLicense?: string;
		licenseType?: string;
		driverType?: string;
		expiryDate?: Date | number | string;
		healthyDate?: Date | number | string;
	}

	interface PayRecord extends Root {
		orderId?: string;
		orderNo?: string;
		payNo?: string;
		payMethod?: string;
		payMethodCode?: string;
		payItem?: string;
		payItemCode?: string;
		financeType?: MyEnumType.EnumFinanceTypeKey;
		financeTypeName?: string;
		amount?: number;
		currencyAmount?: number;
		currency?: string;
		currencyCode?: string;
		images?: string;
		remark?: string;
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

	interface LogDetails extends Root {
		userLogVO: Log;
		logRecordVOs: LogRecord[];
	}

	interface Today extends Root {
		userName: string;
		userNo: string;
		today: string;
		orderCnt: number;
		todayOrderVOs: TodayOrder[];
		callVOs?: any;
		amCall?: number;
		pmCall?: number;
		log?: number;
	}

	interface TodayOrder extends Root {
		orderStatus: number;
		orderNo: string;
		carNo: string;
		carName: string;
		startTime: Date | number | string;
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
		amImage?: string;

		pmCallMethod?: string;
		pmCallTime?: Date | number | string;
		pm1?: string;
		pm2?: string;
		pm3?: string;
		pm4?: string;
		pmConfirmBy?: string;
		pmRemark?: string;
		pmImage?: string;

		weather?: string;
		manager?: string;
		helper?: string;
	}

	interface Car extends Root {
		carNo?: string;
		carName?: string;
		carPhoto?: string;
		carType?: string;
		carSeat?: number;
		plateNum?: string;
		carPark?: string;
		parkingFee?: number;
		status?: string;
		remark?: string;
	}

	interface CarDamage extends Root {
		carId?: string;
		carVO?: string;
		image1?: string;
		image2?: string;
		image3?: string;
		image4?: string;
		remark?: string;
	}

	interface CarDetails extends Root {
		carVO: Car;
		carDamageVOs: CarDamage[];
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
