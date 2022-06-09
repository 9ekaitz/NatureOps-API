package eus.natureops.natureops.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.dto.AchievementView;
import eus.natureops.natureops.service.AchivementService;

@RestController
@RequestMapping("/api")
public class AchivementResource {
  
  @Autowired
  private AchivementService achivementService;

  @GetMapping("/achivements/{page}/{size}")
  public  ResponseEntity<List<AchievementView>> getAll(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size, Authentication auth) {
    return ResponseEntity.ok().body(achivementService.getPage(page, size, auth.getName()));
  }

  @GetMapping("/achivements/size")
  public  ResponseEntity<Integer> size() {
    return ResponseEntity.ok().body(achivementService.achievementsSize());
  }
}
