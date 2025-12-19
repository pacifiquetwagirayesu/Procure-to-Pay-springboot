package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.ACCESS_TOKEN;
import static org.commitlink.procure.utils.Constants.REFRESH_TOKEN;

import java.util.Map;
import java.util.function.Function;
import org.commitlink.procure.dto.AuthUserResponseEntity;
import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.dto.UserLoginEntityResponse;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.models.Token;
import org.commitlink.procure.models.User;

public class UserMapper {

  public static Function<User, UserEntityResponse> mapUser = user ->
    new UserEntityResponse(
      user.getId(),
      user.getEmail(),
      user.getFirstName(),
      user.getLastName(),
      user.getRole(),
      user.getPermissions(),
      user.getPassword(),
      user.getCreatedAt(),
      user.getUpdatedAt()
    );

  public static Function<AuthUser, AuthUserResponseEntity> authUserMapper = authUser ->
    new AuthUserResponseEntity(
      authUser.getId(),
      authUser.getUsername(),
      authUser.getFirstName(),
      authUser.getLastName(),
      authUser.getRole(),
      authUser.getPermissions(),
      authUser.getPassword(),
      authUser.getCreatedAt(),
      authUser.getUpdatedAt()
    );

  public static UserLoginEntityResponse getUserLoginEntityResponse(AuthUser authUser, Token token) {
    return new UserLoginEntityResponse(
      authUserMapper.apply(authUser),
      Map.of(ACCESS_TOKEN, token.getAccessToken(), REFRESH_TOKEN, token.getRefreshToken())
    );
  }
}
