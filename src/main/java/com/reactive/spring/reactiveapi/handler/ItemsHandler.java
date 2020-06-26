package com.reactive.spring.reactiveapi.handler;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

  @Autowired
  ItemReactiveRepository repository;

  public Mono<ServerResponse> getAllItems(ServerRequest request){
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(repository.findAll(), Item.class);
  }
}
