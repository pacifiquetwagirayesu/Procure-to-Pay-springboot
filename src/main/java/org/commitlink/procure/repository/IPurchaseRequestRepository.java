package org.commitlink.procure.repository;

import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
  Page<PurchaseRequest> findByCreatedBy_Email(String createdByEmail, Pageable pageable);

  Page<PurchaseRequest> findByStatus(Status status, Pageable pageable);
}
