package com.reactive.spring.reactiveapi.fluxandmonobackground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

  @Test
  public void monoTest() {
    Mono<String> mono = Mono.just("Spring")
        .log();
    StepVerifier.create(mono)
        .expectNext("Spring")
        .verifyComplete();
  }

  @Test
  public void monoTest_Error() {

    StepVerifier.create(Mono.error(new RuntimeException("Exception Occurred")).log())
        .expectError(RuntimeException.class)
        .verify();
  }

}
