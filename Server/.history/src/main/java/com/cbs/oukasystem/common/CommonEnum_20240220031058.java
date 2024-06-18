package com.cbs.oukasystem.common;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class CommonEnum {

    public enum EnumUserRole {

        Super("Super", "Super"),
        Admin("Admin", "Admin"),
        Operator("Operator", "OP"),
        Seller("Seller", "営業"),
        Driver("Driver", "ドライバー"),
        Accounting("Accounting", "経理"),
        Customer("Customer", "顧客");

        private String code;
        private String message;

        private EnumUserRole(String code, String message) {
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
        public static EnumUserRole forName(String name) {
            for (EnumUserRole c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * ユーザー ステータス
     */
    public enum EnumUserStatus {
        Working("Working", "仕事中"),
        Resting("Resting", "休憩"),
        Retirement("Retirement", "退職"),
        Ban("Ban", "禁止");

        private String code;
        private String message;

        private EnumUserStatus(String code, String message) {
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
        public static EnumUserStatus forName(String name) {
            for (EnumUserStatus c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }

    /*
     * ステータス
     */
    public enum EnumStatus {

        Filling("Filling", "記入中"),
        Working("Working", "対応中"),
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

    public enum EnumSex {
        Male("Male", "男性"),
        Female("Female", "女性");

        private String code;
        private String message;

        private EnumSex(String code, String message) {
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
        public static EnumSex forName(String name) {
            for (EnumSex c : values()) {
                if (c.name().equals(name)) { // change accordingly
                    return c;
                }
            }
            return null;
        }
    }
}
