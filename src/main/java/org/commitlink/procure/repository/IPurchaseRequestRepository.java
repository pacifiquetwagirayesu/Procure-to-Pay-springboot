package org.commitlink.procure.repository;

import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {}
