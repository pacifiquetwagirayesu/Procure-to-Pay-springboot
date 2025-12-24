package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.UserMapper.userPurchaseRequest;

import java.util.function.Function;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.dto.purchase.RequestItemDTO;
import org.commitlink.procure.dto.purchase.RequestItemResponse;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.RequestItem;

public class PurchaseRequestUtil {

  public static Function<RequestItemDTO, RequestItem> mapRequestItem = item ->
    RequestItem.builder().name(item.name()).description(item.description()).quantity(item.quantity()).unitPrice(item.unitPrice()).build();

  public static Function<RequestItem, RequestItemResponse> requestItemResponseMapper = item ->
    new RequestItemResponse(item.getId(), item.getName(), item.getDescription(), item.getQuantity(), item.getUnitPrice());

  public static Function<PurchaseRequest, PurchaseRequestResponse> purchaseRequestResponseMapper = res ->
    new PurchaseRequestResponse(
      res.getId(),
      res.getTitle(),
      res.getDescription(),
      res.getAmount(),
      res.getStatus(),
      res.getItems().stream().map(item -> requestItemResponseMapper.apply(item)).toList(),
      userPurchaseRequest.apply(res.getCreatedBy()),
      userPurchaseRequest.apply(res.getApprovedBy()),
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
