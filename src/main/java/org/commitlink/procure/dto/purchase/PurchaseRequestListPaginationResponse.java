package org.commitlink.procure.dto.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PurchaseRequestListPaginationResponse(
  @JsonProperty("total_element") long totalElement,
  @JsonProperty("total_pages") int totalPages,
  @JsonProperty("has_next") boolean hasNext,
  @JsonProperty("has_previous") boolean hasPrevious,
  Iterable<PurchaseRequestResponse> purchaseRequests
) {}
