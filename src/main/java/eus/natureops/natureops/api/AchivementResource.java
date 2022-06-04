package eus.natureops.natureops.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import eus.natureops.natureops.domain.Achivement;
import eus.natureops.natureops.service.AchivementService;

@RestControllerAdvice
@RequestMapping("/api")
public class AchivementResource {
  

  @Autowired
  private AchivementService achivementService;

  public String get(int id) {
    return "";
  }
  
  @GetMapping("/achivements")
  public  ResponseEntity<List<Achivement>> getAll() {
    return ResponseEntity.ok().body(achivementService.findAll());
  }

  public String save(Achivement achivement) {
    return "";
  }
}
