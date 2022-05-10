package eus.natureops.natureops.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.News;

@RestController
@RequestMapping("/api")
public class NewsResource {

  @GetMapping("/event")
  public String getAll() {
    return "";
  }

  public String save(News news) {
    return "";
  }
}
