package com.reactive.spring.reactiveapi.controller.v1;

import static com.reactive.spring.reactiveapi.constants.ItemConstants.ITEM_END_POINT_V1;
import static org.junit.Assert.assertTrue;

import com.reactive.spring.reactiveapi.constants.ItemConstants;
import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

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

  @Test
  public  void getAllItems() {
    client
        .get()
        .uri(ITEM_END_POINT_V1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Item.class);


  }

  @Test
  public  void getAllItems_2() {
    client
        .get()
        .uri(ITEM_END_POINT_V1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Item.class)
        .consumeWith(response -> {
          List<Item> items = response.getResponseBody();
          items.forEach(item -> {
            assertTrue(item.getId()!=null);
          });
        });

  }

  @Test
  public  void getAllItems_3() {
   client
        .get()
        .uri(ITEM_END_POINT_V1)
        .exchange()
        .expectStatus()
        .isOk();


  }

  @Test
  public void getAllItems_4() {
    client.get().uri(ITEM_END_POINT_V1.concat("/{id}"),"ABC")
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  public  void createItems_4() {
    Item item = new Item(null,"Apple iPhone x",987.09);
    client.post().uri(ITEM_END_POINT_V1)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.justOrEmpty(item),Item.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.description").isEqualTo("Apple iPhone x");
  }
}
