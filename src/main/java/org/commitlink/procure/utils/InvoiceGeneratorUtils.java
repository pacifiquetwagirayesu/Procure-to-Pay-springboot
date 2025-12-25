package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.ERROR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.commitlink.procure.models.purchase.PurchaseItem;
import org.commitlink.procure.models.purchase.PurchaseRequest;

@Slf4j
public class InvoiceGeneratorUtils {

  private static byte[] generateInvoice(PurchaseRequest purchaseRequest) throws IOException {
    try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);

      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        float margin = 50;
        float yPosition = page.getMediaBox().getHeight() - margin;
        float pageWidth = page.getMediaBox().getWidth();

        // Company Header
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Commit Link");
        contentStream.endText();
        yPosition -= 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Company");
        contentStream.endText();
        yPosition -= 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Address: Rusororo, Gasabo, Kigali, Rwanda");
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Phone: +63 995 508 7385");
        contentStream.endText();
        yPosition -= 15;

        // Created by information
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        String createdBy =
          "Created by: " +
          (purchaseRequest.getCreatedBy().getFirstName() != null ? purchaseRequest.getCreatedBy().getFirstName() : "") +
          " " +
          (purchaseRequest.getCreatedBy().getLastName() != null ? purchaseRequest.getCreatedBy().getLastName() : "");
        contentStream.showText(createdBy);
        contentStream.endText();
        yPosition -= 12;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(
          "Email: " + (purchaseRequest.getCreatedBy().getEmail() != null ? purchaseRequest.getCreatedBy().getEmail() : "")
        );
        contentStream.endText();
        yPosition -= 12;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(
          "Role: " + (purchaseRequest.getCreatedBy().getRole() != null ? purchaseRequest.getCreatedBy().getRole() : "")
        );
        contentStream.endText();
        yPosition -= 30;

        // Invoice Title
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("INVOICE");
        contentStream.endText();
        yPosition -= 30;

        // Invoice Details
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        String invoiceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Invoice No: INV-" + purchaseRequest.getId());
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Date: " + invoiceDate);
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Status: " + purchaseRequest.getStatus());
        contentStream.endText();
        yPosition -= 30;

        // Purchase Details Section
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Purchase Details");
        contentStream.endText();
        yPosition -= 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Title: " + (purchaseRequest.getTitle() != null ? purchaseRequest.getTitle() : ""));
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        String description = purchaseRequest.getDescription() != null ? purchaseRequest.getDescription() : "";
        if (description.length() > 80) {
          description = description.substring(0, 80) + "...";
        }
        contentStream.showText("Description: " + description);
        contentStream.endText();
        yPosition -= 35;

        // Items Table Header
        float tableTop = yPosition;
        float tableMargin = margin;
        float tableWidth = pageWidth - (2 * margin);

        // Draw table header background - using new color rgb(22, 128, 60)
        contentStream.setNonStrokingColor(22 / 255f, 128 / 255f, 60 / 255f);
        contentStream.addRect(tableMargin, yPosition - 20, tableWidth, 20);
        contentStream.fill();

