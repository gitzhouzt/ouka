package com.cbs.oukasystem.common;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
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

    public static void sigFieldWithDivAppearance(String dest) throws IOException, InterruptedIOException {
        try (Document document = new Document(new PdfDocument(new PdfWriter(dest)))) {
            Div div = new Div();
            div.add(new Paragraph("Paragraph inside div with red dotted border and pink background")
                    .setBorder(new DashedBorder(ColorConstants.RED, 1)).setBackgroundColor(ColorConstants.PINK));
            Div flexContainer = new Div();
            flexContainer.setProperty(Property.FLEX_WRAP, FlexWrapPropertyValue.WRAP);
            flexContainer.setProperty(Property.FLEX_DIRECTION, FlexDirectionPropertyValue.ROW_REVERSE);
            flexContainer.setProperty(Property.ALIGN_ITEMS, AlignmentPropertyValue.CENTER);
            flexContainer.add(new Image(ImageDataFactory.create(SRC + "image.jpg")).scale(0.1f, 0.3f)
                    .setPadding(10)).add(new List()
                            .add(new ListItem("Flex container with").setListSymbol(ListNumberingType.ZAPF_DINGBATS_1))
                            .add(new ListItem(" image and list,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_2))
                            .add(new ListItem(" wrap row-reverse,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_3))
                            .add(new ListItem(" green dots border,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_4))
                            .setPadding(10))
                    .setBorder(new RoundDotsBorder(ColorConstants.GREEN, 10));
            flexContainer.setNextRenderer(new FlexContainerRenderer(flexContainer));
            div.add(flexContainer);

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

    private static final String FONT = "src/main/resources/NotoSans-Regular.ttf";

    public static void test(String dest) throws IOException {
        // PDF/a-4 requires a PDF 2.0 document
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0));
        // Grab the image color matching profile
        InputStream inputStream = new FileInputStream("src/main/resources/sRGB_CS_profile.icm");
        // Create the PDF/a-4 document by instantiating a PdfADocument object and
        // passing the PDF/a-4 conformance level
        PdfADocument pdfDocument = new PdfADocument(writer, PdfAConformanceLevel.PDF_A_4,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", inputStream));
        // Taking care of the additional PDF/A requirements
        pdfDocument.getCatalog().setLang(new PdfString("nl-nl"));
        pdfDocument.setTagged();
        PdfDocumentInfo info = pdfDocument.getDocumentInfo();
        info
                .setTitle("title")
                .setAuthor("Author")
                .setSubject("Subject")
                .setCreator("Creator")
                .setKeywords("Metadata, iText, PDF")
                .setCreator("My program using iText")
                .addCreationDate();

        Document document = new Document(pdfDocument);
        // PDF/a requires fonts to be embedded
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        Paragraph element = new Paragraph("Hello World").setFont(font).setFontSize(10);
        document.add(element);

        Image logoImage = new Image(ImageDataFactory.create("src/main/resources/logo.png"));
        // PDF/a requires images to have alternative text
        logoImage.getAccessibilityProperties().setAlternateDescription("Logo");
        document.add(logoImage);

        pdfDocument.close();

    }

}