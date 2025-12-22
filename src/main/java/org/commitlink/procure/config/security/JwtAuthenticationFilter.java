package org.commitlink.procure.config.security;

import static org.commitlink.procure.utils.Constants.BEARER_KEY;
import static org.commitlink.procure.utils.Constants.EXEMPT_FOR_AUTH_FILTER;
import static org.commitlink.procure.utils.HttpUtil.authenticationErrorMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.commitlink.procure.models.AuthUser;
import org.commitlink.procure.services.IJwtService;
import org.commitlink.procure.services.impl.UserDetailServiceImpl;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final IJwtService jwtService;
  private final UserDetailServiceImpl userDetailService;

  @Value("${security.jwt.admin}")
  private String adminAuthKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
    throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null && header.contains(BEARER_KEY) && !header.contains(adminAuthKey)) {
      String jwt = header.substring(7);
      try {
        jwtService.isTokenValid(jwt);
      } catch (JwtException ex) {
        authenticationErrorMessage(request, response, HttpStatus.BAD_REQUEST, ex, new ObjectMapper());
        return;
      }

      String username = jwtService.getUsername(jwt);
      AuthUser authUser = userDetailService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        authUser,
        null,
        authUser.getAuthorities()
      );

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    return (EXEMPT_FOR_AUTH_FILTER.contains(request.getServletPath()) || (header != null && header.contains(adminAuthKey)));
  }
}
