package org.commitlink.procure.dto.purchase.proforma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProformaBillingInfoDTO(String invoice_no, String date, String status, ProformaPaymentInfoDto payment_info) {}
