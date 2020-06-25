package com.reactive.spring.reactiveapi.controller;

import com.reactive.spring.reactiveapi.model.User;
import com.reactive.spring.reactiveapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RectiveController {

  private final UserRepository repository;

  @Autowired
  public RectiveController(UserRepository repository) {
    this.repository = repository;
  }

  @PostMapping(value = "/saveuser")
  public Mono<User> saveUser(){
    User user = new User("AN001", "Manvendra");
    return repository.save(user);
  }
}