        // Table Headers
        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);

        float col1 = tableMargin + 10;
        float col2 = tableMargin + 200;
        float col3 = tableMargin + 300;
        float col4 = tableMargin + 370;
        float col5 = tableMargin + 450;

        contentStream.beginText();
        contentStream.newLineAtOffset(col1, yPosition - 14);
        contentStream.showText("Item");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(col2, yPosition - 14);
        contentStream.showText("Description");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(col3, yPosition - 14);
        contentStream.showText("Qty");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(col4, yPosition - 14);
        contentStream.showText("Unit Price");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(col5, yPosition - 14);
        contentStream.showText("Total");
        contentStream.endText();

        yPosition -= 25;
        contentStream.setNonStrokingColor(0f, 0f, 0f);

        // Table Items - from database
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        if (purchaseRequest.getItems() != null) {
          for (PurchaseItem item : purchaseRequest.getItems()) {
            // Draw row separator
            contentStream.setStrokingColor(0.9f, 0.9f, 0.9f);
            contentStream.moveTo(tableMargin, yPosition);
            contentStream.lineTo(tableMargin + tableWidth, yPosition);
            contentStream.stroke();

            yPosition -= 20;

            contentStream.beginText();
            contentStream.newLineAtOffset(col1, yPosition);
            contentStream.showText(item.getItemName() != null ? item.getItemName() : "");
            contentStream.endText();

            // Description with smaller font size (7pt) to fit at least 50 characters
            String desc = item.getDescription() != null ? item.getDescription() : "";
            if (desc.length() > 50) {
              desc = desc.substring(0, 50) + "...";
            }

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
            contentStream.beginText();
            contentStream.newLineAtOffset(col2, yPosition);
            contentStream.showText(desc);
            contentStream.endText();

            // Reset font size for other columns
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

            contentStream.beginText();
            contentStream.newLineAtOffset(col3, yPosition);
            contentStream.showText(String.valueOf(item.getQuantity()));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(col4, yPosition);
            contentStream.showText("$" + (item.getUnitPrice() != null ? item.getUnitPrice() : "0.00"));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(col5, yPosition);
            contentStream.showText("$" + (item.getTotalPrice() != null ? item.getTotalPrice() : "0.00"));
            contentStream.endText();

            yPosition -= 5;
          }
        }

        // Draw bottom border
        yPosition -= 15;
        contentStream.setStrokingColor(0.5f, 0.5f, 0.5f);
        contentStream.setLineWidth(2);
        contentStream.moveTo(tableMargin, yPosition);
        contentStream.lineTo(tableMargin + tableWidth, yPosition);
        contentStream.stroke();

        // Totals Section
        yPosition -= 30;
        float totalLabelX = pageWidth - margin - 150;
        float totalValueX = pageWidth - margin - 70;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
        contentStream.beginText();
        contentStream.newLineAtOffset(totalLabelX, yPosition);
        contentStream.showText("Subtotal:");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(totalValueX, yPosition);
        contentStream.showText("$" + (purchaseRequest.getAmount() != null ? purchaseRequest.getAmount() : "0.00"));
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(totalLabelX, yPosition);
        contentStream.showText("Tax:");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(totalValueX, yPosition);
        contentStream.showText("$0.00");
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(totalLabelX, yPosition);
        contentStream.showText("Shipping:");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(totalValueX, yPosition);
        contentStream.showText("$0.00");
        contentStream.endText();
        yPosition -= 30;

        // Total Due - Highlighted with new color rgb(22, 128, 60)
        contentStream.setNonStrokingColor(22 / 255f, 128 / 255f, 60 / 255f);
        contentStream.addRect(totalLabelX - 10, yPosition - 5, 160, 25);
        contentStream.fill();

        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(totalLabelX, yPosition + 5);
        contentStream.showText("TOTAL DUE:");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(totalValueX, yPosition + 5);
        contentStream.showText("$" + (purchaseRequest.getAmount() != null ? purchaseRequest.getAmount() : "0.00"));
        contentStream.endText();

        // Footer
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, 85);
        contentStream.showText("Payment Information:");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, 72);
        contentStream.showText("Mobile Money (MoMo): +250781835505");
        contentStream.endText();

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 9);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, 55);
        contentStream.showText("Thank you for your business!");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, 42);
        contentStream.showText("Payment due within 30 days. Please include invoice number with payment.");
        contentStream.endText();
      }

      document.save(baos);
      return baos.toByteArray();
    }
  }

  public static void generateInvoice(PurchaseRequest purchaseRequest, String outputPath) {
    try {
      byte[] pdfBytes = generateInvoice(purchaseRequest);
      Files.write(Paths.get(outputPath), pdfBytes);
    } catch (IOException e) {
      log.info(ERROR, e.getMessage());
    }
  }
}
