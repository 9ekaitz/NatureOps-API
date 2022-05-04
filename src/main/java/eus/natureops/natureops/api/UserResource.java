package eus.natureops.natureops.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.natureops.natureops.domain.User;

@RestController
@RequestMapping("/api")
public class UserResource {
  
  public String get(int id) {
    return "";
  }

  public String getAll() {
    return "";
  }

  public String auth(String username, String password) {
    return "";
  }

  public String save(User user) {
    return "";
  }
}
