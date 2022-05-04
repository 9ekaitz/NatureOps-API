package eus.natureops.natureops.repository;

import java.util.List;

import eus.natureops.natureops.domain.User;

public interface UserRepository {
  public User findByUsername(String username);
  public List<User> findAll();
}
