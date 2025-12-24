package org.commitlink.procure.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthUserResponseEntity(
  long id,
  String email,
  String firstName,
  String lastName,
  String role,
  Set<String> permissions,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
