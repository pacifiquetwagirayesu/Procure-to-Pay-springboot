package org.commitlink.procure.services;

import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestListPaginationResponse;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IPurchaseRequestService {
  long createPurchaseRequest(PurchaseRequestDTO purchaseRequest, MultipartFile proforma);

  PurchaseRequestResponse getPurchaseRequestById(long id);

  PurchaseRequestListPaginationResponse purchaseRequestList(int page, int size);
}
