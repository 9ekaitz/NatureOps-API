package eus.natureops.natureops.service;

import eus.natureops.natureops.domain.Role;

public interface RoleService {

  public Role save(Role role);

  public Role findByName(String name);

  public Role findById(Long roleId);
}
