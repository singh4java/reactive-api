package com.reactive.spring.reactiveapi.repository;

import com.reactive.spring.reactiveapi.document.Item;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

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
  public void getAllItems() {
    Flux<Item> itemFlux = repository.findAll();
    StepVerifier.create(itemFlux)
        .expectSubscription()
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  public void getItemByID() {
    StepVerifier.create(repository.findById("ABC"))
        .expectSubscription()
        .expectNextMatches(item -> item.getDescription().equalsIgnoreCase("Apple iSound"))
        .verifyComplete();
  }

  @Test
  public void findItemByDescription() {

    StepVerifier.create(repository.findByDescription("Apple AirLight"))
        .expectSubscription()
        .expectNextMatches(item -> item.getDescription().equalsIgnoreCase("Apple AirLight"))
        .verifyComplete();
  }

  @Test
  public void saveItem() {
    Item item = new Item("EDI", "Google iTap", 894.09);
    Mono<Item> mono = repository.save(item);
    StepVerifier.create(mono)
        .expectSubscription()
        .expectNextMatches(saveitem -> (saveitem.getId() != null && saveitem.getDescription()
            .equalsIgnoreCase("Google iTap")))
        .verifyComplete();
  }


  @Test
  public void updateItem() {

    Mono<Item> flux = repository.findByDescription("Apple AirLight")
        .map(item -> {
          item.setPrice(675.44);
          return item;
        })
        .flatMap(o -> {
          return repository.save(o);
        });
    StepVerifier.create(flux)
        .expectSubscription()
        .expectNextMatches(item -> item.getPrice().equals(675.44))
        .verifyComplete();
  }

  @Test
  public void deleteItemByID() {
    Mono<Void> mono = repository.findById("ABC")
        .map(Item::getId)
        .flatMap(s -> repository.deleteById(s));

    StepVerifier.create(mono.log())
        .expectSubscription()
        .verifyComplete();

    StepVerifier.create(repository.findById("ABC"))
        .expectSubscription()
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  public void deleteItem() {
    Mono<Void> mono = repository.findByDescription("Apple iSound")
        .map(Item::getId)
        .flatMap(s -> repository.deleteById(s));

    StepVerifier.create(mono.log())
        .expectSubscription()
        .verifyComplete();

    StepVerifier.create(repository.findByDescription("Apple iSound"))
        .expectSubscription()
        .expectNextCount(0)
        .verifyComplete();
  }
}