package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.CloudinaryUtil.uploadParams;
import static org.commitlink.procure.utils.Constants.PURCHASE_REQUEST_NOT_FOUND;
import static org.commitlink.procure.utils.Constants.UPLOAD_ERROR;
import static org.commitlink.procure.utils.Constants.URL;
import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND;
import static org.commitlink.procure.utils.PurchaseRequestUtil.calculateTotalAmount;
import static org.commitlink.procure.utils.PurchaseRequestUtil.mapRequestItem;
import static org.commitlink.procure.utils.PurchaseRequestUtil.purchaseRequestResponseMapper;

import com.cloudinary.Cloudinary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestListPaginationResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
  private final IPurchaseRequestRepository purchaseRequestRepository;
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
      .amount(purchaseRequest.totalAmount().setScale(2, RoundingMode.HALF_UP))
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

    BigDecimal totalAmount = calculateTotalAmount.apply(request.getItems());
    if (!Objects.equals(totalAmount, BigDecimal.ZERO)) request.setAmount(totalAmount);

    return purchaseRequestRepository.save(request).getId();
  }

  @Override
  public PurchaseRequestResponse getPurchaseRequestById(long id) {
    PurchaseRequest purchaseRequest = purchaseRequestRepository
      .findById(id)
      .orElseThrow(() -> new PurchaseRequestNotFound(PURCHASE_REQUEST_NOT_FOUND));
    log.info("res: {}", purchaseRequest);
    return purchaseRequestResponseMapper.apply(purchaseRequest);
  }

  @Override
  public PurchaseRequestListPaginationResponse purchaseRequestList(int page, int size) {
    int pageNumber = Math.max(0, (page - 1));
    PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Page<PurchaseRequest> purchaseContent = purchaseRequestRepository.findByCreatedBy_Email(authUser.getUsername(), pageRequest);

    List<PurchaseRequestResponse> requestResponses = purchaseContent
      .getContent()
      .stream()
      .map(purchaseRequest -> purchaseRequestResponseMapper.apply(purchaseRequest))
      .toList();

    return new PurchaseRequestListPaginationResponse(
      purchaseContent.getTotalElements(),
      purchaseContent.getTotalPages(),
      purchaseContent.hasNext(),
      purchaseContent.hasPrevious(),
      requestResponses
    );
  }
}
