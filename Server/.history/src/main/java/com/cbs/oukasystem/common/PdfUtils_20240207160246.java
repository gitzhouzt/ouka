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
import java.util.Map;

public class PdfUtils {
    private static String SRC = "src/main/resources/";
    private static String PDF_DEST = "pdf/";
    private static final String FONT = "src/main/resources/NotoSans-Regular.ttf";

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
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();

        // 设置字体
        form.addSubstitutionFont(getDefaultFont());

        // 执行填充
        doFill(stamper, form, dataMap);

        reader.close();
        stamper.close();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        // ExportsUtils.export(dest);

    }

    /*
     * 填充文本
     */
    private static void doFill(PdfStamper stamper, AcroFields form, Map<String, Object> dataMap)
            throws DocumentException, IOException {
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            form.setField(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    /*
     * 默认字体
     */
    private static BaseFont getDefaultFont() throws DocumentException, IOException {
        return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    }

}