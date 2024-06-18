package com.cbs.oukasystem.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class PdfUtils {
    private static String PDF_DEST = "pdf/";
    private static final String FONT = "HeiseiKakuGo-W5";
    private static final String PLACARD_DEST = "defaultPlacard.pdf";
    private static final String INVOICE_DEST = "トラベル請求書01.pdf";

    // 字体
    private BaseFont baseFont;

    /**
     * 填充模板，并导出填充后模板
     * 
     * @param dest
     * @throws IOException
     */
    public static void invoicePdf(Map<String, Object> dataMap, String dest) throws DocumentException, IOException {

        OutputStream fos = new FileOutputStream(dest);
        generatePdf(fos, dataMap, PLACARD_DEST);

        ExportsUtils.exportPdf(dest);

    }

    public static void defaultPlacard(
            Map<String, Object> dataMap, String dest) throws IOException, DocumentException {
                String url = FileUploadUtil.uploadPath + "/" + PDF_DEST + dest;
        File file = new File(url);
        // ファイルの存在をチェック
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream fos = new FileOutputStream(url);
        generatePdf(fos, dataMap, PLACARD_DEST);

        return 

        // ExportsUtils.exportPdf(dest);
    }

    public static void generatePdf(OutputStream os,
            Map<String, Object> dataMap, String template) throws IOException, DocumentException {

        InputStream templateInputStream = new ClassPathResource(PDF_DEST + template).getInputStream();

        PdfReader pdfReader = new PdfReader(templateInputStream);
        PdfStamper stamper = new PdfStamper(pdfReader, os);
        AcroFields acroFields = stamper.getAcroFields();

        // 设置字体
        acroFields.addSubstitutionFont(getDefaultFont());

        // 执行填充
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            acroFields.setField(entry.getKey(), String.valueOf(entry.getValue()));
        }

        stamper.setFormFlattening(true);
        stamper.close();
    }

    /*
     * 默认字体
     */
    private static BaseFont getDefaultFont() throws DocumentException, IOException {
        // return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
        // BaseFont.NOT_EMBEDDED);
        return BaseFont.createFont(FONT, "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED);
    }

}