package com.reactive.spring.reactiveapi.fluxandmonobackground;

import static reactor.core.scheduler.Schedulers.parallel;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {

  List<String> names = Arrays.asList("Spring", "BooT", "Redis");


  @Test
  public void transformUsingMap() {
    Flux<String> flux = Flux.fromIterable(names)
        .map(s -> s.toUpperCase())
        .log();
    StepVerifier.create(flux)
        .expectNext("SPRING", "BOOT", "REDIS")
        .verifyComplete();
  }

  @Test
  public void transformUsingMap_Length() {
    Flux<Integer> flux = Flux.fromIterable(names)
        .map(s -> s.length())
        .log();
    StepVerifier.create(flux)
        .expectNext(6, 4, 5)
        .verifyComplete();
  }

  @Test
  public void transformUsingMap_Repect() {
    Flux<Integer> flux = Flux.fromIterable(names)
        .map(s -> s.length())
        .repeat(1)
        .log();
    StepVerifier.create(flux)
        .expectNext(6, 4, 5, 6, 4, 5)
        .verifyComplete();
  }

  @Test
  public void transformUsingMap_Filter() {
    Flux<String> flux = Flux.fromIterable(names)
        .filter(s -> s.length() > 5)
        .map(s -> s.toUpperCase())
        .log();
    StepVerifier.create(flux)
        .expectNext("SPRING")
        .verifyComplete();
  }

  @Test
  public void transformUsingFlatMap() {
    Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"))
        .flatMap(s -> {
          return Flux.fromIterable(convertToList(s));
        }).log();
    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }

  private List<String> convertToList(String s) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Arrays.asList(s, "NewValue");
  }

  @Test
  public void transformUsingParallel() {
    Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"))
        .window(2)
        .flatMap((s) ->
            s.map(this::convertToList).subscribeOn(parallel()))
        .flatMap(s -> Flux.fromIterable(s))
        .log();
    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  public void transformUsingParallel2() {
    Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"))
        .window(2)
        .concatMap((s) ->
            s.map(this::convertToList).subscribeOn(parallel()))
        .flatMap(s -> Flux.fromIterable(s))
        .log();
    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  public void transformUsingSequence() {
    Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"))
        .window(2)
        .flatMapSequential((s) ->
            s.map(this::convertToList).subscribeOn(parallel()))
        .flatMap(s -> Flux.fromIterable(s))
        .log();
    StepVerifier.create(flux)
        .expectNextCount(6)
        .verifyComplete();
  }

}
