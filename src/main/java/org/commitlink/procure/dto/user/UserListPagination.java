package org.commitlink.procure.dto.user;

public record UserListPagination(
  long totalElement,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious,
  Iterable<UserEntityResponse> users
) {}
