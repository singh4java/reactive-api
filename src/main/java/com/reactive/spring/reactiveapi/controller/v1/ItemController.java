package com.reactive.spring.reactiveapi.controller.v1;

import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1;
import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1_STREAM;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.document.ItemCapped;
import com.reactive.spring.reactiveapi.repository.ItemReactiveCappedRepository;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {


  @Autowired
  ItemReactiveRepository repository;
  @Autowired
  ItemReactiveCappedRepository cappedRepository;


  @GetMapping(ITEM_END_POINT_V1)
  public Flux<Item> getAllItems() {
    return repository.findAll();
  }

  @GetMapping(ITEM_END_POINT_V1 + "/{id}")
  public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {
    return repository.findById(id)
        .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

  }

  @PostMapping(ITEM_END_POINT_V1)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Item> createItem(@RequestBody Item item) {
    return repository.save(item);
  }

  @DeleteMapping(ITEM_END_POINT_V1 + "/{id}")
  public Mono<Void> createItem(@PathVariable String id) {
    return repository.deleteById(id);
  }

  @PutMapping(ITEM_END_POINT_V1 + "/{id}")
  public Mono<ResponseEntity<Item>> updatedIdem(@PathVariable String id,
      @RequestBody Item item) {
    return repository.findById(id)
        .flatMap(fatchItem -> {
          fatchItem.setPrice(item.getPrice());
          fatchItem.setDescription(item.getDescription());
          return repository.save(fatchItem);
        })
        .map(item1 -> new ResponseEntity<>(item1, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping(ITEM_END_POINT_V1 + "/runtimeException")
  public Flux<Item> runtimeException() {

    return repository.findAll()
        .concatWith(Mono.error(new RuntimeException("runtimeException from ItemController")));
  }

}
