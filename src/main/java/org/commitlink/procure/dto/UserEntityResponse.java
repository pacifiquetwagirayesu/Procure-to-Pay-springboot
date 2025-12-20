package org.commitlink.procure.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserEntityResponse(
  long id,
  String email,
  String firstName,
  String lastName,
  String role,
  Set<String> permissions,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {}
