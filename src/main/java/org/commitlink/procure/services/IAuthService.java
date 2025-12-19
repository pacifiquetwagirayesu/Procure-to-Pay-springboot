package org.commitlink.procure.services;

import jakarta.validation.Valid;
import org.commitlink.procure.dto.UserLoginEntityResponse;
import org.commitlink.procure.dto.UserLoginRequest;

public interface IAuthService {
  UserLoginEntityResponse userLogin(@Valid UserLoginRequest userLoginRequest);

  UserLoginEntityResponse userRefreshToken(String refreshToken);
}
