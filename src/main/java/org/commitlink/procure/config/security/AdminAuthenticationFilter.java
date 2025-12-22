package org.commitlink.procure.config.security;

import static org.commitlink.procure.utils.Constants.ADMIN_EMAIL;
import static org.commitlink.procure.utils.Constants.PASSWORD_ADMIN;
import static org.commitlink.procure.utils.Constants.PREFIX;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.Role;
import org.commitlink.procure.models.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationFilter extends OncePerRequestFilter {

  private final PasswordEncoder passwordEncoder;

  @Value("${security.jwt.admin}")
  private String adminAuthKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.substring(7).equals(adminAuthKey)) {
      InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
      var admin = AuthUser.getUser(
        User
          .builder()
          .id(0L)
          .email(ADMIN_EMAIL)
          .role(Role.ADMIN.name())
          .password(passwordEncoder.encode(PASSWORD_ADMIN))
          .permissions(Role.ADMIN.getPermissions())
          .build()
      );
      manager.createUser(admin);

      var auth = new UsernamePasswordAuthenticationToken(admin, null, Set.of(new SimpleGrantedAuthority(PREFIX + Role.ADMIN.name())));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    return header != null && !header.contains(adminAuthKey);
  }
}
