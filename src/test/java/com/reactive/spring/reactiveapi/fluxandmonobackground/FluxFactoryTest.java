package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxFactoryTest {

  List<String> names = Arrays.asList("Spring", "Boor", "Redis");

  @Test
  public void fluxUsingIterable() {

    Flux<String> flux = Flux.fromIterable(names);

    StepVerifier.create(flux)
        .expectNext("Spring", "Boor", "Redis")
        .verifyComplete();

  }

  @Test
  public void fluxUsingArray() {

    String[] namesArray = {"Spring", "Boor", "Redis"};

    Flux<String> flux = Flux.fromArray(namesArray);

    StepVerifier.create(flux)
        .expectNext("Spring", "Boor", "Redis")
        .verifyComplete();

  }

  @Test
  public void fluxUsingStream() {

    Flux<String> flux = Flux.fromStream(names.stream());

    StepVerifier.create(flux)
        .expectNext("Spring", "Boor", "Redis")
        .verifyComplete();

  }

  @Test
  public void fluxUsingRange() {

    Flux<Integer> flux = Flux.range(1, 5);

    StepVerifier.create(flux)
        .expectNext(1,2,3,4,5)
        .verifyComplete();
  }
}
