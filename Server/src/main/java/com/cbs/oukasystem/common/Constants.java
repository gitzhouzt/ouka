package com.cbs.oukasystem.common;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * リテラル共通
 */
@Service
@Transactional
public final class Constants {

    // 文字列_-1
    public final static String STR_MINUS_1 = "-1";
    // 文字列_0
    public final static String STR_0 = "0";
    // 文字列_1
    public final static String STR_1 = "1";
    // 文字列_2
    public final static String STR_2 = "2";
    // 文字列_3
    public final static String STR_3 = "3";
    // 文字列_4
    public final static String STR_4 = "4";
    // 文字列_5
    public final static String STR_5 = "5";
    // 文字列_6
    public final static String STR_6 = "6";
    // 文字列_7
    public final static String STR_7 = "7";
    // 文字列_8
    public final static String STR_8 = "8";

    // 数字_-1
    public final static int NUM_MINUS_1 = -1;
    // 数字_0
    public final static int NUM_0 = 0;
    // 数字_1
    public final static int NUM_1 = 1;
    // 数字_2
    public final static int NUM_2 = 2;
    // 数字_3
    public final static int NUM_3 = 3;
    // 数字_4
    public final static int NUM_4 = 4;
    // 数字_5
    public final static int NUM_5 = 5;
    // 数字_6
    public final static int NUM_6 = 6;
    // 数字_7
    public final static int NUM_7 = 7;
    // 数字_8
    public final static int NUM_8 = 8;
    // 数字_9
    public final static int NUM_9 = 9;
    // 数字_10
    public final static int NUM_10 = 10;

    // 数字_7日前
    public final static Long BEFORE_DAY_MINUS_7L = 7L;
    // 数字_30分
    public final static Long Minutes_30L = 30L;
    // 数字_十分間
    public final static Long NUM_600 = 600L;
    // 数字_三十日間
    public final static Long NUM_30 = 30 * 24 * 60 * 60L;
    // Token期間
    public final static int NUM_Day_30 = 30;
    // Token期間
    public final static Long Day_30 = 30L;

    // メールフォーマット
    public final static String EMAIL_REGEX = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
    // 電話番号フォーマット
    public final static String TEL_REGEX = "^0[789]0[0-9]{8}$";
    // パスワードフォーマット
    public final static String PASS_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,20}$";
    // パスワードフォーマット
    public final static String COUNTRY_NUM_REGEX = "^[+][0-9]{0,5}";

    // ファイル種類_写真
    public final static String FILE_SORT_AVATAR = "avatar";
    // ファイル種類_評価
    public final static String FILE_SORT_EVALUATE = "evaluate";
    // ファイル種類_営業許可書
    public final static String FILE_SORT_LICENSE = "license";

    // 表示ページ数_10
    public final static int PAGE_SIZE_10 = 10;

    // メニュー タイプ
    public final static String MENU_TYPE_MAIN = "1";
    public final static String MENU_TYPE_CHILD = "2";

    // UTF-8
    public static final String CHARSET = "UTF-8";

    // DEFAULT_PASSWORD
    public static final String DEFAULT_PASSWORD = "abcd@6789";

    public static final String DEFAULT_COMPANY_PREFIX = "C"; // DEFAULT_会社番号
    public static final String DEFAULT_STAFF_PREFIX = "R"; // DEFAULT_利用者番号
    public static final String DEFAULT_ORDER_PREFIX = "O"; // DEFAULT_注文番号
    public static final String DEFAULT_CAR_PREFIX = "CS"; // DEFAULT_車両
    public static final String DEFAULT_NUMBER = "001";

    public static final int QRCODE_DEFAULT_WIDTH = 500;
    public static final int QRCODE_DEFAULT_HEIGHT = 500;

}
