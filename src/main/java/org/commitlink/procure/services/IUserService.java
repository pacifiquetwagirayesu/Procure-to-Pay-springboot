package org.commitlink.procure.services;

import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.dto.UserRegisterRequest;

public interface IUserService {
    long userRegister(UserRegisterRequest userRegisterRequest);

    UserEntityResponse getUserById(long id);
}
