package org.commitlink.procure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestListPaginationResponse;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.services.IPurchaseRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/requests")
@Tag(name = "Purchase Request Endpoints")
public class PurchaseRequestController {

  private final IPurchaseRequestService purchaseRequestService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "create purchase request")
  @ResponseStatus(HttpStatus.CREATED)
  public long createPurchaseRequest(
    @Parameter(
      description = "Purchase request JSON data",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PurchaseRequestDTO.class))
    ) @RequestPart @Valid PurchaseRequestDTO purchaseRequest,
    @RequestPart(required = false) MultipartFile proforma
  ) {
    return purchaseRequestService.createPurchaseRequest(purchaseRequest, proforma);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "purchase request detail")
  public PurchaseRequestResponse getPurchaseRequest(@PathVariable long id) {
    return purchaseRequestService.getPurchaseRequestById(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Purchase request list request")
  public PurchaseRequestListPaginationResponse purchaseRequestListResponse(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "3") int size
  ) {
    return purchaseRequestService.purchaseRequestList(page, size);
  }

  @PatchMapping(value = "/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Purchase status update request")
  public PurchaseRequestResponse purchaseRequestStatusApprove(@PathVariable long id) {
    return null;
  }

  @PatchMapping(value = "/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Purchase status update request")
  public PurchaseRequestResponse purchaseRequestStatusReject(@PathVariable long id) {
    return null;
  }

  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Purchase request update")
  public PurchaseRequestResponse purchaseRequestUpdate(@PathVariable long id, @RequestBody PurchaseRequestDTO req) {
    return null;
  }

  @PostMapping(value = "/{id}/submit-receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Purchase Request Submit Receipt")
  public PurchaseRequestResponse purchaseRequestSubmitReceipt(@PathVariable long id, @RequestPart MultipartFile receipt) {
    return null;
  }
}
