package org.commitlink.procure.utils;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class InvoiceGeneratorUtil {

  private static final float MARGIN = 50;
  private static final float Y_START = 750;

  public static void generatePaidInvoice() throws IOException {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    PDPageContentStream content = new PDPageContentStream(document, page);

    float yPosition = Y_START;

    // Header
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("INVOICE");
    content.endText();

    // PAID Stamp
    content.setNonStrokingColor(new Color(220, 53, 69)); // Red color
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 48);
    content.beginText();
    content.newLineAtOffset(350, yPosition - 10);
    content.showText("PAID");
    content.endText();

    yPosition -= 60;

    // Invoice Details
    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Invoice Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
    content.newLineAtOffset(0, -15);
    content.showText("Payment Method: Mobile Money (MoMo)");
    content.newLineAtOffset(0, -15);
    content.showText("MoMo Number: +250781835505");
    content.endText();

    yPosition -= 60;

    // Purchase Details
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Office Supplies Purchase");
    content.endText();

    yPosition -= 20;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Monthly office stationery and supplies");
    content.endText();

    yPosition -= 40;

    // Created By
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Created By:");
    content.endText();

    yPosition -= 18;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Pacifique Twagirayesu (STAFF)");
    content.newLineAtOffset(0, -15);
    content.showText("Email: user@example.com");
    content.endText();

    yPosition -= 40;

    // Table Header
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
    content.setNonStrokingColor(new Color(52, 58, 64));

    // Draw header background
    content.setNonStrokingColor(new Color(248, 249, 250));
    content.addRect(MARGIN, yPosition - 15, 495, 25);
    content.fill();

    content.setNonStrokingColor(Color.BLACK);
    content.beginText();
    content.newLineAtOffset(MARGIN + 5, yPosition);
    content.showText("Item");
    content.newLineAtOffset(200, 0);
    content.showText("Description");
    content.newLineAtOffset(150, 0);
    content.showText("Qty");
    content.newLineAtOffset(60, 0);
    content.showText("Unit Price");
    content.newLineAtOffset(80, 0);
    content.showText("Total");
    content.endText();

    yPosition -= 30;

    // Table Row
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN + 5, yPosition);
    content.showText("Notebook");
    content.newLineAtOffset(200, 0);
    content.showText("200-page ruled");
    content.newLineAtOffset(150, 0);
    content.showText("5");
    content.newLineAtOffset(60, 0);
    content.showText("$3.50");
    content.newLineAtOffset(80, 0);
    content.showText("$17.50");
    content.endText();

    yPosition -= 40;

    // Draw line
    content.setStrokingColor(new Color(222, 226, 230));
    content.setLineWidth(1);
    content.moveTo(MARGIN, yPosition);
    content.lineTo(545, yPosition);
    content.stroke();

    yPosition -= 25;

    // Totals
    float totalsX = 400;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Subtotal:");
    content.newLineAtOffset(100, 0);
    content.showText("$41.50");
    content.endText();

    yPosition -= 18;

    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Tax:");
    content.newLineAtOffset(100, 0);
    content.showText("$0.00");
    content.endText();

    yPosition -= 18;

    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Shipping:");
    content.newLineAtOffset(100, 0);
    content.showText("$0.00");
    content.endText();

    yPosition -= 25;

    // Draw line
    content.moveTo(totalsX, yPosition);
    content.lineTo(545, yPosition);
    content.stroke();

    yPosition -= 20;

    // Total Due
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Total Due:");
    content.newLineAtOffset(100, 0);
    content.showText("$41.50");
    content.endText();

    yPosition -= 60;

    // Payment Status
    content.setNonStrokingColor(new Color(25, 135, 84));
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Payment Status: PAID");
    content.endText();

    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition - 15);
    content.showText("Paid on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
    content.endText();

    yPosition -= 60;

    // Signature Section
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Authorized Signature:");
    content.endText();

    yPosition -= 30;

    // Draw signature line
    content.setStrokingColor(Color.BLACK);
    content.moveTo(MARGIN, yPosition);
    content.lineTo(MARGIN + 200, yPosition);
    content.stroke();

    // Signature text (simulated)
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 14);
    content.beginText();
    content.newLineAtOffset(MARGIN + 20, yPosition + 5);
    content.showText("Pacifique Twagirayesu");
    content.endText();

    yPosition -= 20;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Pacifique Twagirayesu - STAFF");
    content.endText();

    // Footer
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
    content.setNonStrokingColor(new Color(108, 117, 125));
    content.beginText();
    content.newLineAtOffset(MARGIN, 40);
    content.showText("Thank you for your business!");
    content.endText();

    content.close();

    document.save("paid_invoice.pdf");
    document.close();
  }
}
