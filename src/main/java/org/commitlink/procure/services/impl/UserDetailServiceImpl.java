package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.commitlink.procure.exceptions.UserNotFoundException;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.models.User;
import org.commitlink.procure.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailServiceImpl implements UserDetailsService {

  private final IUserRepository userRepository;

  @Override
  public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    return AuthUser.getUser(user);
  }
}
