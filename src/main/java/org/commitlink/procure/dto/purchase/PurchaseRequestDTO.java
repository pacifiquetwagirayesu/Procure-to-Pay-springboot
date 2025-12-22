package org.commitlink.procure.dto.purchase;

import java.math.BigDecimal;
import java.util.List;
import org.commitlink.procure.models.purchase.Status;

public record PurchaseRequestDTO(String title, String description, BigDecimal amount, Status status, List<PurchaseItem> items) {}
