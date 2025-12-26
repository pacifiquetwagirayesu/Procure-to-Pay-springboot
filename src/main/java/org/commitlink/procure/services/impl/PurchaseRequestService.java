package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.CloudinaryUtils.uploadParams;
import static org.commitlink.procure.utils.Constants.ERROR;
import static org.commitlink.procure.utils.Constants.LEVEL_ONE_UPDATE_ERROR;
import static org.commitlink.procure.utils.Constants.LEVEL_TWO_UPDATE_ERROR;
import static org.commitlink.procure.utils.Constants.PURCHASE_REQUEST_NOT_FOUND;
import static org.commitlink.procure.utils.Constants.STATUS_CANNOT_CHANGE;
import static org.commitlink.procure.utils.Constants.STATUS_UPDATE_SUCCESS;
import static org.commitlink.procure.utils.Constants.SUCCESS;
import static org.commitlink.procure.utils.Constants.URL;
import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND;
import static org.commitlink.procure.utils.PurchaseRequestMapper.calculateTotalAmount;
import static org.commitlink.procure.utils.PurchaseRequestMapper.getPurchaseRequestItems;
import static org.commitlink.procure.utils.PurchaseRequestMapper.purchaseRequestResponseMapper;
import static org.commitlink.procure.utils.RoleUtils.canNotBeUpdated;
import static org.commitlink.procure.utils.RoleUtils.getRoles;
import static org.commitlink.procure.utils.RoleUtils.isApprover;
import static org.commitlink.procure.utils.RoleUtils.isLevelOneApprover;
import static org.commitlink.procure.utils.RoleUtils.isLevelTwoApprover;
import static org.commitlink.procure.utils.RoleUtils.statusCanNotBeChanged;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.purchase.PurchaseRequestDTO;
import org.commitlink.procure.dto.purchase.PurchaseRequestListPaginationResponse;
import org.commitlink.procure.dto.purchase.PurchaseRequestResponse;
import org.commitlink.procure.exceptions.NotFoundException;
import org.commitlink.procure.exceptions.PurchaseRequestNotFound;
import org.commitlink.procure.exceptions.UserNotFoundException;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.Status;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.User;
import org.commitlink.procure.repository.IPurchaseRequestRepository;
import org.commitlink.procure.repository.IUserRepository;
import org.commitlink.procure.services.IPurchaseRequestService;
import org.commitlink.procure.utils.FileUtils;
import org.commitlink.procure.utils.PurchaseOrderInvoiceGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
  private final ExecutorService executorService;
  private final ObjectMapper mapper;

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
      .items(getPurchaseRequestItems(purchaseRequest))
      .createdBy(user)
      .proforma(null)
      .build();

    if (proforma != null && !proforma.isEmpty()) {
      try {
        Map upload = cloudinary.uploader().upload(proforma.getBytes(), uploadParams(proforma.getOriginalFilename()));
        request.setProforma(upload.get(URL).toString());
        String metaData = FileUtils.extractTextFromProforma(proforma);
        request.setProformaMetadata(mapper.writeValueAsString(metaData).replaceAll("\"\"\"", " "));
      } catch (Exception e) {
        log.info(ERROR, e.getMessage());
      }
    }

    BigDecimal totalAmount = calculateTotalAmount.apply(request.getItems());
    if (!Objects.equals(totalAmount, BigDecimal.ZERO)) request.setAmount(totalAmount);
    request.setAmount(totalAmount);

    PurchaseRequest savedRequest = requestRepository.save(request);
    return savedRequest.getId();
  }

  @Override
  public PurchaseRequestResponse getPurchaseRequestById(long id) {
    PurchaseRequest purchaseRequest = requestRepository
      .findById(id)
      .orElseThrow(() -> new PurchaseRequestNotFound(PURCHASE_REQUEST_NOT_FOUND));
    return purchaseRequestResponseMapper.apply(purchaseRequest);
  }

  @Override
  public PurchaseRequestListPaginationResponse purchaseRequestList(int page, int size) {
    int pageNumber = Math.max(0, (page - 1));
    PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Page<PurchaseRequest> purchaseContent;

    if (isApprover(authUser)) {
      purchaseContent = requestRepository.findByStatus(Status.PENDING, pageRequest);
    } else {
      purchaseContent = requestRepository.findByCreatedBy_Email(authUser.getUsername(), pageRequest);
    }

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

  @Override
  @PreAuthorize("hasRole('APPROVER_LEVEL_1') or hasRole('APPROVER_LEVEL_2')")
  public Map<String, String> approvePurchaseRequest(long id) {
    PurchaseRequest purchaseRequest = requestRepository.findById(id).orElseThrow(() -> new NotFoundException(PURCHASE_REQUEST_NOT_FOUND));
    if (statusCanNotBeChanged(purchaseRequest)) return Map.of(SUCCESS, STATUS_CANNOT_CHANGE);

    AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findByEmail(authUser.getUsername()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

    if (statusCanNotBeChanged(purchaseRequest)) return Map.of(SUCCESS, STATUS_CANNOT_CHANGE);

    List<String> roles = getRoles(purchaseRequest);

    if (isLevelOneApprover(roles, authUser)) {
      purchaseRequest.getApprovedBy().add(user);
    } else if (canNotBeUpdated(roles, authUser)) {
      throw new AuthorizationDeniedException(LEVEL_TWO_UPDATE_ERROR);
    } else if (isLevelTwoApprover(roles, authUser)) {
      purchaseRequest.getApprovedBy().add(user);
      purchaseRequest.setStatus(Status.APPROVED);
      purchaseRequest.setApprovedAt(LocalDateTime.now());

      try {
        PurchaseOrderInvoiceGenerator.generatePurchaseOrderInvoice(purchaseRequest);
      } catch (IOException e) {
        log.error(ERROR, e.getMessage());
      }
    } else {
      throw new AuthorizationDeniedException(LEVEL_ONE_UPDATE_ERROR);
    }

    PurchaseRequest request = requestRepository.save(purchaseRequest);
    return Map.of(SUCCESS, STATUS_UPDATE_SUCCESS.formatted(request.getTitle()));
  }

  @Override
  @PreAuthorize("hasRole('APPROVER_LEVEL_1') or hasRole('APPROVER_LEVEL_2')")
  public Map<String, String> rejectPurchaseRequest(long id) {
    PurchaseRequest purchaseRequest = requestRepository.findById(id).orElseThrow(() -> new NotFoundException(PURCHASE_REQUEST_NOT_FOUND));
    if (statusCanNotBeChanged(purchaseRequest)) return Map.of(SUCCESS, STATUS_CANNOT_CHANGE);

    AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    List<String> roles = getRoles(purchaseRequest);

    if (isLevelOneApprover(roles, authUser)) {
      purchaseRequest.setStatus(Status.REJECTED);
    } else if (canNotBeUpdated(roles, authUser)) {
      throw new AuthorizationDeniedException(LEVEL_TWO_UPDATE_ERROR);
    } else if (isLevelTwoApprover(roles, authUser)) {
      purchaseRequest.setStatus(Status.REJECTED);
    } else {
      throw new AuthorizationDeniedException(LEVEL_ONE_UPDATE_ERROR);
    }

    purchaseRequest.setRejectedAt(LocalDateTime.now());
    PurchaseRequest request = requestRepository.save(purchaseRequest);
    return Map.of(SUCCESS, STATUS_UPDATE_SUCCESS.formatted(request.getTitle()));
  }
}
