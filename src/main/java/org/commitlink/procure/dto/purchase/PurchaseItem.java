package org.commitlink.procure.dto.purchase;

import java.math.BigDecimal;

public record PurchaseItem(String title, String description, BigDecimal amount) {}
