package org.commitlink.procure.config.security;

import static org.commitlink.procure.utils.Constants.AUTH_URL;
import static org.commitlink.procure.utils.Constants.USER_REGISTER_URL;
import static org.commitlink.procure.utils.Constants.WHITE_LIST_URL;
import static org.commitlink.procure.utils.HttpUtil.authenticationErrorMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter authenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> {
      authorize.requestMatchers(WHITE_LIST_URL).permitAll();
      authorize.requestMatchers(USER_REGISTER_URL).permitAll();
      authorize.requestMatchers(AUTH_URL).permitAll();
      authorize.anyRequest().authenticated();
    });

    http.csrf(AbstractHttpConfigurer::disable);
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling(ex ->
      ex.authenticationEntryPoint(
        (
          (request, response, authException) ->
            authenticationErrorMessage(request, response, HttpStatus.UNAUTHORIZED, authException, new ObjectMapper())
        )
      )
    );

    return http.build();
  }
}
