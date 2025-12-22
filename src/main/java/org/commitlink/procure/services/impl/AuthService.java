package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.Constants.INVALID_TOKEN;
import static org.commitlink.procure.utils.Constants.MALFORMED_TOKEN;
import static org.commitlink.procure.utils.UserMapper.getUserLoginEntityResponse;

import io.jsonwebtoken.JwtException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commitlink.procure.dto.UserLoginEntityResponse;
import org.commitlink.procure.dto.UserLoginRequest;
import org.commitlink.procure.exceptions.InvalidToken;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.Token;
import org.commitlink.procure.repository.ITokenRepository;
import org.commitlink.procure.services.IAuthService;
import org.commitlink.procure.services.IJwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

  private final ITokenRepository tokenRepository;
  private final UserDetailServiceImpl userDetailService;
  private final IJwtService jwtService;

  @Override
  public UserLoginEntityResponse userLogin(UserLoginRequest userLoginRequest) {
    AuthUser authUser = userDetailService.loadUserByUsername(userLoginRequest.email());
    Token token = tokenRepository.save(
      Token.builder().accessToken(jwtService.generateToken(authUser)).refreshToken(jwtService.generateRefreshToken(authUser)).build()
    );

    return getUserLoginEntityResponse(authUser, token);
  }

  @Override
  public UserLoginEntityResponse userRefreshToken(String refreshToken) {
    Token token;
    boolean tokenValid;

    try {
      tokenValid = jwtService.isTokenValid(refreshToken);
    } catch (JwtException e) {
      throw new InvalidToken(MALFORMED_TOKEN);
    }

    if (!tokenValid) throw new InvalidToken(INVALID_TOKEN);
    AuthUser authUser = userDetailService.loadUserByUsername(jwtService.getUsername(refreshToken));
    Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);

    if (tokenOptional.isPresent()) {
      token = tokenOptional.get();
      token.setAccessToken(jwtService.generateToken(authUser));
      token = tokenRepository.save(token);
    } else {
      token =
        Token.builder().accessToken(jwtService.generateToken(authUser)).refreshToken(jwtService.generateRefreshToken(authUser)).build();
    }

    return getUserLoginEntityResponse(authUser, token);
  }
}
