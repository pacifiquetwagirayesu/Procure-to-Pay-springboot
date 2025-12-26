package org.commitlink.procure.dto.purchase.proforma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProformaPaymentInfoDto(String mobile_money, String due_date) {}
