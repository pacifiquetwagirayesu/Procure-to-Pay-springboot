package org.commitlink.procure.dto.purchase;

import java.math.BigDecimal;

public record RequestItemDTO(String name, String description, int quantity, BigDecimal unitPrice) {}
