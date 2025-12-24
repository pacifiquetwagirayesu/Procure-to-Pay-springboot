package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.UserMapper.userPurchaseRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.dto.purchase.RequestItemDTO;
import org.commitlink.procure.dto.purchase.RequestItemResponse;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.RequestItem;

public class PurchaseRequestUtil {

  public static Function<RequestItemDTO, RequestItem> mapRequestItem = item -> {
    int quantity = item.quantity();
    BigDecimal unitPrice = item.unitPrice().setScale(2, RoundingMode.HALF_UP);
    BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    return RequestItem
      .builder()
      .itemName(item.itemName())
      .description(item.description())
      .quantity(item.quantity())
      .unitPrice(item.unitPrice())
      .totalPrice(totalPrice)
      .build();
  };

  public static Function<List<RequestItem>, BigDecimal> calculateTotalAmount = items -> {
    BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    for (RequestItem item : items) {
      if (item.getTotalPrice() != null) {
        totalAmount = totalAmount.add(item.getTotalPrice());
      }
    }
    return totalAmount;
  };

  public static Function<RequestItem, RequestItemResponse> requestItemResponseMapper = item ->
    new RequestItemResponse(
      item.getId(),
      item.getItemName(),
      item.getDescription(),
      item.getQuantity(),
      item.getUnitPrice(),
      item.getTotalPrice()
    );

  public static Function<PurchaseRequest, PurchaseRequestResponse> purchaseRequestResponseMapper = res ->
    new PurchaseRequestResponse(
      res.getId(),
      res.getTitle(),
      res.getDescription(),
      res.getAmount(),
      res.getStatus(),
      res.getItems().stream().map(item -> requestItemResponseMapper.apply(item)).toList(),
      userPurchaseRequest.apply(res.getCreatedBy()),
      res.getApprovedBy().stream().map(approver -> userPurchaseRequest.apply(approver)).toList(),
      res.getProforma(),
      res.getProformaMetadata(),
      res.getPurchaseOrder(),
      res.getPurchaseOrderMetadata(),
      res.getReceipt(),
      res.getReceiptMetadata(),
      res.getReceiptValidation(),
      res.getApprovedAt(),
      res.getRejectedAt(),
      res.getCreatedAt(),
      res.getUpdatedAt()
    );
}
