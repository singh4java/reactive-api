package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoFactoryTest {

  List<String> names = Arrays.asList("Spring", "Boor", "Redis");

  @Test
  public void monoUsingJustEmpty() {
    Mono<Object> mono = Mono.justOrEmpty(null).log();

    StepVerifier.create(mono)
        .verifyComplete();
  }

  @Test
  public void monoUsingSupplier() {
    Supplier<String> supplier = () -> "Spring";

    Mono<String> mono = Mono.fromSupplier(supplier).log();

    StepVerifier.create(mono)
        .expectNext("Spring")
        .verifyComplete();
  }
}
