package eus.natureops.natureops.api;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.service.NewsService;

@RestController
@RequestMapping("/api/news")
public class NewsResource {

  @Autowired
  private NewsService newsService;

  @GetMapping("/{page}/{size}" )
  public ResponseEntity<List<News>> getAll(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size ) {

    return  ResponseEntity.ok().body(newsService.findAll(page,size));
  }


  @GetMapping("/size" )
  public ResponseEntity<Integer> getSize() {
    return  ResponseEntity.ok().body(newsService.getNewsSize());
  }

}
