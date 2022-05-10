package eus.natureops.natureops.service;

import java.util.List;

import eus.natureops.natureops.domain.Event;

public interface EventService {
  public Event save(Event event);

  public List<Event> findAll();

  public Event findById(Long id);
}
