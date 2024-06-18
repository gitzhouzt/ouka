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
  'Customer' = '顧客'
}

/**
 * 性別
 */
export enum EnumSex {
  'Male' = '男性',
  'Female' = '女性'
}

/**
 * 时间类型
 */
export enum EnumDateType {
  'Day' = '日',
  'Month' = '月',
  'Year' = '年'
}

/*
 * ユーザー ステータス
 */
export enum EnumUserStatus {
  '' = '選択してください',
  'Working' = '仕事中',
  'Resting' = '休憩',
  'Retirement' = '退職',
  'Ban' = '禁止'
}

export enum EnumStatus {
  '' = '選択してください',
  'Filling' = '記入中',
  'Waiting' = '対応中',
  'Completed' = '対応完了'
}

export enum EnumTargetType {
  'Car' = '車両',
  'Driver' = 'ドライバー'
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
  'Cancelled' = 'キャンセル'
}

/**
 * 精算 ステータス
 */
export enum EnumFinanceStatus {
  '' = '選択してください',
  'Waiting' = '精算待ち',
  'Paid' = '精算済',
  'Completed' = '決算済'
}

/**
 * 精算 タイプ
 */
export enum EnumFinanceType {
  '' = '選択してください',
  'InEarnings' = '入金(売上)',
  'InAdvance' = '入金(立替)',
  'InTemp' = '前受金',
  'OutAdvance' = '出金(立替)',
  'OutCost' = '出金(コスト)'
}

/**
 * 注文 タイプ
 */
export enum EnumOrderType {
  '' = '選択してください',
  'Single' = 'シングル',
  'Haiya' = 'ハイヤー',
  'TimeLimitHaiya' = '時間制限ハイヤー',
  'Airport_Y' = '空港迎',
  'Airport_S' = '空港送'
}

/*
 * 車両 ステータス
 */
export enum EnumCarStatus {
  '' = '選択してください',
  'Standing' = '車庫',
  'Working' = '作業中',
  'Check' = '点検中',
  'Repair' = '修理中',
  'Scrapped' = '廃棄',
  'Sell' = '売る'
}

/** ********* STEP ********* */

/** LoginModule */
export enum EnumLoginModule {
  'pwd-login' = 'ログイン',
  'reset-pwd' = 'パスワード変更'
}

/**
 *  step
 */
export enum EnumActionStepModule {
  'action-input' = '情報入力',
  'action-confirmed' = '情報確認',
  'action-success' = '保存済'
}

export enum EnumDeleteStepModule {
  'del-confirmed' = '削除確認',
  'del-success' = '削除済'
}

export enum EnumOrderStepModule {
  'action-customer' = 'お客様情報入力',
  'action-order' = '注文情報入力',
  'action-confirmed' = '情報確認',
  'action-success' = '保存済'
}

export enum EnumDeployStepModule {
  'action-driver' = 'ドライバー支配',
  'action-car' = '車両支配',
  'action-remark' = '注文備考',
  'action-confirmed' = '情報確認',
  'action-success' = '保存済'
}

export enum EnumAccidentStepModule {
  'action-accident' = '事故情報入力',
  'action-finance' = '精算情報入力',
  'action-confirmed' = '情報確認',
  'action-success' = '保存済'
}
