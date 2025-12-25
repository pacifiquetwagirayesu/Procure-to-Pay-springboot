package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.UserMapper.userPurchaseRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseItemDTO;
import org.commitlink.procure.dto.purchase.PurchaseItemResponse;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.dto.purchase.proforma.ProformaMetadataDTO;
import org.commitlink.procure.models.purchase.PurchaseItem;
import org.commitlink.procure.models.purchase.PurchaseRequest;

@Slf4j
public class PurchaseRequestMapper {

  public static Function<PurchaseItemDTO, PurchaseItem> mapRequestItem = item -> {
    int quantity = item.quantity();
    BigDecimal unitPrice = item.unitPrice().setScale(2, RoundingMode.HALF_UP);
    BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    return PurchaseItem
      .builder()
      .itemName(item.itemName())
      .description(item.description())
      .quantity(item.quantity())
      .unitPrice(item.unitPrice())
      .totalPrice(totalPrice)
      .build();
  };

  public static Function<List<PurchaseItem>, BigDecimal> calculateTotalAmount = items -> {
    BigDecimal totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    for (PurchaseItem item : items) {
      if (item.getTotalPrice() != null) {
        totalAmount = totalAmount.add(item.getTotalPrice());
      }
    }
    return totalAmount;
  };

  public static Function<PurchaseItem, PurchaseItemResponse> requestItemResponseMapper = item ->
    new PurchaseItemResponse(
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
      res.getProformaMetadata() != null ? proformaMetadataDTO(res) : null,
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

  private static ProformaMetadataDTO proformaMetadataDTO(PurchaseRequest request) {
    try {
      return new ObjectMapper().readValue(request.getProformaMetadata(), ProformaMetadataDTO.class);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
    return null;
  }

  public static List<PurchaseItem> getPurchaseItems(PurchaseRequestDTO purchaseRequest) {
    return purchaseRequest.items() != null ? purchaseRequest.items().stream().map(item -> mapRequestItem.apply(item)).toList() : null;
  }
}
