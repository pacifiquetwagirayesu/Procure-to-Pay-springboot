package org.commitlink.procure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.dto.UserLoginEntityResponse;
import org.commitlink.procure.dto.UserLoginRequest;
import org.commitlink.procure.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/auth")
@RestController
@Tag(name = "Authentication Endpoints")
@RequiredArgsConstructor
public class AuthController {

  private final IAuthService authService;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public UserLoginEntityResponse userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    return authService.userLogin(userLoginRequest);
  }

  @PostMapping("/refresh-token")
  @ResponseStatus(HttpStatus.CREATED)
  public UserLoginEntityResponse userRefreshToken(@Valid @RequestParam("refresh-token") String refreshToken) {
    return authService.userRefreshToken(refreshToken);
  }
}
