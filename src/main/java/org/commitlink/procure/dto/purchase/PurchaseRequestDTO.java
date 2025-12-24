package org.commitlink.procure.dto.purchase;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseRequestDTO(String title, String description, BigDecimal amount, List<RequestItemDTO> items) {}
