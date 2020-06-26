package com.reactive.spring.reactiveapi.initialize;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {


  @Autowired
  ItemReactiveRepository repository;

  @Override
  public void run(String... args) throws Exception {
    List<Item> itemList = Arrays.asList(
        new Item(null, "Apple Tv", 799.7),
        new Item(null, "Apple MAC pro", 1700.7),
        new Item(null, "Apple iPad", 599.17),
        new Item(null, "Apple Watch", 399.7),
        new Item(null, "Apple AirLight", 5699.7),
        new Item("ABC", "Apple iSound", 8.7)
    );
    repository.deleteAll()
        .thenMany(Flux.fromIterable(itemList))
        .flatMap(repository::save)
        .subscribe(Item::print);
        //.thenMany(repository.findAll())
        //.subscribe(o -> System.out.println("Mongo Data : " + o.toString()));
  }
}
