package eus.natureops.natureops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  public <T> T findByUsername(String username, Class<T> type);
  public List<User> findAll();
  public User findByUsername(String username);

}
