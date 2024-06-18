package com.cbs.oukasystem.common;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.lay

 public static void downLoadCSVFile(String screenName, List<String> fieldList,
            List sheetDataList) {
    try (Document document = new Document(new PdfDocument(new PdfWriter("./hello-pdf.pdf")))) {
        document.add(new Paragraph("Hello PDF!"));
    }
}
}
