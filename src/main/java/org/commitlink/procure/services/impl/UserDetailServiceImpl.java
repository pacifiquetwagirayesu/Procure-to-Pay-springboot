package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.Constants.INVALID_TOKEN;

import lombok.RequiredArgsConstructor;
import org.commitlink.procure.exceptions.InvalidToken;
import org.commitlink.procure.models.user.AuthUser;
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
    var user = userRepository.findByEmail(username).orElseThrow(() -> new InvalidToken(INVALID_TOKEN));
    return AuthUser.getUser(user);
  }
}
