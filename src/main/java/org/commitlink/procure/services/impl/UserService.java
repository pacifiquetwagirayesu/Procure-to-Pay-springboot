package org.commitlink.procure.services.impl;

import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND_MESSAGE;
import static org.commitlink.procure.utils.UserMapper.mapUser;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.dto.UserListPagination;
import org.commitlink.procure.dto.UserRegisterRequest;
import org.commitlink.procure.exceptions.UserNotFoundException;
import org.commitlink.procure.models.Role;
import org.commitlink.procure.models.User;
import org.commitlink.procure.repository.IUserRepository;
import org.commitlink.procure.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements IUserService {

  private final IUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public long userRegister(UserRegisterRequest ur) {
    Role role = Role.valueOf(ur.role().toUpperCase());

    User user = User
      .builder()
      .firstName(ur.firstName())
      .lastName(ur.lastName())
      .email(ur.email())
      .role(role.name())
      .password(passwordEncoder.encode(ur.password()))
      .permissions(role.getPermissions())
      .createdAt(LocalDateTime.now())
      .build();

    User saved = userRepository.save(user);

    return saved.getId();
  }

  @Override
  @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
  public UserListPagination getUserList(int page, int size) {
    int pageNumber = Math.max(0, (page - 1));
    PageRequest pageRequest = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "firstName"));
    Page<User> pageContent = userRepository.findAll(pageRequest);
    Iterable<UserEntityResponse> userList = pageContent.getContent().stream().map(user -> mapUser.apply(user)).toList();

    return new UserListPagination(
      pageContent.getTotalElements(),
      pageContent.getTotalPages(),
      pageContent.hasNext(),
      pageContent.hasPrevious(),
      userList
    );
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUserById(long id) {
    userRepository.deleteById(id);
  }

  @Override
  @PreAuthorize("#id == authentication.principal.id")
  public UserEntityResponse getUserById(long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(id)));
    return mapUser.apply(user);
  }
}
