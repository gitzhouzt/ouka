package com.cbs.oukasystem.common;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.BusinessEnum.EnumLanguageType;
import com.cbs.oukasystem.common.MessageEnum.EnumMailCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.common.Email;
import com.cbs.oukasystem.entity.order.OrderEntity;

/*
 * メール送信の共通サービス
 */
@Service
@Transactional
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "spring.mail")
public class EmailUtils {

    public static String from;

    @Value("${spring.mail.from}")
    public void setFrom(String from) {
        EmailUtils.from = from;
    }

    @Autowired
    private JavaMailSender mailSender;

    public Email sendMail(Email email) {
        try {
            sendMimeMail(email);
        } catch (Exception e) {
            email.setStatus("fail");
            email.setErrorMsg(e.getMessage());
        }
        return email;
    }

    /**
     * HTML形式で送信すること。
     */
    private void sendMimeMail(Email email) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setSubject(email.getSubject());
            messageHelper.setFrom(from);
            messageHelper.setTo(email.getTo());
            if (null != email.getCc() && email.getCc().length > 0) {
                messageHelper.setCc(email.getCc());
            }
            if (null != email.getBcc() && email.getBcc().length > 0) {
                messageHelper.setBcc(email.getBcc());
            }
            if (null != email.getMultipartFiles()) {
                for (MultipartFile multipartFile : email.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (null == email.getSendDate()) {
                email.setSendDate(new Date());
            }
            messageHelper.setText(email.getText(), true);
            // 送信
            messageHelper.setSentDate(email.getSendDate());
            mailSender.send(messageHelper.getMimeMessage());
            email.setStatus("ok");
        } catch (Exception e) {
            throw new BaseException(EnumUploadCheck.ERROR, e.getMessage());
        }
    }

    // 【XXXXX配車サービス】確認コードのお知らせ
    public void sendEmailVerifyCodeToUser(String to, String code) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        StringBuilder textBuilder = new StringBuilder();
        Email email = new Email();
        email.setTo(to);

