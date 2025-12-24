package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.commitlink.procure.models.purchase.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PurchaseRequestResponse(
  Long id,
  String title,
  String description,
  BigDecimal amount,
  Status status,
  List<RequestItemResponse> items,
  UserPurchaseRequest createdBy,
  List<UserPurchaseRequest> approvedBy,
  String proforma,
  @JsonProperty("proforma_metadata") String proformaMetadata,
  @JsonProperty("purchase_order") String purchaseOrder,
  @JsonProperty("purchase_order_metadata") String purchaseOrderMetadata,
  String receipt,
  @JsonProperty("receipt_metadata") String receiptMetadata,
  @JsonProperty("receipt_validation") String receiptValidation,
  LocalDateTime approvedAt,
  LocalDateTime rejectedAt,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
