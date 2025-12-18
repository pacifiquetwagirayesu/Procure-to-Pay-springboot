package org.commitlink.procure.services;

import lombok.AllArgsConstructor;
import org.commitlink.procure.dto.UserEntityResponse;
import org.commitlink.procure.dto.UserRegisterRequest;
import org.commitlink.procure.exceptions.UserNotFoundException;
import org.commitlink.procure.models.Role;
import org.commitlink.procure.models.User;
import org.commitlink.procure.repository.IUserRepository;
import org.commitlink.procure.utils.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.commitlink.procure.utils.Constants.USER_NOT_FOUND_MESSAGE;
import static org.commitlink.procure.utils.UserMapper.mapUser;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public long userRegister(UserRegisterRequest ur) {
        Role role = Role.valueOf(ur.role().toUpperCase());

        User user = User.builder().firstName(ur.firstName())
                .lastName(ur.lastName())
                .email(ur.email())
                .role(role.name())
                .password(passwordEncoder.encode(ur.password()))
                .permissions(role.getPermissions())
                .createdAt(LocalDateTime.now()).build();

        User saved = userRepository.save(user);

        return saved.getId();
    }

    @Override
    public UserEntityResponse getUserById(long id) {
       User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(id)));
        return mapUser.apply(user);
    }
}
