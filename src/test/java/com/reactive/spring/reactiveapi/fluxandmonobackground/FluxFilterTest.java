package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxFilterTest {

  List<String> names = Arrays.asList("Spring", "Spring Boot", "Reactive Spring");


  @Test
  public void fluxFilter_Test() {

    Flux<String> stringFlux = Flux.fromIterable(names)
        .filter(s -> s.startsWith("R"))
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Reactive Spring")
        .verifyComplete();
  }

  @Test
  public void fluxFilter_Test_Length() {

    Flux<String> stringFlux = Flux.fromIterable(names)
        .filter(s -> s.length()>11)
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Reactive Spring")
        .verifyComplete();
  }
}
