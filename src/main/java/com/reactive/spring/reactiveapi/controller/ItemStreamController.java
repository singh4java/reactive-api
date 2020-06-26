package com.reactive.spring.reactiveapi.controller;

import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1_STREAM;

import com.reactive.spring.reactiveapi.document.ItemCapped;
import com.reactive.spring.reactiveapi.repository.ItemReactiveCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ItemStreamController {

  @Autowired
  ItemReactiveCappedRepository cappedRepository;


  @GetMapping(value = ITEM_END_POINT_V1_STREAM,produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<ItemCapped> getAllItemsStream() {
    return cappedRepository.findItemsBy();
  }
}
