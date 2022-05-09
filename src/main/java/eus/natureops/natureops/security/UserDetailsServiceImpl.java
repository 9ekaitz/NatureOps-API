package eus.natureops.natureops.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final User user = userRepository.findByUsername(username);

    if (user == null) throw new UsernameNotFoundException(username);

    boolean disabled = !user.isEnabled();

    UserDetails userDetails = org.springframework.security.core.userdetails.User
    .withUsername(username)
    .password(user.getPassword())
    .disabled(disabled)
    .authorities(user.getRole().getName())
    .build();

    return userDetails;
  }
}
