package eus.natureops.natureops.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  // public User findByUsername(String username);
  public <T> Set<T> findAll(Class<T> type);
  public <T> T findById(Long id, Class<T> type);
}