package dev.araldicamila.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firstRoute")

public class MinhaPrimeiraController {

  @GetMapping("/")
  public String returnFirstMessage() {
    return "Hello World!";
  }
}
