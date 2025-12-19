package org.commitlink.procure.dto;

public record UserListPagination(
  long totalElement,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious,
  Iterable<UserEntityResponse> users
) {}
