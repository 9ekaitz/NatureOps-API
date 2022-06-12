package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Place;

public interface PlaceService {
  public Place save(Place place);

  public List<Place> getAll();

  public Place findById(Long id);
}
