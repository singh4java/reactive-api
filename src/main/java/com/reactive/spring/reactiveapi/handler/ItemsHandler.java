package com.reactive.spring.reactiveapi.handler;


import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

  @Autowired
  ItemReactiveRepository repository;

  public Mono<ServerResponse> getAllItems(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(repository.findAll(), Item.class);
  }

  public Mono<ServerResponse> getOneItems(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<Item> itemMono = repository.findById(id);

    return itemMono.flatMap(item ->
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromObject(item)))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> createItem(ServerRequest request) {

    Mono<Item> mono = request.bodyToMono(Item.class);

    return mono.flatMap(item ->
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(repository.save(item), Item.class));
  }

  public Mono<ServerResponse> deleteItem(ServerRequest request) {

    String id = request.pathVariable("id");
    Mono<Void> itemMono = repository.deleteById(id);

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(itemMono, Void.class);
  }

  public Mono<ServerResponse> updateItem(ServerRequest request) {

    String id = request.pathVariable("id");
    Mono<Item> itemMono1 = request.bodyToMono(Item.class)
        .flatMap(item -> {
          Mono<Item> itemMono = repository.findById(id)
              .flatMap(item1 -> {
                item1.setDescription(item.getDescription());
                item1.setPrice(item.getPrice());
                return repository.save(item1);
              });
          return itemMono;
        });
    return itemMono1.flatMap(item ->
        ServerResponse.ok()
    .contentType(MediaType.APPLICATION_JSON)
    .body(fromObject(item)))
        .switchIfEmpty(null);
  }


}
