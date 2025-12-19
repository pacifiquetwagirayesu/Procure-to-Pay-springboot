package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.Constants.ACCESS_TOKEN;
import static org.commitlink.procure.utils.Constants.REFRESH_TOKEN;
import static org.commitlink.procure.utils.UserMapper.authUserMap;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.dto.UserLoginEntityResponse;
import org.commitlink.procure.dto.UserLoginRequest;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.models.Token;
import org.commitlink.procure.repository.ITokenRepository;
import org.commitlink.procure.services.IAuthService;
import org.commitlink.procure.services.IJwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {
  private final ITokenRepository tokenRepository;
  private final UserDetailServiceImpl userDetailService;
  private final IJwtService jwtService;

  @Override
  public UserLoginEntityResponse userLogin(UserLoginRequest userLoginRequest) {
    AuthUser user = (AuthUser) userDetailService.loadUserByUsername(userLoginRequest.email());

    Token token =
        tokenRepository.save(
            Token.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build());

    return new UserLoginEntityResponse(
        authUserMap.apply(user),
        Map.of(ACCESS_TOKEN, token.getAccessToken(), REFRESH_TOKEN, token.getRefreshToken()));
  }
}
