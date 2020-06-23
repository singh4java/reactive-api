package com.reactive.spring.reactiveapi.fluxandmonobackground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

  @Test
  public void fluxTest() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.just("After Error"))
        .log();
    stringFlux
        .subscribe(System.out::println, (e) -> e.printStackTrace(),
            () -> System.out.println("Completed"));
  }

  @Test
  public void fluxTestElements_WithoutError() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .verifyComplete();

  }

  @Test
  public void fluxTestElements_WithError() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(new RuntimeException("Exception occurred")))
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .verifyError();

  }

  @Test
  public void fluxTestElements_Count() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .log();
    StepVerifier.create(stringFlux)
        .expectNextCount(3)
        .verifyComplete();

  }

  @Test
  public void fluxTestElements_WithError_1() {
    Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(new RuntimeException("Exception occurred")))
        .log();
    StepVerifier.create(stringFlux)
        .expectNext("Spring", "Spring Boot", "Reactive Spring")
        .verifyError();

  }
}
