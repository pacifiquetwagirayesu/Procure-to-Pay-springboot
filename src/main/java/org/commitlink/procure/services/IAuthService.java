package org.commitlink.procure.services;

import jakarta.validation.Valid;
import org.commitlink.procure.dto.user.UserLoginEntityResponse;
import org.commitlink.procure.dto.user.UserLoginRequest;

public interface IAuthService {
  UserLoginEntityResponse userLogin(@Valid UserLoginRequest userLoginRequest);

  UserLoginEntityResponse userRefreshToken(String refreshToken);
}
