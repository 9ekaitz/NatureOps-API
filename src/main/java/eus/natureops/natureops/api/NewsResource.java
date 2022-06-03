package eus.natureops.natureops.api;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.News;
import eus.natureops.natureops.service.NewsService;

@RestController
@RequestMapping("/api")
public class NewsResource {

  @Autowired
  private NewsService newsService;

  @GetMapping("/news/{page}/{size}" )
  public ResponseEntity<List<News>> getAll(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size ) {

    return  ResponseEntity.ok().body(newsService.findAll(page,size));
  }


  @GetMapping("/news/size" )
  public ResponseEntity<Integer> getSize() {

    return  ResponseEntity.ok().body(newsService.getNewsSize());
  }


  public String save(News news) {
    return "";
  }
}
