package eus.natureops.natureops.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.UserView;
import eus.natureops.natureops.form.UserRegistrationForm;
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
  public User register(UserRegistrationForm form) {
    User user = new User();
    user.setName(form.getName());
    user.setEmail(form.getEmail());
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setRole(roleRepository.findByName("ROLE_USER"));
    user.setEnabled(true);

    return userRepository.save(user);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username, User.class);
  }

  @Override
  public User disable(User user) {
    user.setEnabled(false);
    user.setUsername(user.getId()+"_"+user.getUsername());
    return userRepository.save(user);
  }

  @Override
  public User setRole(String username, String name) {
    Role role = roleRepository.findByName(name);
    User user = userRepository.findByUsername(username, User.class);

    user.setRole(role);

    return save(user);
  }

  @Override
  public UserView loadView(String username) {
    return userRepository.findByUsername(username, UserView.class);
  }

}
