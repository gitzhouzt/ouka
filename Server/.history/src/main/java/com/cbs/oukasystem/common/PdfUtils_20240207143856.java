package com.cbs.oukasystem.common;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.RoundDotsBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.renderer.FlexContainerRenderer;
import com.itextpdf.pdfa.PdfADocument;

import net.bytebuddy.jar.asm.Label;

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
    private static String DEST = "target/";
    private static final String FONT = "src/main/resources/NotoSans-Regular.ttf";

    public static void sigFieldWithDivAppearance(String dest) throws IOException, InterruptedIOException {
        try (Document document = new Document(new PdfDocument(new PdfWriter(dest)))) {
            // Div div = new Div();
            // div.add(new Paragraph("Paragraph inside div with red dotted border and pink
            // background")
            // .setBorder(new DashedBorder(ColorConstants.RED,
            // 1)).setBackgroundColor(ColorConstants.PINK));
            // Div flexContainer = new Div();
            // flexContainer.setProperty(Property.FLEX_WRAP, FlexWrapPropertyValue.WRAP);
            // flexContainer.setProperty(Property.FLEX_DIRECTION,
            // FlexDirectionPropertyValue.ROW_REVERSE);
            // flexContainer.setProperty(Property.ALIGN_ITEMS,
            // AlignmentPropertyValue.CENTER);
            // flexContainer.add(new Image(ImageDataFactory.create(SRC +
            // "image.jpg")).scale(0.1f, 0.3f)
            // .setPadding(10)).add(new List()
            // .add(new ListItem("Flex container
            // with").setListSymbol(ListNumberingType.ZAPF_DINGBATS_1))
            // .add(new ListItem(" image and
            // list,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_2))
            // .add(new ListItem(" wrap
            // row-reverse,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_3))
            // .add(new ListItem(" green dots
            // border,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_4))
            // .setPadding(10))
            // .setBorder(new RoundDotsBorder(ColorConstants.GREEN, 10));
            // flexContainer.setNextRenderer(new FlexContainerRenderer(flexContainer));
            // div.add(flexContainer);

            Div div = new Div();
            Div titleDiv = new Div();
            titleDiv.setProperty(Property.FLEX_DIRECTION, FlexDirectionPropertyValue.ROW_REVERSE);
            titleDiv.setProperty(Property.ALIGN_ITEMS, AlignmentPropertyValue.CENTER);
            titleDiv.add(new Paragraph("鈴木様")).setProperty(Property.FONT_SIZE, 40);
            ;
            titleDiv.setNextRenderer(new FlexContainerRenderer(titleDiv));
            div.add(titleDiv);

            SignatureFieldAppearance appearance = new SignatureFieldAppearance("form SigField");
            appearance.setContent(div)
                    .setFontColor(ColorConstants.WHITE).setFontSize(10)
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.MAGENTA, 2))
                    .setInteractive(true);
            document.add(appearance);
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();

        OutputStream out = null;
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(dest, "utf-8"));
            out = response.getOutputStream();
            InputStream is = new FileInputStream(dest);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}