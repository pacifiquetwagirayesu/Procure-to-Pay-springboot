package org.commitlink.procure.config;

import static org.commitlink.procure.utils.Constants.API_KEY;
import static org.commitlink.procure.utils.Constants.CLOUD_NAME;
import static org.commitlink.procure.utils.Constants.SECRET_KEY;
import static org.commitlink.procure.utils.Constants.SECURE;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

  @Value("${cloudinary.api-key}")
  private String cloudinaryApiKey;

  @Value("${cloudinary.cloud-name}")
  private String cloudinaryName;

  @Value("${cloudinary.api-secret}")
  private String cloudinarySecret;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(
      ObjectUtils.asMap(CLOUD_NAME, cloudinaryName, API_KEY, cloudinaryApiKey, SECRET_KEY, cloudinarySecret, SECURE, true)
    );
  }

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder) {
    return builder.build();
  }

  @Bean
  public ExecutorService executorService() {
    return new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(2));
  }
}
