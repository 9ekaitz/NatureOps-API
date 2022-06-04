package eus.natureops.natureops.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  
  @GetMapping("/achivements/{page}/{size}")
  public  ResponseEntity<List<Achivement>> getAll(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
    return ResponseEntity.ok().body(achivementService.findAll(page, size));
  }

  @GetMapping("/achivements/size")
  public  ResponseEntity<Integer> size() {
    return ResponseEntity.ok().body(achivementService.achievementsSize());
  }

  public String save(Achivement achivement) {
    return "";
  }
}
