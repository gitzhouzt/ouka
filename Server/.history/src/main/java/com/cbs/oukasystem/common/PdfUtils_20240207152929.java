package com.cbs.oukasystem.common;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.qrcode.ByteArray;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    public static void tablePdf(Map<String, Object> dataMap, String dest) throws IOException {

        PdfReader reader = new PdfReader(SRC + PDF_DEST + "トラベル請求書01.pdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, out);

        ExportsUtils.export(dest);

    }

}