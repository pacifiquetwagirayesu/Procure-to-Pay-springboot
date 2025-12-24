package org.commitlink.procure.services;

import org.commitlink.procure.dto.user.UserEntityResponse;
import org.commitlink.procure.dto.user.UserListPaginationResponse;
import org.commitlink.procure.dto.user.UserRegisterRequest;

public interface IUserService {
  long userRegister(UserRegisterRequest userRegisterRequest);

  UserEntityResponse getUserById(long id);

  UserListPaginationResponse getUserList(int page, int size);

  void deleteUserById(long id);
}
