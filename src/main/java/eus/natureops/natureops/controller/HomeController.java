package eus.natureops.natureops.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  @RequestMapping(value = { "/", "/login", "/logout", "/registro", "/dashboard/**" })
  public String index() {
    return "index.html";
  }
}
