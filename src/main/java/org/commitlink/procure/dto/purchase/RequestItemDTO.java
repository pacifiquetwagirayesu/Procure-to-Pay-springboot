package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RequestItemDTO(String name, String description, int quantity, @JsonProperty("unit_price") BigDecimal unitPrice) {}
