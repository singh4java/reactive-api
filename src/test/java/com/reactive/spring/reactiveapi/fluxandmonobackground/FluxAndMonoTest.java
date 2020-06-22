package com.reactive.spring.reactiveapi.fluxandmonobackground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

  @Test
  public void fluxTest() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        //.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("After Error"))
        .log();
    stringFlux
        .subscribe(System.out::println, (e) -> e.printStackTrace(),
            () -> System.out.println("Completed"));
  }

  @Test
  public void fluxTestElements_WithoutError(){
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .verifyComplete();

  }
}