        if (lang.equals(EnumLanguageType.JA_JP.getCode())) {
            email.setSubject("【XXXXX配車サービス】確認コードのお知らせ");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + "認証コードは次のとおり:  </div>");
            textBuilder.append("<div>" + code + "</div>");
            textBuilder.append("<br/>");
            textBuilder.append("<div>10分以内に認証を完了してください。</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else if (lang.equals(EnumLanguageType.EN_US.getCode())) {
            email.setSubject("【XXXXX配車サービス】Verification Code");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + "Your verification code information is as follows:  </div>");
            textBuilder.append("<div>" + code + "</div>");
            textBuilder.append("<br/>");
            textBuilder.append("<div>Please complete the verification within 10 minutes.</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else {
            email.setSubject("【XXXXX配車サービス】验证码信息");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + "您的验证码信息如下:  </div>");
            textBuilder.append("<div>" + code + "</div>");
            textBuilder.append("<br/>");
            textBuilder.append("<div>请在10分钟内完成验证。</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        }

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    // 【XXXXX配車サービス】より注文のお知らせ_注文No
    public void sendEmailOrderToCompany(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();
        // 会社が注文を確認
        email.setTo(to);
        email.setSubject("【XXXXX配車サービス】より注文のお知らせ_注文No（" + order.getOrderNo() + "）");
        textBuilder.append("<html><body>");
        textBuilder.append("<div>会社様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、配車注文のお知らせです。</div>");
        textBuilder.append("<div>管理画面より、ご確認の上処理してください。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>予約車両：" + order.getCarName() + "</div>");
            textBuilder.append("<div>車両タイプ：" + order.getCarType() + "</div>");
        }
        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>予約金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");
        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR, email.getErrorMsg());
        }
    }

    public void sendEmailOrderToUser(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();
        String lang = LocaleContextHolder.getLocale().getLanguage();

        // ユーザーが注文情報を確認
        email.setTo(to);
        if (lang == EnumLanguageType.JA_JP.getCode()) {
            email.setSubject("【XXXXX配車サービス】より注文のお知らせ_注文No（" + order.getOrderNo() + "）");

            textBuilder = new StringBuilder();
            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
            textBuilder.append("<div>ご利用ありがとうございます。</div>");
            textBuilder.append("<div>下記の通り、配車注文のお知らせです。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");
            textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
            }

            textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>予約金額：" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else if (lang == EnumLanguageType.EN_US.getCode()) {
            email.setSubject("【XXXXX配車サービス】Order Notification_Order No（" + order.getOrderNo() + "）");

            textBuilder = new StringBuilder();
            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>Thank you for using our service.</div>");
            textBuilder.append("<div>Below is the information about the delivery order.</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>Order No:" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>Departure Time:" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>Booking Driver:" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>Reservation Course:" + order.getCarName() + "</div>");
            }
            textBuilder.append("<div>Boarding Address:" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>Get off Address:" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>Customer Remark:" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>Booking Amount:" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else {
            email.setSubject("【XXXXX配車サービス】订单通知_订单No（" + order.getOrderNo() + "）");

            textBuilder = new StringBuilder();
            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>感谢您使用Yukoyo。</div>");
            textBuilder.append("<div>以下是您的订单详情。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>订单编号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>旅行日期：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>预约司机：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>预约套餐：" + order.getCarName() + "</div>");
            }
            textBuilder.append("<div>上车地址：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下车地址：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>乘客备注：" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>订单金额：" + order.getOrderPrice() + "日元</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");
        }

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    // 【XXXXX配車サービス】より受付完了のお知らせ_注文No
    public void sendEmailConfirmToUser(String to, OrderEntity order) {
        Email email = new Email();
        String lang = LocaleContextHolder.getLocale().getLanguage();
        StringBuilder textBuilder = new StringBuilder();
        // ユーザーへ送信 ドライバーへ送信
        email.setTo(to);

        if (lang == EnumLanguageType.JA_JP.getCode()) {
            email.setSubject("【XXXXX配車サービス】より受付完了のお知らせ_注文No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>ご利用ありがとうございます。</div>");
            textBuilder.append("<div>下記の通り、配車受付のお知らせです。</div>");
            textBuilder.append("<div>Yukoyoアプリログイン後、決済処理へお進みください。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
            }
            textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>予約金額：" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else if (lang == EnumLanguageType.EN_US.getCode()) {
            email.setSubject("【XXXXX配車サービス】Notice of completion of reception_Order No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>Thank you for using our service.</div>");
            textBuilder.append("<div>The following is the notice of delivery reception.</div>");
            textBuilder.append("<div>After logging in to the Oukasystem app, proceed to payment processing.</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>Order No:" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>Departure Time:" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>Booking Driver:" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>Reservation Course:" + order.getCarName() + "</div>");
            }

            textBuilder.append("<div>Boarding Address:" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>Get off Address:" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>Customer Remark:" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>Booking Amount:" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else {
            email.setSubject("【XXXXX配車サービス】订单确认成功通知_订单No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>感谢您使用Yukoyo。</div>");
            textBuilder.append("<div>以下是您的订单详情。</div>");
            textBuilder.append("<div>请登录APP查询支付操作。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>订单编号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>旅行日期：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>预约司机：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>预约套餐：" + order.getCarName() + "</div>");
            }
            textBuilder.append("<div>上车地址：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下车地址：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>乘客备注：" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>订单金额：" + order.getOrderPrice() + "日元</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");
        }

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    public void sendEmailConfirmToDriver(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();

        // ユーザーへ送信 ドライバーへ送信
        email.setTo(to);
        email.setSubject("【XXXXX配車サービス】より受付完了のお知らせ_注文No（" + order.getOrderNo() + "）");
        textBuilder.append("<html><body>");
        textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、配車受付のお知らせです。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
        }

        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    // 【XXXXX配車サービス】より予約キャンセルのお知らせ_注文No（

    public void sendEmailCompanyCancelToUser(String to, OrderEntity order) {
        Email email = new Email();
        String lang = LocaleContextHolder.getLocale().getLanguage();
        StringBuilder textBuilder = new StringBuilder();
        // 顧客へ送信
        email.setTo(to);

        if (lang == EnumLanguageType.JA_JP.getCode()) {
            email.setSubject("【XXXXX配車サービス】より予約キャンセルのお知らせ_注文No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
            textBuilder.append("<div>ご利用ありがとうございます。</div>");
            textBuilder.append("<div>ご選択いただいた、ハイヤー会社より混雑のため</div>");
            textBuilder.append("<div>配車の受付ができませんでした。</div>");
            textBuilder.append("<div>他のハイヤー会社かドライバーをご予約してください。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
            }

            textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else if (lang == EnumLanguageType.EN_US.getCode()) {
            email.setSubject("【XXXXX配車サービス】Notice of reservation cancellation_Order No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + ":  </div>");
            textBuilder.append("<div>Thank you for using our service.</div>");
            textBuilder.append("<div>Due to congestion from the selected hire company</div>");
            textBuilder.append("<div>I was unable to receive the delivery.</div>");
            textBuilder.append("<div>Please book with another hire company or driver.</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>Order No:" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>Departure Time:" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>Booking Driver:" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>Reservation Course:" + order.getCarName() + "</div>");
            }

            textBuilder.append("<div>Boarding Address:" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>Get off Address:" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>Customer Remark:" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>Booking Amount:" + order.getOrderPrice() + "円</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        } else {
            email.setSubject("【XXXXX配車サービス】订单取消通知_订单No（" + order.getOrderNo() + "）");

            textBuilder.append("<html><body>");
            textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
            textBuilder.append("<div>感谢您使用Yukoyo。</div>");
            textBuilder.append("<div>因公司服务繁忙，暂时无法受理您的订单。</div>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");

            textBuilder.append("<div>订单编号：" + order.getOrderNo() + "</div>");
            textBuilder.append("<div>旅行日期：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                    + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
            if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
                textBuilder.append("<div>预约司机：" + order.getDriverName() + "</div>");
            }
            if (null != order.getCarName() && !order.getCarName().isEmpty()) {
                textBuilder.append("<div>预约套餐：" + order.getCarName() + "</div>");
            }
            textBuilder.append("<div>上车地址：" + order.getOrderFromDetails() + "</div>");
            textBuilder.append("<div>下车地址：" + order.getOrderToDetails() + "</div>");
            textBuilder.append("<div>乘客备注：" + order.getCustomerRemark() + "</div>");
            textBuilder.append("<div>订单金额：" + order.getOrderPrice() + "日元</div>");
            textBuilder.append("<br/>");
            textBuilder.append(getEmailFooter());
            textBuilder.append("</html></body>");

        }

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    public void sendEmailUserCancelToUser(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();

        email.setTo(to);
        email.setSubject("【XXXXX配車サービス】より予約キャンセル受付のお知らせ_注文No（" + order.getOrderNo() + "）");

        textBuilder.append("<html><body>");
        textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、予約キャンセル受付のお知らせです。。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
        }
        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append("<div>※予約後、キャセル手数料は下記の通りです。円</div>");
        textBuilder.append("<div>1日以内キャンセル手数料：全額。</div>");
        textBuilder.append("<div>2日以上キャンセル手数料：無料。</div>");
        textBuilder.append("<br/>");
        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");
        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    public void sendEmailUserCancelToCompany(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();

        email.setTo(to);
        email.setSubject("【XXXXX配車サービス】より予約キャンセル受付のお知らせ_注文No（" + order.getOrderNo() + "）");

        textBuilder.append("<html><body>");
        textBuilder.append("<div>会社様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、予約キャンセル受付のお知らせです。。</div>");
        textBuilder.append("<div>管理画面より、ご確認ください。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
        }

        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append("<div>※予約後、キャセル手数料は下記の通りです。円</div>");
        textBuilder.append("<div>1日以内キャンセル手数料：全額。</div>");
        textBuilder.append("<div>2日以上キャンセル手数料：無料。</div>");
        textBuilder.append("<br/>");
        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");
        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    // 【XXXXX配車サービス】配車3日前のお知らせ_注文No（
    public void sendEmail3DayToUser(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();

        email.setTo(to);

        email.setSubject("【XXXXX配車サービス】乗車3日前のお知らせ_注文No（" + order.getOrderNo() + "）");

        textBuilder.append("<html><body>");
        textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、乗車3日前お知らせです。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
        }

        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>Order No:" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>Departure Time:" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>Booking Driver:" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>Reservation Course:" + order.getCarName() + "</div>");
        }

        textBuilder.append("<div>Boarding Address:" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>Get off Address:" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>Customer Remark:" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>Booking Amount:" + order.getOrderPrice() + "円</div>");

        textBuilder.append("<br/>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>订单编号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>旅行日期：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>预约司机：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>预约套餐：" + order.getCarName() + "</div>");
        }
        textBuilder.append("<div>上车地址：" + order.getOrderFromDetails() + "</div>");

        textBuilder.append("<div>下车地址：" + order.getOrderToDetails() + "</div>");

        textBuilder.append("<div>乘客备注：" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>订单金额：" + order.getOrderPrice() + "日元</div>");
        textBuilder.append("<br/>");

        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");

        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    // 【XXXXX配車サービス】配車1日前のお知らせ_注文No（
    public void sendEmail1DayToUser(String to, OrderEntity order) {
        Email email = new Email();
        StringBuilder textBuilder = new StringBuilder();

        email.setTo(to);
        email.setSubject("【XXXXX配車サービス】乗車1日前のお知らせ_注文No（" + order.getOrderNo() + "）");

        textBuilder.append("<html><body>");
        textBuilder.append("<div>" + order.getCustomerName() + "様:  </div>");
        textBuilder.append("<div>ご利用ありがとうございます。</div>");
        textBuilder.append("<div>下記の通り、乗車1日前お知らせです。</div>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>注文番号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>出発日時：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>ドライバー：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>車両：" + order.getCarName() + "</div>");
        }

        textBuilder.append("<div>乗車住所：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下車住所：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>お客様の備考" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>決済金額：" + order.getOrderPrice() + "円</div>");
        textBuilder.append("<br/>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");
        textBuilder.append("<div>Order No:" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>Departure Time:" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>Booking Driver:" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>Reservation Course:" + order.getCarName() + "</div>");
        }
        if (null != order.getOrderFromDetails() && !order.getOrderFromDetails().isEmpty()) {
            textBuilder.append("<div>Boarding Address:" + order.getOrderFromDetails() + "</div>");
        }
        if (null != order.getOrderToDetails() && !order.getOrderToDetails().isEmpty()) {
            textBuilder.append("<div>Get off Address:" + order.getOrderToDetails() + "</div>");
        }
        textBuilder.append("<div>Booking Amount:" + order.getOrderPrice() + "円</div>");

        textBuilder.append("<br/>");
        textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
        textBuilder.append("<br/>");

        textBuilder.append("<div>订单编号：" + order.getOrderNo() + "</div>");
        textBuilder.append("<div>旅行日期：" + CommonUtils.getFormatDate(order.getStartTime()) + "　～　"
                + CommonUtils.getFormatDate(order.getEndTime()) + "</div>");
        if (null != order.getDriverName() && !order.getDriverName().isEmpty()) {
            textBuilder.append("<div>预约司机：" + order.getDriverName() + "</div>");
        }
        if (null != order.getCarName() && !order.getCarName().isEmpty()) {
            textBuilder.append("<div>预约套餐：" + order.getCarName() + "</div>");
        }
        textBuilder.append("<div>上车地址：" + order.getOrderFromDetails() + "</div>");
        textBuilder.append("<div>下车地址：" + order.getOrderToDetails() + "</div>");
        textBuilder.append("<div>乘客备注：" + order.getCustomerRemark() + "</div>");
        textBuilder.append("<div>订单金额：" + order.getOrderPrice() + "日元</div>");
        textBuilder.append("<br/>");
        textBuilder.append(getEmailFooter());
        textBuilder.append("</html></body>");
        email.setText(textBuilder.toString());
        email = sendMail(email);
        if (email.getStatus().equals("fail")) {
            throw new BaseException(EnumMailCheck.SEND_ERROR.getErrorMsg(), email.getErrorMsg());
        }
    }

    /**
     * メール送信共用文言
     * 
     * @return
     */
    public String getEmailFooter() {
        String lang = LocaleContextHolder.getLocale().getLanguage();

        String content = "";

        if (lang == EnumLanguageType.JA_JP.getCode()) {
            content = "<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>" +
                    "<div>ご不明点等あれば、下記アプリ提供会社までお問い合わせくださいませ。</div>" +
                    "<br/>" +
                    "<div>株式会社クラウドビジネスソリューションズ</div>" +
                    "<div>Mail：info@cbsdata.co.jp</div>" +
                    "<div>電話：03-6910-0896</div>" +
                    "<div>緊急連絡：090-6107-5888</div>" +
                    "<br/>" +
                    "<div>※このメールアドレスは、配信専用です。</div>" +
                    "<div>このメッセージに返信しないようお願いいたします。</div>";
        } else if (lang == EnumLanguageType.EN_US.getCode()) {
            content = "<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>" +
                    "<div>ご不明点等あれば、下記アプリ提供会社までお問い合わせくださいませ。</div>" +
                    "<br/>" +
                    "<div>株式会社クラウドビジネスソリューションズ</div>" +
                    "<div>Mail：info@cbsdata.co.jp</div>" +
                    "<div>電話：03-6910-0896</div>" +
                    "<div>緊急連絡：090-6107-5888</div>" +
                    "<br/>" +
                    "<div>※このメールアドレスは、配信専用です。</div>" +
                    "<div>このメッセージに返信しないようお願いいたします。</div>";
        } else {
            content = "<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>" +
                    "<div>有任何疑问请以以下方式联系咨询。</div>" +
                    "<br/>" +
                    "<div>株式会社クラウドビジネスソリューションズ</div>" +
                    "<div>Mail：info@cbsdata.co.jp</div>" +
                    "<div>电话：03-6910-0896</div>" +
                    "<div>紧急联系：090-6107-5888</div>" +
                    "<br/>" +
                    "<div>※此邮件为自动发送。</div>" +
                    "<div>请勿回复此邮件，谢谢。</div>";
        }
        return content;
    }
}