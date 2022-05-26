package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Event;
import eus.natureops.natureops.dto.EventSimpleView;

public interface EventService {
  public Event save(Event event);
  public List<Event> findAll();
  public Event findById(Long id);
  public EventSimpleView getEventSimple(Long id);
}
