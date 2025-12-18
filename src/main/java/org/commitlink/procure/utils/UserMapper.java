package org.commitlink.procure.utils;

import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.models.User;

import java.util.function.Function;

public class UserMapper {

    public static Function<User, UserEntityResponse> mapUser = user -> new UserEntityResponse(
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
}
