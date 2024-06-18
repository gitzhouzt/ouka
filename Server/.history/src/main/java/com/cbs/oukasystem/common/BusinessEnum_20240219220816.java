package com.cbs.oukasystem.common;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class BusinessEnum {

    /*
     * 注文ステータス
     */
    public enum EnumOrderStatus {

        Filling("Filling", "記入中"),
        Assigning("Assigning", "支配待ち"),
        Sending("Sending", "発送待ち"),
        Sending("Sending", "確認待ち"),
        Booked("Booked", "予約済み"),
        Working("Working", "作業中"),
        Completed("Completed", "注文完了"),
        Cancelled("Cancelled", "キャンセル");

        private String code;
        private String message;

        private EnumOrderStatus(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumOrderStatus forName(String name) {
            for (EnumOrderStatus c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 決済ステータス
     */
    public enum EnumFinanceStatus {
        Waiting("Waiting", "支払待ち"),
        Paid("Paid", "支払済");

        private String code;
        private String message;

        private EnumFinanceStatus(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumFinanceStatus forName(String name) {
            for (EnumFinanceStatus c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 注文 Type
     * 空港送、迎、ハイヤー、半日ハイヤー、シングル、コース
     */
    public enum EnumOrderType {

        Single("Single", "シングル"),
        Haiya("Haiya", "ハイヤー"),
        TimeLimitHaiya("TimeLimitHaiya", "時間制限ハイヤー"),
        Airport_Y("Airport_Y", "空港迎"),
        Airport_S("Airport_S", "空港送");

        private String code;
        private String message;

        private EnumOrderType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumOrderType forName(String name) {
            for (EnumOrderType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 認証 Type
     */
    public enum EnumVerifyType {

        Register("Register", "ユーザー登録"),
        Pwd("Pwd", "パスワード変更");

        private String code;
        private String message;

        private EnumVerifyType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumVerifyType forName(String name) {
            for (EnumVerifyType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 認証 method
     */
    public enum EnumVerifyMethod {

        Email("Email", "メール"),
        Phone("Phone", "携帯");

        private String code;
        private String message;

        private EnumVerifyMethod(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumVerifyMethod forName(String name) {
            for (EnumVerifyMethod c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * コース Type
     */
    public enum EnumLanguageType {

        ZH_CN("zh", "中国語"),
        JA_JP("ja", "日本語"),
        EN_US("en", "英語");

        private String code;
        private String message;

        private EnumLanguageType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumLanguageType forName(String name) {
            for (EnumLanguageType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 車両 ステータス
     */
    public enum EnumCarStatus {

        Standing("Standing", "車庫"),
        Working("Working", "作業中"),
        Check("Check", "点検中"),
        Repair("Repair", "修理中"),
        Scrapped("Scrapped", "廃棄"),
        Sell("Sell", "売る");

        private String code;
        private String message;

        private EnumCarStatus(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumCarStatus forName(String name) {
            for (EnumCarStatus c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 点検 Type
     */
    public enum EnumCarCheckType {

        Check_3_Month("Check_3_Month", "点検"),
        Check_year("Check_year", "年検");

        private String code;
        private String message;

        private EnumCarCheckType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumCarCheckType forName(String name) {
            for (EnumCarCheckType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * Data Type
     */
    public enum EnumDataType {

        Customer("Customer", "顧客"),
        Company("Company", "会社"),
        Admin("Admin", "Admin");

        private String code;
        private String message;

        private EnumDataType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumDataType forName(String name) {
            for (EnumDataType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    public enum EnumTemplateType {

        Kanban("Kanban", "看板"),
        Invoice("Invoice", "請求書");

        private String code;
        private String message;

        private EnumTemplateType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumTemplateType forName(String name) {
            for (EnumTemplateType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 事故ステータス
     */
    public enum EnumStatus {

        Filling("Filling", "記入中"),
        Waiting("Waiting", "対応中"),
        Completed("Completed", "対応完了");

        private String code;
        private String message;

        private EnumStatus(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumStatus forName(String name) {
            for (EnumStatus c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 目标 Type
     */
    public enum EnumTargetType {

        Car("Car", "車両"),
        Driver("Driver", "ドライバー");

        private String code;
        private String message;

        private EnumTargetType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumTargetType forName(String name) {
            for (EnumTargetType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * スケジュール タイプ
     */
    public enum EnumActionType {

        Order("Order", "注文"),
        Rest("Rest", "休憩"),
        Repair("Repair", "修理"),
        Check("Check", "点検");

        private String code;
        private String message;

        private EnumActionType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumActionType forName(String name) {
            for (EnumActionType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * 精算 タイプ
     */
    public enum EnumFinanceType {

        Deposit("Deposit", "入金"),
        Expenditure("Expenditure", "支出"),
        Cost("Cost", "コスト");

        private String code;
        private String message;

        private EnumFinanceType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @JsonCreator
        public static EnumFinanceType forName(String name) {
            for (EnumFinanceType c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

}
