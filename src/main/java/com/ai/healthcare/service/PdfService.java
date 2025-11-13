package com.ai.healthcare.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public byte[] createPdf(String content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        PdfPage page = pdf.addNewPage();

        String imagePath = "src/main/resources/static/img.png";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image logo = new Image(imageData);
        logo.setWidth(50); // adjust size as needed
        logo.setAutoScale(true);

        // Create the paragraph
        Paragraph title = new Paragraph("WESTERN UNION RECEIPT")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

// Create a 2-column table
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 5})); // 1 part image, 5 parts text
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginTop(30);

// Add cells: image left, paragraph right
        table.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(title).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));

// Add table to document
        document.add(table);
        // Now add content on top of background
        /*document.add(new Paragraph("WESTERN UNION RECEIPT")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30));*/

        document.add(new Paragraph(content)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(20));

        document.close();
        return out.toByteArray();
    }
}
