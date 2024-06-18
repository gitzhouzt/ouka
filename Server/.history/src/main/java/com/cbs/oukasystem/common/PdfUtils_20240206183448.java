package com.cbs.oukasystem.common;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.RoundDotsBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.renderer.FlexContainerRenderer;

import java.io.IOException;
import java.io.InterruptedIOException;

public class PdfUtils {
    private static String SRC = "src/main/resources/";
    private static String DEST = "target/";

    
    
    
        
        oid sigFieldWithDivAppearance(String dest) throws IOException, InterruptedIOException {
                t document = new Document(new PdfDocument(new PdfWriter(dest)))) {
        Div div = new Div();
        div.add(new Paragraph("Paragraph inside div with red dotted border and pin
                .setBorder(new DashedBorder(ColorConstants.RED, 1)).setBackgroundColor(ColorConstan
        Div flexContainer = new Div();
        flexContainer.setProperty(Property.FLEX_WRAP, FlexWrapPropertyValue.WRAP);
                ainer.setProperty(Property.FLEX
                        tProperty(Property.ALIGN_ITEMS, AlignmentPropertyValue.CENTER);
                        d(new Image(ImageDataFactory.create(SRC + "image.jpg")).scale(0.1f, 0.3f)
                        ing(10)).add(new List()
                         ListItem("Flex container with").setListSymbol(ListNumberingType.ZAPF_DINGBATS_1))
                         ListItem(" imag
                .add(new ListItem(" wrap row-reverse,").setListSymbol(List
                .add(new ListItem(" green dots border,").setListSymbol(ListNumbe
                .setPadding(10)

        div.add(flexContainer);
        
                eFieldAppearance appearance = new SignatureFieldApp
                ce.setContent(div)
                .setFontColor(ColorConstants.WHITE).setFontSize(10)
                .setBackgroundColor(Co
                .setBorder(new So
     
         }
    }
}