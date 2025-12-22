package org.commitlink.procure.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class AuthUser implements UserDetails {

  private long id;
  private String username;
  private String firstName;
  private String lastName;
  private String password;
  private String role;
  private Set<String> permissions;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
  }

  public static AuthUser getUser(User user) {
    return new AuthUser(
      user.getId(),
      user.getEmail(),
      user.getFirstName(),
      user.getLastName(),
      user.getPassword(),
      user.getRole(),
      user.getPermissions(),
      user.getCreatedAt(),
      user.getUpdatedAt()
    );
  }

  @Override
  public String toString() {
    return (
      "AuthUser{" +
      "id=" +
      id +
      ", username='" +
      username +
      '\'' +
      ", firstName='" +
      firstName +
      '\'' +
      ", lastName='" +
      lastName +
      '\'' +
      ", password='" +
      password +
      '\'' +
      ", role='" +
      role +
      '\'' +
      ", permissions=" +
      permissions +
      ", createdAt=" +
      createdAt +
      ", updatedAt=" +
      updatedAt +
      '}'
    );
  }
}
