package com.ai.healthcare.utility;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class PdfGenerator {
    public static void main(String[] args) {
        try {
            String dest = "sample.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph(
                    "This is a sample document for RAG verification. The patient has successfully recovered from the surgery using the new AI protocols."));
            document.close();
            System.out.println("PDF Created: " + new File(dest).getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
