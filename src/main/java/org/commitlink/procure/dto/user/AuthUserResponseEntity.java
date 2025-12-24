package org.commitlink.procure.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Set;

public record AuthUserResponseEntity(
  long id,
  String email,
  String firstName,
  String lastName,
  String role,
  Set<String> permissions,
  @JsonIgnore String password,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
