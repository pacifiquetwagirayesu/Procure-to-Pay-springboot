package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.CloudinaryUtil.uploadParams;
import static org.commitlink.procure.utils.Constants.PURCHASE_REQUEST_NOT_FOUND;
import static org.commitlink.procure.utils.Constants.UPLOAD_ERROR;
import static org.commitlink.procure.utils.Constants.URL;
import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND;
import static org.commitlink.procure.utils.PurchaseRequestUtil.mapRequestItem;
import static org.commitlink.procure.utils.PurchaseRequestUtil.purchaseRequestResponseMapper;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.exceptions.PurchaseRequestNotFound;
import org.commitlink.procure.exceptions.UserNotFoundException;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.Status;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.User;
import org.commitlink.procure.repository.IPurchaseRequestRepository;
import org.commitlink.procure.repository.IUserRepository;
import org.commitlink.procure.services.IPurchaseRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PurchaseRequestService implements IPurchaseRequestService {

  private final Cloudinary cloudinary;
  private final IPurchaseRequestRepository requestRepository;
  private final IUserRepository userRepository;

  @Override
  @PreAuthorize("hasRole('STAFF')")
  public long createPurchaseRequest(PurchaseRequestDTO purchaseRequest, MultipartFile proforma) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AuthUser authUser = (AuthUser) authentication.getPrincipal();
    User user = userRepository.findByEmail(authUser.getUsername()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

    PurchaseRequest request = PurchaseRequest
      .builder()
      .title(purchaseRequest.title())
      .description(purchaseRequest.description())
      .amount(purchaseRequest.amount())
      .status(Status.PENDING)
      .items(purchaseRequest.items().stream().map(item -> mapRequestItem.apply(item)).toList())
      .createdBy(user)
      .proforma(null)
      .build();

    if (!proforma.isEmpty()) {
      try {
        String proformaUrl = cloudinary
          .uploader()
          .upload(proforma.getBytes(), uploadParams(proforma.getOriginalFilename()))
          .get(URL)
          .toString();
        request.setProforma(proformaUrl);
      } catch (Exception e) {
        log.info(UPLOAD_ERROR, e.getMessage());
      }
    }
//    requestRepository.save(request).getId();
    return 1;
  }

  @Override
  public PurchaseRequestResponse getPurchaseRequestById(long id) {
    PurchaseRequest purchaseRequest = requestRepository
      .findById(id)
      .orElseThrow(() -> new PurchaseRequestNotFound(PURCHASE_REQUEST_NOT_FOUND));
    return purchaseRequestResponseMapper.apply(purchaseRequest);
  }
}
