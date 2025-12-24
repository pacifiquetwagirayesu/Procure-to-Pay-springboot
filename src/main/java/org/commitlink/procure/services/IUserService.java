package org.commitlink.procure.services;

import org.commitlink.procure.dto.user.UserEntityResponse;
import org.commitlink.procure.dto.user.UserListPagination;
import org.commitlink.procure.dto.user.UserRegisterRequest;

public interface IUserService {
  long userRegister(UserRegisterRequest userRegisterRequest);

  UserEntityResponse getUserById(long id);

  UserListPagination getUserList(int page, int size);

  void deleteUserById(long id);
}
