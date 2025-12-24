package org.commitlink.procure.controller.purchase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.services.IPurchaseRequestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchase-request")
@Tag(name = "Purchase Request Endpoints")
@Slf4j
public class PurchaseRequestController {

  private final IPurchaseRequestService purchaseRequestService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "create purchase request")
  public long createPurchaseRequest(
    @Parameter(
      description = "Purchase request JSON data",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseRequestDTO.class))
    ) @RequestPart PurchaseRequestDTO purchaseRequest,
    @RequestPart(required = false) MultipartFile proforma
  ) {
    return purchaseRequestService.createPurchaseRequest(purchaseRequest, proforma);
  }

  @GetMapping("/{id}")
  @Operation(summary = "get purchase request details")
  public PurchaseRequestResponse getPurchaseRequest(@PathVariable long id) {
    return purchaseRequestService.getPurchaseRequestById(id);
  }
}
