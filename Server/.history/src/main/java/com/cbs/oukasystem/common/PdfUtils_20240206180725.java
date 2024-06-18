package com.cbs.oukasystem.common;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PdfUtils {

    public static void downLoadCSVFile() {
        TString filename = "D:/Program Files/pdfTest/testTable3.pdf";
    pdf.createPDF(filename);
    System.out.println("打印完成");
    }

}
