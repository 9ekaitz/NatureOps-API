package eus.natureops.natureops.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import eus.natureops.natureops.filter.AuthenticationFilter;
import eus.natureops.natureops.filter.AuthorizationFilter;
import eus.natureops.natureops.utils.FingerprintHelper;
import eus.natureops.natureops.utils.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private FingerprintHelper fingerprintHelper;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(this.authenticationManagerBean(), jwtUtil,
        fingerprintHelper);
    authenticationFilter.setFilterProcessesUrl("/api/login");
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests().antMatchers("/api/token/refresh").permitAll();
    http.authorizeRequests().antMatchers("/api/**").authenticated();
    http.authorizeRequests().antMatchers("/api/get/**").hasAnyAuthority("ROLE_USER");
    http.authorizeRequests().anyRequest().permitAll();
    http.addFilter(authenticationFilter);
    http.addFilterBefore(new AuthorizationFilter(jwtUtil, fingerprintHelper),
        UsernamePasswordAuthenticationFilter.class);
  }
}
