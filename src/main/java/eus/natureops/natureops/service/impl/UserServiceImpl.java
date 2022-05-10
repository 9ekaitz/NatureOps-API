package eus.natureops.natureops.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.repository.RoleRepository;
import eus.natureops.natureops.repository.UserRepository;
import eus.natureops.natureops.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public User register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User disable(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User setRole(String username, String name) {
    Role role = roleRepository.findByName(name);
    User user = userRepository.findByUsername(username);

    user.setRole(role);

    return save(user);
  }

}
