package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.ACCESS_TOKEN;
import static org.commitlink.procure.utils.Constants.REFRESH_TOKEN;

import java.util.Map;
import java.util.function.Function;
import org.commitlink.procure.dto.purchase.UserPurchaseRequest;
import org.commitlink.procure.dto.user.AuthUserResponseEntity;
import org.commitlink.procure.dto.user.UserEntityResponse;
import org.commitlink.procure.dto.user.UserLoginEntityResponse;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.Token;
import org.commitlink.procure.models.user.User;

public class UserMapper {

  public static Function<User, UserEntityResponse> mapUser = user ->
    new UserEntityResponse(
      user.getId(),
      user.getEmail(),
      user.getFirstName(),
      user.getLastName(),
      user.getRole(),
      user.getPermissions(),
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
      authUser.getCreatedAt(),
      authUser.getUpdatedAt()
    );

  public static UserLoginEntityResponse getUserLoginEntityResponse(AuthUser authUser, Token token) {
    return new UserLoginEntityResponse(
      authUserMapper.apply(authUser),
      Map.of(ACCESS_TOKEN, token.getAccessToken(), REFRESH_TOKEN, token.getRefreshToken())
    );
  }

  public static Function<User, UserPurchaseRequest> userPurchaseRequest = user -> {
    if (user == null) return null;
    return new UserPurchaseRequest(user.getId(), user.getRole(), user.getEmail());
  };
}
