package eus.natureops.natureops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eus.natureops.natureops.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  public Role findByName(String name);
}
