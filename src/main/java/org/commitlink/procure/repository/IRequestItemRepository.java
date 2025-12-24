package org.commitlink.procure.repository;

import org.commitlink.procure.models.purchase.RequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRequestItemRepository extends JpaRepository<RequestItem, Long> {}
