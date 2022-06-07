package eus.natureops.natureops.service;

import eus.natureops.natureops.domain.User;
import eus.natureops.natureops.dto.UserView;
import eus.natureops.natureops.form.UserRegistrationForm;

public interface UserService {
  public User register(UserRegistrationForm form);
  public User save(User user);
  public User findByUsername(String username);
  public UserView loadView(String username);
  public User disable(User user);
  public User setRole(String username, String name);
}
