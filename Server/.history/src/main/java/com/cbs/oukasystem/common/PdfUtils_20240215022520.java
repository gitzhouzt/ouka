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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class PdfUtils {
    private static String SRC = "src/main/resources/";
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

        PdfReader reader = new PdfReader(SRC + PDF_DEST + INVOICE_DEST);

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
        stamper.getAcroFields().setGenerateAppearances(true);
        stamper.close();

        // 生成pdf路径存放的路径
        OutputStream fos = new FileOutputStream(dest);
        fos.write(bos.toByteArray());
        fos.flush();
        fos.close();
        bos.close();

        ExportsUtils.export(dest);

    }

    public static void generatePdf(OutputStream os,
            Map<String, Object> dataMap,String template) throws IOException, DocumentException {

        InputStream templateInputStream = new ClassPathResource(SRC + PDF_DEST + template).getInputStream();

        PdfReader pdfReader = new PdfReader(templateInputStream);
        PdfStamper stamper = new PdfStamper(pdfReader, os);
        AcroFields acroFields = stamper.getAcroFields();
        getDefaultFont());
        BaseFont base = BaseFont.createFont(fonts_ttf, BaseFont.IDENTITY_H, false);
        acroFields.addSubstitutionFont(base);
        // 填充模板中的占位符(属性名在制作模板时指定)
        acroFields.setField("name", message.getName());
        acroFields.setField("date", message.getDate());
        acroFields.setField("phone", message.getPhone());
        acroFields.setField("email", message.getEmail());
        acroFields.setField("school", message.getSchool());
        acroFields.setField("reason", message.getReason());
        acroFields.setField("speciality", message.getSpeciality());
        acroFields.setField("volunteerType", message.getVolunteerType());
        // 配置申请类型
        String volunteerTypes = buildVolunteerTypes(message.getVolunteerType(), dictData);
        acroFields.setField("volunteerTypes", volunteerTypes);
        // 配置志愿服务
        buildAllVolunteers(acroFields, allVolunteers, pickVolunteers);

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