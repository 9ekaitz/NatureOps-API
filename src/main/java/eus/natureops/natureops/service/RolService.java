package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Role;

public interface RolService {

  public Role save(Role role);

  public Role findByName(String name);

  public List<Role> findAll();

  public Role findById(Long roleId);
}
