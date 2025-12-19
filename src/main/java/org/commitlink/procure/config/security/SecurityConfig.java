package org.commitlink.procure.config.security;

import static org.commitlink.procure.utils.Constants.AUTH_URL;
import static org.commitlink.procure.utils.Constants.USER_REGISTER_URL;
import static org.commitlink.procure.utils.Constants.WHITE_LIST_URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> {
      authorize.requestMatchers(WHITE_LIST_URL).permitAll();
      authorize.requestMatchers(USER_REGISTER_URL).permitAll();
      authorize.requestMatchers(AUTH_URL).permitAll();
    });

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
