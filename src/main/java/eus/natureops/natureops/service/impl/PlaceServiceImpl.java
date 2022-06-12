package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Place;
import eus.natureops.natureops.repository.PlaceRepository;
import eus.natureops.natureops.service.PlaceService;

@Service
public class PlaceServiceImpl implements PlaceService{

  @Autowired
  private PlaceRepository placeRepository;

  @Override
  public Place save(Place place) {
    return null;
  }

  @Override
  public List<Place> getAll() {
    return placeRepository.findAll();
  }

  @Override
  public Place findById(Long id) {
    return placeRepository.findById(id).orElse(null);
  }
  
}
