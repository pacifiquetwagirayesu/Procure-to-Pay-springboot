package org.commitlink.procure.dto.purchase;

import java.math.BigDecimal;

public record RequestItemResponse(Long id, String name, String description, Integer quantity, BigDecimal unitPrice) {}
