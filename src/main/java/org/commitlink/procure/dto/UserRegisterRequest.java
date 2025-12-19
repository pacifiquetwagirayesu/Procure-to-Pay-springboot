package org.commitlink.procure.dto;

import static org.commitlink.procure.models.Role.APPROVER_LEVEL_1;
import static org.commitlink.procure.models.Role.APPROVER_LEVEL_2;
import static org.commitlink.procure.models.Role.FINANCE;
import static org.commitlink.procure.models.Role.STAFF;
import static org.commitlink.procure.utils.Constants.EMAIL_REQUIRED;
import static org.commitlink.procure.utils.Constants.FIRSTNAME_REQUIRED;
import static org.commitlink.procure.utils.Constants.INVALID_EMAIL;
import static org.commitlink.procure.utils.Constants.INVALID_ROLE_MESSAGE;
import static org.commitlink.procure.utils.Constants.PASSWORD_REQUIRED;
import static org.commitlink.procure.utils.Constants.ROLE_REQUIRED;

import jakarta.validation.constraints.Email;
import java.util.Set;
import java.util.stream.Collectors;
import org.commitlink.procure.exceptions.InvalidInputException;

public record UserRegisterRequest(
    @Email(message = INVALID_EMAIL) String email,
    String firstName,
    String lastName,
    String role,
    String password) {

  public UserRegisterRequest {
    if (firstName == null || firstName.isBlank())
      throw new InvalidInputException(FIRSTNAME_REQUIRED);
    if (email == null || email.isBlank()) throw new InvalidInputException(EMAIL_REQUIRED);
    if (password == null || password.isBlank()) throw new InvalidInputException(PASSWORD_REQUIRED);
    if (role == null || role.isBlank()) throw new InvalidInputException(ROLE_REQUIRED);

    Set<String> validRoles =
        Set.of(STAFF, APPROVER_LEVEL_1, APPROVER_LEVEL_2, FINANCE).stream()
            .map(r -> r.name().toLowerCase())
            .collect(Collectors.toSet());

    if (!validRoles.contains(role)) {
      throw new InvalidInputException(
          INVALID_ROLE_MESSAGE.formatted(role.toLowerCase(), validRoles));
    }

    firstName = firstName.strip();
    lastName = lastName.strip();
    email = email.strip();
    role = role.strip();
    role = role.toUpperCase();
  }
}
