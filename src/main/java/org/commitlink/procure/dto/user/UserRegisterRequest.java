package org.commitlink.procure.dto.user;

import static org.commitlink.procure.models.user.Role.APPROVER_LEVEL_1;
import static org.commitlink.procure.models.user.Role.APPROVER_LEVEL_2;
import static org.commitlink.procure.models.user.Role.FINANCE;
import static org.commitlink.procure.models.user.Role.STAFF;
import static org.commitlink.procure.utils.Constants.EMAIL_REQUIRED;
import static org.commitlink.procure.utils.Constants.FIRSTNAME_REQUIRED;
import static org.commitlink.procure.utils.Constants.INVALID_EMAIL;
import static org.commitlink.procure.utils.Constants.INVALID_ROLE_MESSAGE;
import static org.commitlink.procure.utils.Constants.PASSWORD_REQUIRED;
import static org.commitlink.procure.utils.Constants.ROLE_REQUIRED;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import java.util.stream.Collectors;
import org.commitlink.procure.exceptions.InvalidInputException;

public record UserRegisterRequest(
  @Email(message = INVALID_EMAIL) @NotBlank(message = EMAIL_REQUIRED) String email,
  @NotBlank(message = FIRSTNAME_REQUIRED) String firstName,
  @NotBlank(message = FIRSTNAME_REQUIRED) String lastName,
  @NotBlank(message = ROLE_REQUIRED) String role,
  @NotBlank(message = PASSWORD_REQUIRED) String password
) {
  public UserRegisterRequest {
    Set<String> validRoles = Set
      .of(STAFF, APPROVER_LEVEL_1, APPROVER_LEVEL_2, FINANCE)
      .stream()
      .map(r -> r.name().toLowerCase())
      .collect(Collectors.toSet());

    if (!validRoles.contains(role)) {
      throw new InvalidInputException(INVALID_ROLE_MESSAGE.formatted(role.toLowerCase(), validRoles));
    }

    firstName = firstName.strip();
    lastName = lastName.strip();
    email = email.strip();
    role = role.strip();
    role = role.toUpperCase();
  }
}
