package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record PurchaseItemResponse(
  Long id,
  String name,
  String description,
  Integer quantity,
  @JsonProperty("unit_price") @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal unitPrice,
  @JsonProperty("total_price") @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal totalPrice
) {}
