package com.cbs.oukasystem.common;

import com.itextpdf.text.pdf.BaseFont;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PdfUtils {
    private static String SRC = "src/main/resources/";
    private static String PDF_DEST = "pdf/";
    private static final String FONT = "src/main/resources/NotoSans-Regular.ttf";

    private final transient com.itextpdf.text.pdf.PdfReader reader;
    // 字体
    private BaseFont baseFont;

    private boolean addPageNumber;

    public static void tablePdf(String dest) throws IOException {

        ExportsUtils.export(dest);

    }

}