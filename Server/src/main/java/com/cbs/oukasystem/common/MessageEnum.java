package com.cbs.oukasystem.common;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cbs.oukasystem.config.BaseErrorInfoInterface;

@Service
@Transactional
public final class MessageEnum {

    public enum EnumResponseCheck implements BaseErrorInfoInterface {
        SUCCESS(200, "ok", "response.success"),
        REQUEST_SYNTAX_ERROR(400, "リクエストに構文エラー", "response.syntax.error"),
        USER_NOT_ALLOWED(401, "ユーザーが許可されていません", "response.not.allowed"),
        SERVER_DENIED_ACCESS(403, "サーバーがアクセスを拒否しました", "request.server.denied.access"),
        NOT_FOUND(404, "見つからなかった", "request.not.found"),
        REQUEST_TIMEOUT(408, "リクエストがタイムアウト", "request.timeout"),
        SYSTEM_ERROR(500, "システムエラーです。しばらくお待ちください。", "request.system.error");

        private Integer code;
        private String message;
        private String localeKey;

        private EnumResponseCheck(Integer code, String message, String localeKey) {
            this.code = code;
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return code;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumLoginCheck implements BaseErrorInfoInterface {

        SUCCESS("ログインしました", "login.success"),
        ERROR("ログインエラー", "login.error"),
        NOT_LOGIN("ログインしてください", "login.not.login"),
        TOKEN_ERROR("トークンエラー", "login.token.error"),
        USER_DISABLED("ユーザーが無効になっています", "login.user.disabled"),
        TOKEN_EXPIRED("トークンの有効期限が切れました", "login.token.expired");

        private String message;
        private String localeKey;

        private EnumLoginCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return 999;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumVerifyCheck implements BaseErrorInfoInterface {
        INPUT_NULL("確認コードを入力してください", "verify.input.null"),
        INPUT_FORMAT("6文字の確認コードを入力してください", "verify.input.format"),
        INPUT_ERROR("確認コード間違いです", "verify.input.error"),
        SEND_SUCCESS("確認コードが送信されました", "verify.send.success"),
        SEND_ERROR("確認コードの送信が失敗しました", "verify.send.error"),
        EXPIRED("確認コードの有効期限が切れています。再取得してください", "verify.expired"),
        EXPIRED_NOT("確認コードは 10 分間有効です。頻繁に送信しないでください", "verify.expired.not");

        private String message;
        private String localeKey;

        private EnumVerifyCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumPwdCheck implements BaseErrorInfoInterface {
        INPUT_NULL("パスワードを入力してください", "pwd.input.null"),
        INPUT_OLD_NULL("現在のパスワードを入力してください。", "pwd.input.old.null"),
        INPUT_NEW_NULL("新しいパスワードを入力してください。", "pwd.input.new.null"),
        INPUT_FORMAT("新しいパスワードは8から16文字の半角英数字記号を組み合わせた内容をご入力ください", "pwd.input.format"),
        INPUT_CONFIRM_NULL("確認パスワードを入力してください", "pwd.input.confirm.null"),
        INPUT_NOT_SAME("新しいパスワードが確認用に入力されたものと異なります。入力しなおしてください。", "pwd.input.not.same"),
        INPUT_ERROR("正しいパスワードを入力してください", "pwd.input.error");

        private String message;
        private String localeKey;

        private EnumPwdCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumMailCheck implements BaseErrorInfoInterface {
        INPUT_NULL("メールアドレスを入力してください", "email.input.null"),
        INPUT_FORMAT("有効なメールアドレスを入力してください", "email.input.format"),
        SEND_SUCCESS("メールが送信を成功しました", "email.send.success"),
        SEND_ERROR("メールが送信を失敗しました", "email.send.error");

        private String message;
        private String localeKey;

        private EnumMailCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumPhoneCheck implements BaseErrorInfoInterface {
        INPUT_NULL("電話番号を入力してください", "phone.input.null"),
        INPUT_FORMAT("有効な電話番号を入力してください", "phone.input.format"),
        COUNTRY_ERROR("国番失敗しました", "phone.country.error");

        private String message;
        private String localeKey;

        private EnumPhoneCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumRegisterCheck implements BaseErrorInfoInterface {
        ERROR("登録失敗しました", "register.error"),
        SUCCESS("登録しました", "register.success"),
        CANT_ADMIN("ユーザー名を admin/ にすることはできません", "register.cant.admin");

        private String message;
        private String localeKey;

        private EnumRegisterCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumInsertCheck implements BaseErrorInfoInterface {
        ERROR("追加失敗しました", "insert.error"),
        SUCCESS("追加しました", "insert.success");

        private String message;
        private String localeKey;

        private EnumInsertCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumUpdateCheck implements BaseErrorInfoInterface {
        ERROR("更新失敗しました", "update.error"),
        SUCCESS("更新しました", "update.error");

        private String message;
        private String localeKey;

        private EnumUpdateCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumIOUCheck implements BaseErrorInfoInterface {
        ERROR("追加/更新失敗しました", "iou.error"),
        SUCCESS("追加/更新しました", "iou.success");

        private String message;
        private String localeKey;

        private EnumIOUCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumDeleteCheck implements BaseErrorInfoInterface {
        ERROR("削除失敗しました", "delete.error"),
        SUCCESS("削除しました", "delete.success");

        private String message;
        private String localeKey;

        private EnumDeleteCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumUploadCheck implements BaseErrorInfoInterface {
        SUCCESS("アップロードしました", "upload.success"),
        ERROR("アップロード失敗しました", "upload.error"),
        IMAGE_FORMAT("フォーマットが異なります", "upload.image.format");

        private String message;
        private String localeKey;

        private EnumUploadCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumDataCheck implements BaseErrorInfoInterface {
        ERROR("データ受信に失敗しました", "data.error"),
        EMPTY("データなし", "data.empty"),
        VERIFY_FAILED("データ検証に失敗しました", "data.verify.error"),
        NOT_EXIST("存在しません", "data.not.exist"),
        AREA_NOT_EXIST("地域が存在しません", "data.area.not.exist"),
        USER_NOT_EXIST("未登録ユーザーです", "data.user.not.exist"),
        STAFF_NOT_EXIST("スタッフが存在しません", "data.staff.not.exist"),
        EXISTED("は既に存在しています", "data.existed"),
        USER_EXISTED("メールまたは名前既に登録済です", "data.user.existed"),
        MENU_EXISTED("メニューは既に登録済です", "data.menu.existed"),
        STAFF_EXISTED("スタッフは既に登録済です", "data.staff.existed"),
        COMPANY_EXISTED("会社名は既に登録済です", "data.company.existed"),
        QRCODE_GENERATE_ERROR("QRコードの生成が失敗しました", "data.qrcode.error");

        private String message;
        private String localeKey;

        private EnumDataCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

    public enum EnumOrderCheck implements BaseErrorInfoInterface {
        CANCEL_CANT("注文が進行中や完了しており、キャンセルできません", "order.cancel.cant"),
        DEL_CANT("注文が完了していないので、削除できません", "order.delete.cant"),
        NOT_EXIST("注文が存在しません", "order.not.exist"),
        COMMENT_CANT("注文が完了していないので、コメントできません", "order.comment.cant"),
        COURSE_LIMIT("このコースの注文数が今月の予約上限に達しました", "order.course.limit");

        private String message;
        private String localeKey;

        private EnumOrderCheck(String message, String localeKey) {
            this.message = message;
            this.localeKey = localeKey;
        }

        @Override
        public Integer getErrorCode() {
            return -1;
        }

        @Override
        public String getErrorMsg() {
            return message;
        }

        @Override
        public String getLocaleKey() {
            return localeKey;
        }
    }

}
