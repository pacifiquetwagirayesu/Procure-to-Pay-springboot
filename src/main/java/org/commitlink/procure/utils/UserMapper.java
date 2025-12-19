package org.commitlink.procure.utils;

import java.util.function.Function;
import org.commitlink.procure.dto.AuthUserResponseEntity;
import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.models.User;

public class UserMapper {

  public static Function<User, UserEntityResponse> mapUser =
      user ->
          new UserEntityResponse(
              user.getId(),
              user.getEmail(),
              user.getFirstName(),
              user.getLastName(),
              user.getRole(),
              user.getPermissions(),
              user.getPassword(),
              user.getCreatedAt(),
              user.getUpdatedAt());

  public static Function<AuthUser, AuthUserResponseEntity> authUserMap =
      authUser ->
          new AuthUserResponseEntity(
              authUser.getId(),
              authUser.getUsername(),
              authUser.getFirstName(),
              authUser.getLastName(),
              authUser.getRole(),
              authUser.getPermissions(),
              authUser.getPassword(),
              authUser.getCreatedAt(),
              authUser.getUpdatedAt());
}
