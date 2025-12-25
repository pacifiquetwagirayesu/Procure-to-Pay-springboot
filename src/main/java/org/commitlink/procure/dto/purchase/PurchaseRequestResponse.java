package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonProperty("total_amount") @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal amount,
  Status status,
  List<PurchaseItemResponse> items,
  UserPurchaseRequest createdBy,
  List<UserPurchaseRequest> approvedBy,
  String proforma,
  @JsonProperty("purchase_order") String purchaseOrder,
  String receipt,
  @JsonProperty("receipt_validation") String receiptValidation,
  LocalDateTime approvedAt,
  LocalDateTime rejectedAt,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
