package org.commitlink.procure.config.security;

import static org.commitlink.procure.utils.Constants.AUTH_URL;
import static org.commitlink.procure.utils.Constants.REQUEST_PURCHASE_URL;
import static org.commitlink.procure.utils.Constants.USER_URLS;
import static org.commitlink.procure.utils.Constants.WHITE_LIST_URL;
import static org.commitlink.procure.utils.HttpUtils.authenticationErrorMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter authenticationFilter;
  private final AdminAuthenticationFilter adminAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> {
      authorize.requestMatchers(WHITE_LIST_URL).permitAll();
      authorize.requestMatchers(HttpMethod.POST, USER_URLS).permitAll();
      authorize.requestMatchers(HttpMethod.GET, USER_URLS).authenticated();
      authorize.requestMatchers(HttpMethod.DELETE, USER_URLS).authenticated();
      authorize.requestMatchers(REQUEST_PURCHASE_URL).authenticated();
      authorize.requestMatchers(AUTH_URL).permitAll();
      authorize.anyRequest().denyAll();
    });

    http.csrf(AbstractHttpConfigurer::disable);
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterAt(adminAuthenticationFilter, JwtAuthenticationFilter.class);
    http.exceptionHandling(ex ->
      ex.authenticationEntryPoint(
        (
          (request, response, authException) ->
            authenticationErrorMessage(request, response, HttpStatus.UNAUTHORIZED.value(), authException, new ObjectMapper())
        )
      )
    );

    return http.build();
  }
}
