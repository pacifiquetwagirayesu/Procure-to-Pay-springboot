package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
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
  UserPurchaseRequest approvedBy,
  String proforma,
  String proformaMetadata,
  String purchaseOrder,
  String purchaseOrderMetadata,
  String receipt,
  String receiptMetadata,
  String receiptValidation,
  LocalDateTime approvedAt,
  LocalDateTime rejectedAt,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
