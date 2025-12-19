package org.commitlink.procure.repository;

import org.commitlink.procure.models.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITokenRepository extends CrudRepository<Token, Long> {}
