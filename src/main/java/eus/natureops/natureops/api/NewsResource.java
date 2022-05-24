package eus.natureops.natureops.api;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.News;

@RestController
@RequestMapping("/api")
public class NewsResource {


  @Autowired
  private RabbitTemplate rabbitTemplate;

  @GetMapping("/event")
  public ResponseEntity<?> getAll() {
    rabbitTemplate.convertAndSend("amq.topic", "rest.score", "Hello");
    return ResponseEntity.accepted().build();
  }

  public String save(News news) {
    return "";
  }
}
