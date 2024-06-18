package com.cbs.oukasystem.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class PdfUtils {
    private static String SRC = "src/main/resources/";
    private static String PDF_DEST = "pdf/";
    private static final String FONT = "src/main/resources/NotoSans-Regular.ttf";
    private static final String PLACARD_DEST = "defaultPlacard.pdf";

    // 字体
    private BaseFont baseFont;

    /**
     * 填充模板，并导出填充后模板
     * 
     * @param dest
     * @throws IOException
     */
    public static void tablePdf(Map<String, Object> dataMap, String dest) throws DocumentException, IOException {

        PdfReader reader = new PdfReader(SRC + PDF_DEST + "トラベル請求書01.pdf");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields form = stamper.getAcroFields();

        // 设置字体
        form.addSubstitutionFont(getDefaultFont());

        // 执行填充
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            form.setField(entry.getKey(), String.valueOf(entry.getValue()));
        }

        stamper.setFormFlattening(true);
        stamper.close();

        // 生成pdf路径存放的路径
        OutputStream fos = new FileOutputStream(dest);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        bos.close();

        ExportsUtils.export(dest);

    }

    public static void defaultPlacardPdf(Map<String, Object> dataMap, String dest)
            throws DocumentException, IOException {

        PdfReader reader = new PdfReader(SRC + PDF_DEST + PLACARD_DEST);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields form = stamper.getAcroFields();
        // 设置字体
        form.addSubstitutionFont(getDefaultFont());

        // 执行填充
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            form.setField(entry.getKey(), String.valueOf(entry.getValue()));
        }

        stamper.setFormFlattening(true);
        stamper.close();

        // 生成pdf路径存放的路径
        OutputStream fos = new FileOutputStream(dest);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        bos.close();
        ExportsUtils.export(dest);

    }

    /*
     * 默认字体
     */
    private static BaseFont getDefaultFont() throws DocumentException, IOException {
        return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    }

}