/**
 * ユーザー  役職
 */
export enum EnumUserRole {
	'' = '選択してください',
	'Super' = 'Super',
	'Admin' = 'Admin',
	'Operator' = 'OP',
	'OperationManager' = '運管',
	'Seller' = '営業',
	'Driver' = 'ドライバー',
	'Accounting' = '経理',
	'Customer' = '顧客',
}

/**
 * 性別
 */
export enum EnumSex {
	'Male' = '男性',
	'Female' = '女性',
}

/*
 * ユーザー ステータス
 */
export enum EnumUserStatus {
	'Working' = '仕事中',
	'Resting' = '休憩',
	'Retirement' = '退職',
	'Ban' = '禁止',
}

export enum EnumStatus {
	'Waiting' = '対応待ち',
	'Working' = '対応中',
	'Completed' = '対応完了',
}

/**
 * 注文 ステータス
 */
export enum EnumOrderStatus {
	'' = '選択してください',
	'Filling' = '記入中',
	'Assigning' = '支配待ち',
	'Sending' = '発送待ち',
	'Check' = 'チェック待ち',
	'Booked' = '予約済み',
	'Working' = 'サービス',
	'Completed' = '注文完了',
	'Cancelled' = 'キャンセル',
}

/**
 * 精算ステータス
 */
export enum EnumFinanceStatus {
	'Waiting' = '精算待ち',
	'Completed' = '精算済',
}

/**
 * 精算 タイプ
 */
export enum EnumFinanceType {
	'Deposit' = '入金',
	'Expenditure' = '支出',
	'Cost' = 'コスト',
}

/**
 * 注文 タイプ
 */
export enum EnumOrderType {
	'Single' = 'シングル',
	'Haiya' = 'ハイヤー',
	'TimeLimitHaiya' = '時間制限ハイヤー',
	'Airport_Y' = '空港迎',
	'Airport_S' = '空港送',
}

/*
 * 車両 ステータス
 */
export enum EnumCarStatus {
	'Standing' = '車庫',
	'Working' = '作業中',
	'Check' = '点検中',
	'Repair' = '修理中',
	'Scrapped' = '廃棄',
	'Sell' = '売った',
}

/**
 * 事故 ステータス
 */
export enum EnumAccidentStatus {
	'Waiting' = '対応中',
	'Completed' = '対応完了',
}

/**
 * アクセス ソート
 */
export enum EnumAccessSort {
	'DEPART_ASC' = '所属部署 昇順',
	'DEPART_DES' = '所属部署 降順',
	'CREATE_TIME_ASC' = '払出日時 昇順',
	'CREATE_TIME_DES' = '払出日時 降順',
	'SUPPORT_TIME_ASC' = '対応日時 昇順',
	'SUPPORT_TIME_DES' = '対応日時 降順',
}

/** ********* STEP ********* */

/** LoginModule */
export enum EnumLoginModule {
	'user-login' = '緊急時の対応',
	'staff-login' = 'ドライバーアプリ',
}

/**
 * 基本的step
 */
export enum EnumBaseStepModule {
	'confirmed' = '確認',
	'success' = '成功',
}

/**
 * Add or Edit step
 */
export enum EnumAddOrEditStepModule {
	'action' = 'アクション',
	'confirmed' = '確認',
	'success' = '成功',
}
