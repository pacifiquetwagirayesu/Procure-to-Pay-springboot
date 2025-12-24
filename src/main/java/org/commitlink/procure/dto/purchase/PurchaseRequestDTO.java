package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public record PurchaseRequestDTO(
  @NotBlank(message = "purchase title is required") String title,
  @NotBlank(message = "purchase description is required") String description,
  @Min(value = 0, message = "totalAmount must be greater than 0") @JsonProperty("total_amount") BigDecimal totalAmount,
  List<RequestItemDTO> items
) {}
