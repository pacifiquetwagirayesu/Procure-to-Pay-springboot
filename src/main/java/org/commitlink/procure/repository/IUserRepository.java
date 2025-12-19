package org.commitlink.procure.repository;

import java.util.Optional;
import org.commitlink.procure.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
