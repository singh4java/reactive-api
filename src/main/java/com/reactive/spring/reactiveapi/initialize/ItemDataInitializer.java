package com.reactive.spring.reactiveapi.initialize;

import com.reactive.spring.reactiveapi.document.Item;
import com.reactive.spring.reactiveapi.document.ItemCapped;
import com.reactive.spring.reactiveapi.repository.ItemReactiveCappedRepository;
import com.reactive.spring.reactiveapi.repository.ItemReactiveRepository;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {


  ItemReactiveRepository repository;
  ReactiveMongoTemplate reactiveMongoTemplate;
  ItemReactiveCappedRepository cappedRepository;

  @Autowired
  public ItemDataInitializer(
      ItemReactiveRepository repository,
      ReactiveMongoTemplate reactiveMongoTemplate,
      ItemReactiveCappedRepository cappedRepository
  ) {
    this.repository = repository;
    this.reactiveMongoTemplate = reactiveMongoTemplate;
    this.cappedRepository = cappedRepository;

  }

  @Override
  public void run(String... args) throws Exception {
    initialDataSetup();
    createCappedCollection();
    dataSetupForCappedCollection();
  }

  private void createCappedCollection() {
    reactiveMongoTemplate.dropCollection(ItemCapped.class);
    reactiveMongoTemplate.createCollection(ItemCapped.class,
        CollectionOptions.empty().maxDocuments(20).size(50000).capped());

  }

  public List<Item> data() {
    return Arrays.asList(
        new Item(null, "Apple Tv", 799.7),
        new Item(null, "Apple MAC pro", 1700.7),
        new Item(null, "Apple iPad", 599.17),
        new Item(null, "Apple Watch", 399.7),
        new Item(null, "Apple AirLight", 5699.7),
        new Item("ABC", "Apple iSound", 8.7)
    );
  }


  private void initialDataSetup() {
    repository.deleteAll()
        .thenMany(Flux.fromIterable(data()))
        .flatMap(repository::save)
        .thenMany(repository.findAll())
        .subscribe(Item::print);
  }

  public void dataSetupForCappedCollection(){
    Flux<ItemCapped> cappedFlux = Flux.interval(Duration.ofSeconds(1))
        .map(i -> new ItemCapped(null, "Random Item " + i, (100.00) + i));
    cappedRepository.insert(cappedFlux)
        .subscribe(ItemCapped::print);
  }
}
