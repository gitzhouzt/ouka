package com.cbs.oukasystem.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;

/*
 * 共通サービス
 */
public class CommonUtils {

    /**
     * 確認コード(６桁)を作成
     *
     * @return 確認コード
     */
    public static String getVerifyCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    /**
     * 擬似乱数(１０桁)を作成
     *
     * @return 擬似乱数
     */
    public static String getRandomName() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 注文番号を作成
     *
     * @return 擬似乱数
     */
    public static String getOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int newNum = (int) ((Math.random() * 9 + 1) * 10000000);
        return time + String.valueOf(newNum);
    }

    /**
     * シリアル(６桁)を作成
     *
     * @return シリアル
     */
    public static String getSeqNum() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    /**
     * システム日付を取得(yyyyMMddHHmmss)
     *
     * @return システム日付(yyyyMMddHHmmss)
     */
    public static String getCurrentDateNo() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(d);
    }

    /**
     * システム日付を取得(yyyyMMddHHmmss)
     *
     * @return システム日付(yyyyMMddHHmmss)
     */
    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sf.format(d);
    }

    public static String getCurrentDate(String pattern) {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(d);
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sf.format(date);
    }

    public static String getFormatDate(Date date, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(date);
    }

    public static Date getDateStr(String dateStr, String pattern) {
        try {
            if (null == pattern || pattern.isEmpty()) {
                pattern = "yyyy/MM/dd HH:mm:ss";
            }
            SimpleDateFormat sf = new SimpleDateFormat(pattern);
            return sf.parse(dateStr);
        } catch (ParseException e) {
            throw new BaseException(EnumDataCheck.VERIFY_FAILED, e.getMessage());
        }
    }

    /**
     * Javaのネイティブダイジェストを使用してSHA256暗号化を実装する
     * 
     * @param str メッセージ
     * @return
     */
    public static String toSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * バイトを16進数に変換
     * 
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1ビットを取得して0操作を構成する
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * @param equipmentType
     * @param equipmentNo
     * @return no
     */
    public static String getNew4BitEquipmentNo(String equipmentType, Long equipmentNo) {
        String newEquipmentNo = Constants.DEFAULT_NUMBER;
        if (equipmentNo != null) {
            Long newEquipment = equipmentNo + 1;
            newEquipmentNo = String.format(equipmentType + "%04d", newEquipment);
        }
        return newEquipmentNo;
    }

    /**
     * @param equipmentType
     * @param equipmentNo
     * @return no
     */
    public static String getNewDateEquipmentNo(String equipmentType, Long equipmentNo) {
        String newEquipmentNo = Constants.DEFAULT_NUMBER;
        if (equipmentNo != null) {
            Long newEquipment = equipmentNo + 1;
            Date date = new Date();
            newEquipmentNo = String.format(equipmentType + "_%tL" + "%02d", date, date, newEquipment);
        }
        return newEquipmentNo;
    }

}