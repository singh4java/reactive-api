package com.reactive.spring.reactiveapi.repository;

import com.reactive.spring.reactiveapi.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemReactiveCappedRepository extends ReactiveMongoRepository<ItemCapped, String> {


  @Tailable
  Flux<ItemCapped> findItemsBy();
}
