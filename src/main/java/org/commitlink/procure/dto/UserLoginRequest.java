package org.commitlink.procure.dto;

import static org.commitlink.procure.utils.Constants.INVALID_EMAIL;
import static org.commitlink.procure.utils.Constants.PASSWORD_REQUIRED;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@Email(message = INVALID_EMAIL) String email, @NotBlank(message = PASSWORD_REQUIRED) String password) {}
