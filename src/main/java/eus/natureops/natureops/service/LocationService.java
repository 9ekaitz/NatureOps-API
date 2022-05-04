package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Location;

public interface LocationService {
  public Location save(Location location);

  public List<Location> findAll();

  public Location findById(Long id);
}
