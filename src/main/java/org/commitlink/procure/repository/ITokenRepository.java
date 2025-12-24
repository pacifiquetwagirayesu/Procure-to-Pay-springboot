package org.commitlink.procure.repository;

import java.util.Optional;
import org.commitlink.procure.models.user.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITokenRepository extends CrudRepository<Token, Long> {
  Optional<Token> findByRefreshToken(String refreshToken);
}
