package org.commitlink.procure.dto.purchase.proforma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProformaMetaDataItemDTO(String item_name, int quantity, BigDecimal unit_price, BigDecimal total) {}
