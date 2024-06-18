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

    
    
    
        oid sigFieldWithDivA
        (Document document = new Document(new PdfDocument(new PdfWriter(dest)))) {
                = new Div();
        div.add(new Paragraph("Paragra
                .setBorder(new DashedBorder(ColorConstants.RED, 1)).setBackgroundC
        Div flexContainer = new Div();
        flexContainer.setProperty(Property.FLEX_WRAP, FlexWrapPropertyValue.WRAP);
        flexContainer.setProperty(Property.FLEX_DIRECTION, FlexDirectionPropertyValue.ROW_REVERSE
                ainer.setProperty(Property.ALIG
                        d(new Image(ImageDataFactory.create(SRC + "image.jpg")).scale(0.1f, 0.3f)
                        ing(10)).add(new List()
                        .add(new ListItem("Flex container with").setListSymbol(ListNumberingType.ZAPF_DINGBATS_1)
                        .add(new ListItem(" image and list,").setListSymbol(ListNumberingType.ZAPF_DINGBATS_2))
                        .add(new ListIte
                        .add(new ListItem(" green dots border,").setListSy
                        .setPadding(10))
                .setBorder(new 

        div.add(flexContainer);
        
                eFieldAppearance appearance = new SignatureFieldApp
                ce.setContent(div)
                .setFontColor(ColorConstants.WHITE).setFontSize(10)
                .setBackgroundColor(Co
                .setBorder(new So
     
         }
    }
}