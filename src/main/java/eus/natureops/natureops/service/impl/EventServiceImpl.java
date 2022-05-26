package eus.natureops.natureops.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eus.natureops.natureops.domain.Event;
import eus.natureops.natureops.dto.EventSimpleView;
import eus.natureops.natureops.repository.EventRepository;
import eus.natureops.natureops.service.EventService;

@Service
public class EventServiceImpl implements EventService{

  @Autowired
  private EventRepository eventRepository;

  @Override
  public Event save(Event event) {
    return eventRepository.save(event);
  }

  @Override
  public List<Event> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Event findById(Long id) {
    return eventRepository.findById(id, Event.class);
  }

  @Override
  public EventSimpleView getEventSimple(Long id) {
    return eventRepository.findById(id, EventSimpleView.class);
  }
  
}
