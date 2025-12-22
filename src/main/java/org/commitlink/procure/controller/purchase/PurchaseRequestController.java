package org.commitlink.procure.controller.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests")
@Tag(name = "Purchase Request Endpoints")
@Slf4j
public class PurchaseRequestController {

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public long createPurchaseRequest(@RequestPart PurchaseRequestDTO purchaseRequest, @RequestPart File proforma) {
    return 0;
  }
}
