package org.commitlink.procure.dto.purchase.proforma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProformaMetadataDTO(
  String created_by,
  String purchase_details,
  List<ProformaMetaDataItemDTO> items,
  ProformaBillingInfoDTO billing_info
) {}
