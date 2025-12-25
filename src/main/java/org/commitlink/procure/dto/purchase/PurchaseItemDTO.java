package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PurchaseItemDTO(
  @JsonProperty("item_name") String itemName,
  String description,
  @Min(value = 1, message = "quantity must be at least 1") int quantity,
  @JsonProperty("unit_price")
  @NotNull(message = "unit price is required")
  @DecimalMin(value = "0.01", message = "unit price must greater than 0")
  BigDecimal unitPrice
) {}
