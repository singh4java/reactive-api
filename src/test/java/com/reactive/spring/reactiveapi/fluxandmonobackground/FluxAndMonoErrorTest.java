package com.reactive.spring.reactiveapi.fluxandmonobackground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

  @Test
  public void fluxErrorHandling() {

    Flux<String> flux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"));

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectError(RuntimeException.class)
        .verify();

  }

  @Test
  public void fluxErrorHandling_WithResume() {

    Flux<String> flux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorResume(throwable -> {
          System.out.println("Exception is :" + throwable);
          return Flux.just("defualt", "defualt-1");
        });

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("defualt", "defualt-1")
        .verifyComplete();

  }

  @Test
  public void fluxErrorHandling_OnErrorReturn() {

    Flux<String> flux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorReturn("default");

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("default")
        .verifyComplete();

  }

  @Test
  public void fluxErrorHandling_OnErrorMap() {

    Flux<String> flux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap(throwable -> new CustomException(throwable));

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectError(CustomException.class)
        .verify();

  }

  @Test
  public void fluxErrorHandling_OnErrorMap_withRetry() {

    Flux<String> flux = Flux.just("A", "B", "C")
        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap(throwable -> new CustomException(throwable))
        .retry(2);

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectNext("A", "B", "C")
        .expectError(CustomException.class)
        .verify();

  }

}
