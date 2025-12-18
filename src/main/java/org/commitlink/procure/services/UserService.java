package org.commitlink.procure.services;

import lombok.AllArgsConstructor;
import org.commitlink.procure.dto.UserRegisterRequest;
import org.commitlink.procure.models.Role;
import org.commitlink.procure.models.User;
import org.commitlink.procure.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
