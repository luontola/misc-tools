package net.orfjackal.experimental;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.*;

/**
 * Fills the form fields of a PDF file with the names of the fields, to make it
 * easier to see which field is named what.
 *
 * @author Esko Luontola
 * @since 20.2.2011
 */
public class FillPdfFormFields {

    public static void main(String[] args) throws Exception {
        String src = args[0];
        String dest = args[1];

        PdfReader reader = new PdfReader(new FileInputStream(src));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest), '\0', true);
        try {
            AcroFields form = stamper.getAcroFields();
            BaseFont font = BaseFont.createFont("C:\\Windows\\Fonts\\ARIALN.TTF", BaseFont.IDENTITY_H, false);
            for (String field : form.getFields().keySet()) {
                form.setFieldProperty(field, "textfont", font, null);
                form.setFieldProperty(field, "textcolor", new BaseColor(Color.BLACK), null);
                form.setFieldProperty(field, "textsize", new Float(6.0), null);
                form.setField(field, field);  // must be done AFTER setting the properties, or the properties will be reset
            }
        } finally {
            stamper.close();
        }
    }
}
