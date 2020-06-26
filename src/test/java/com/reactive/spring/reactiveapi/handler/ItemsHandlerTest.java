package com.reactive.spring.reactiveapi.handler;

import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1;
import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1_FUNCTIONAL;
import static org.junit.jupiter.api.Assertions.*;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemsHandlerTest {


  @Autowired
  WebTestClient client;

  @Autowired
  ItemReactiveRepository repository;

  List<Item> itemList = Arrays.asList(
      new Item(null, "Apple Tv", 799.7),
      new Item(null, "Apple MAC pro", 1700.7),
      new Item(null, "Apple iPad", 599.17),
      new Item(null, "Apple Watch", 399.7),
      new Item(null, "Apple AirLight", 5699.7),
      new Item("ABC", "Apple iSound", 8.7)

  );

  @Before
  public void setUp() {
    repository.deleteAll()
        .thenMany(Flux.fromIterable(itemList))
        .flatMap(repository::save)
        .doOnNext(Item::print)
        .blockLast();
  }

  @org.junit.Test
  public  void getAllItems() {
    client
        .get()
        .uri(ITEM_END_POINT_V1_FUNCTIONAL)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Item.class);


  }

}