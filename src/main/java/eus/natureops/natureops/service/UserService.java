package eus.natureops.natureops.service;

import eus.natureops.natureops.domain.User;

public interface UserService {
  public User register(User user);
  public User save(User user);
  public User findByUsername(String username);
  public User disable(User user);
  public User setRole(String username, String name);
}
