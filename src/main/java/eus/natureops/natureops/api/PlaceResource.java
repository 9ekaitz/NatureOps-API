package eus.natureops.natureops.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.Place;
import eus.natureops.natureops.service.PlaceService;

@RestController
@RequestMapping("/api/places")
public class PlaceResource {
  
  @Autowired
  private PlaceService placeService;

  @GetMapping
  public ResponseEntity<List<Place>> getPlaces() {
    return ResponseEntity.ok(placeService.getAll());
  }
}
