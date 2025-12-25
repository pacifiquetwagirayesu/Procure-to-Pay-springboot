package org.commitlink.procure.utils;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.commitlink.procure.models.purchase.PurchaseItem;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.user.User;

public class PurchaseOrderInvoiceGenerator {

  private static final float MARGIN = 50;
  private static final float Y_START = 750;
  private static final Color BRAND_COLOR = new Color(22, 128, 60);
  private static final float PAGE_WIDTH = 545;

  public static void generatePurchaseOrderInvoice(PurchaseRequest purchaseRequest) throws IOException {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    PDPageContentStream content = new PDPageContentStream(document, page);

    float yPosition = Y_START;

    // Header
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24);
    content.setNonStrokingColor(BRAND_COLOR);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("PURCHASE ORDER");
    content.endText();

    yPosition -= 50;

    // PO Details Section
    content.setNonStrokingColor(Color.BLACK);

    // Left column
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("PO Number:");
    content.endText();

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN + 80, yPosition);
    content.showText("PO-" + String.format("%06d", purchaseRequest.getId()));
    content.endText();

    // Right column - Status
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.beginText();
    content.newLineAtOffset(350, yPosition);
    content.showText("Status:");
    content.endText();

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.setNonStrokingColor(BRAND_COLOR);
    content.beginText();
    content.newLineAtOffset(400, yPosition);
    content.showText(purchaseRequest.getStatus().toString());
    content.endText();

    yPosition -= 18;

    // PO Date
    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("PO Date:");
    content.endText();

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN + 80, yPosition);
    content.showText(formatDate(purchaseRequest.getCreatedAt().toString()));
    content.endText();

    yPosition -= 18;

    // Request ID
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Request ID:");
    content.endText();

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN + 80, yPosition);
    content.showText("REQ-" + String.format("%06d", purchaseRequest.getId()));
    content.endText();

    yPosition -= 35;

    // Requested By Section
    drawSectionHeader(content, "REQUESTED BY", yPosition);
    yPosition -= 25;

    User createdBy = purchaseRequest.getCreatedBy();
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN, yPosition);
    content.showText("Email:");
    content.endText();

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(MARGIN + 80, yPosition);
    content.showText(createdBy.getEmail());
    content.endText();

    yPosition -= 35;

    // Order Items Section
    drawSectionHeader(content, "ORDER ITEMS", yPosition);
    yPosition -= 25;

    // Draw items table
    yPosition = drawItemsTable(content, purchaseRequest.getItems(), yPosition);

    yPosition -= 30;

    // Totals Section
    float totalsX = 380;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Subtotal:");
    content.newLineAtOffset(90, 0);
    content.showText("$" + purchaseRequest.getAmount());
    content.endText();

    yPosition -= 18;

    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Tax:");
    content.newLineAtOffset(90, 0);
    content.showText("$0.00");
    content.endText();

    yPosition -= 18;

    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Shipping:");
    content.newLineAtOffset(90, 0);
    content.showText("$0.00");
    content.endText();

    yPosition -= 18;

    // Draw line
    content.setStrokingColor(Color.BLACK);
    content.setLineWidth(1);
    content.moveTo(totalsX, yPosition);
    content.lineTo(PAGE_WIDTH, yPosition);
    content.stroke();

    yPosition -= 18;

    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
    content.beginText();
    content.newLineAtOffset(totalsX, yPosition);
    content.showText("Total Amount:");
    content.newLineAtOffset(90, 0);
    content.showText("$" + purchaseRequest.getAmount());
    content.endText();

    yPosition -= 40;

    // Approvals Section
    if (purchaseRequest.getApprovedBy() != null && !purchaseRequest.getApprovedBy().isEmpty()) {
      drawSectionHeader(content, "APPROVALS", yPosition);
      yPosition -= 25;
      yPosition = drawApprovalsTable(content, purchaseRequest.getApprovedBy(), purchaseRequest.getApprovedAt().toString(), yPosition);
    }

    yPosition -= 30;

    // Footer
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 8);
    content.setNonStrokingColor(new Color(108, 117, 125));
    content.beginText();
    content.newLineAtOffset(MARGIN, 40);
    content.showText("This is a computer-generated purchase order and is valid without signature.");
    content.endText();

    content.close();

    String filename = "po/purchase_order_" + purchaseRequest.getId() + ".pdf";
    document.save(filename);
    document.close();

    System.out.println("Purchase Order generated: " + filename);
  }

  private static void drawSectionHeader(PDPageContentStream content, String title, float yPosition) throws IOException {
    content.setNonStrokingColor(new Color(240, 240, 240));
    content.addRect(MARGIN, yPosition - 5, PAGE_WIDTH - MARGIN, 20);
    content.fill();

    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
    content.beginText();
    content.newLineAtOffset(MARGIN + 5, yPosition + 2);
    content.showText(title);
    content.endText();
  }

  private static float drawItemsTable(PDPageContentStream content, java.util.List<PurchaseItem> items, float yPosition) throws IOException {
    // Table column positions
    float col1 = MARGIN + 5; // #
    float col2 = col1 + 25; // Item
    float col3 = col2 + 120; // Description
    float col4 = col3 + 150; // Qty
    float col5 = col4 + 40; // Unit Price
    float col6 = col5 + 70; // Total

    // Table header background
    content.setNonStrokingColor(new Color(245, 245, 245));
    content.addRect(MARGIN, yPosition - 10, PAGE_WIDTH - MARGIN, 18);
    content.fill();

    // Table header
    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
    content.beginText();
    content.newLineAtOffset(col1, yPosition - 3);
    content.showText("#");
    content.newLineAtOffset(col2 - col1, 0);
    content.showText("Item");
    content.newLineAtOffset(col3 - col2, 0);
    content.showText("Description");
    content.newLineAtOffset(col4 - col3, 0);
    content.showText("Qty");
    content.newLineAtOffset(col5 - col4, 0);
    content.showText("Unit Price");
    content.newLineAtOffset(col6 - col5, 0);
    content.showText("Total");
    content.endText();

    yPosition -= 20;

    // Table rows
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
    int rowNumber = 1;
    for (PurchaseItem item : items) {
      content.beginText();
      content.newLineAtOffset(col1, yPosition);
      content.showText(String.valueOf(rowNumber));
      content.newLineAtOffset(col2 - col1, 0);
      content.showText(truncateText(item.getItemName(), 18));
      content.newLineAtOffset(col3 - col2, 0);
      content.showText(truncateText(item.getDescription(), 25));
      content.newLineAtOffset(col4 - col3, 0);
      content.showText(String.valueOf(item.getQuantity()));
      content.newLineAtOffset(col5 - col4, 0);
      content.showText("$" + item.getUnitPrice());
      content.newLineAtOffset(col6 - col5, 0);
      content.showText("$" + item.getTotalPrice());
      content.endText();

      yPosition -= 18;
      rowNumber++;
    }

    return yPosition;
  }

  private static float drawApprovalsTable(PDPageContentStream content, java.util.List<User> approvers, String approvedAt, float yPosition)
    throws IOException {
    // Table column positions
    float col1 = MARGIN + 5; // Level
    float col2 = col1 + 80; // Approver
    float col3 = col2 + 200; // Date
    float col4 = col3 + 100; // Status

    // Table header background
    content.setNonStrokingColor(new Color(245, 245, 245));
    content.addRect(MARGIN, yPosition - 10, PAGE_WIDTH - MARGIN, 18);
    content.fill();

    // Table header
    content.setNonStrokingColor(Color.BLACK);
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
    content.beginText();
    content.newLineAtOffset(col1, yPosition - 3);
    content.showText("Level");
    content.newLineAtOffset(col2 - col1, 0);
    content.showText("Approver");
    content.newLineAtOffset(col3 - col2, 0);
    content.showText("Date");
    content.newLineAtOffset(col4 - col3, 0);
    content.showText("Status");
    content.endText();

    yPosition -= 20;

    // Table rows
    content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
    String formattedDate = formatDateTime(approvedAt);

    for (User approver : approvers) {
      String level = approver.getRole().replace("APPROVER_LEVEL_", "Level ");

      content.beginText();
      content.newLineAtOffset(col1, yPosition);
      content.showText(level);
      content.newLineAtOffset(col2 - col1, 0);
      content.showText(truncateText(approver.getEmail(), 30));
      content.newLineAtOffset(col3 - col2, 0);
      content.showText(formattedDate);
      content.endText();

      // Status in green
      content.setNonStrokingColor(BRAND_COLOR);
      content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
      content.beginText();
      content.newLineAtOffset(col4, yPosition);
      content.showText("APPROVED");
      content.endText();

      content.setNonStrokingColor(Color.BLACK);
      content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);

      yPosition -= 18;
    }

    return yPosition;
  }

  private static String truncateText(String text, int maxLength) {
    if (text.length() <= maxLength) {
      return text;
    }
    return text.substring(0, maxLength - 3) + "...";
  }

  private static String formatDate(String dateTimeStr) {
    try {
      LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
      return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (Exception e) {
      return dateTimeStr;
    }
  }

  private static String formatDateTime(String dateTimeStr) {
    try {
      LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
      return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    } catch (Exception e) {
      return dateTimeStr;
    }
  }
}
