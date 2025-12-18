package org.commitlink.procure.dto;

import jakarta.validation.constraints.Email;
import org.commitlink.procure.exceptions.InvalidInputException;

import java.util.Set;
import java.util.stream.Collectors;

import static org.commitlink.procure.models.Role.APPROVER_LEVEL_1;
import static org.commitlink.procure.models.Role.APPROVER_LEVEL_2;
import static org.commitlink.procure.models.Role.FINANCE;
import static org.commitlink.procure.models.Role.STAFF;
import static org.commitlink.procure.utils.Constants.INVALID_ROLE_MESSAGE;

public record UserRegisterRequest(
        @Email(message = "user email is required")
        String email,
        String firstName,
        String lastName,
        String role,
        String password
) {

    public UserRegisterRequest {
        if (firstName == null || firstName.isBlank()) throw new InvalidInputException("First name is required");
        if (email == null || email.isBlank()) throw new InvalidInputException("Email is required");
        if (password == null || password.isBlank()) throw new InvalidInputException("Password is required");
        if (role == null || role.isBlank()) throw new InvalidInputException("Role is required");

        Set<String> validRoles = Set.of(STAFF, APPROVER_LEVEL_1, APPROVER_LEVEL_2, FINANCE)
                .stream().map(r -> r.name().toLowerCase()).collect(Collectors.toSet());

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
