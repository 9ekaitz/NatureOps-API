package eus.natureops.natureops.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.Event;
import eus.natureops.natureops.dto.EventSimpleView;
import eus.natureops.natureops.service.EventService;

@RestController
@RequestMapping("/api")
public class EventResource {

  @Autowired
  private EventService eventService;

  @GetMapping("/getSimple/{id}")
  public ResponseEntity<EventSimpleView> getSimple(@PathVariable long id) {
    return ResponseEntity.ok().body(eventService.getEventSimple(id));
  }

  public String getAll() {
    return "";
  }

  public String save(Event event) {
    return "";
  }
}
