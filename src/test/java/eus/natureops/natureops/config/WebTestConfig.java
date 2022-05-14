package eus.natureops.natureops.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

@TestConfiguration
public class WebTestConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetailsService userDetailsService = new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) {
        if (username == "eka") {
          return org.springframework.security.core.userdetails.User
              .withUsername(username)
              .password(BCrypt.hashpw("123", BCrypt.gensalt()))
              .disabled(false)
              .authorities("ROLE_USER")
              .build();
        } else
          throw new UsernameNotFoundException(username);
      }
    };
    return userDetailsService;
  }
}