package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public record PurchaseRequestDTO(
  @NotBlank(message = "purchase title is required") String title,
  @NotBlank(message = "purchase description is required") String description,
  @DecimalMin(value = "0.01", message = "total amount must greater than 0") @JsonProperty("total_amount") BigDecimal totalAmount,
  List<PurchaseItemDTO> items
) {}
