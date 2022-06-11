package eus.natureops.natureops.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Role;
import eus.natureops.natureops.repository.RoleRepository;
import eus.natureops.natureops.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  RoleRepository roleRepository;

  @Override
  public Role save(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public Role findByName(String name) {
    return roleRepository.findByName(name);
  }

  @Override
  public Role findById(Long roleId) {
    return roleRepository.findById(roleId).orElse(null);
  }

}
