package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.time.Duration;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class VirtualTimeTest {

  @Test
  public void without_VirtualTime(){
    Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
        .take(3)
        .log();

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(0l,1l,2l)
        .verifyComplete();
  }


  @Test
  public void with_VirtualTime(){

    VirtualTimeScheduler.getOrSet();
    Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
        .take(3)
        .log();

    StepVerifier.withVirtualTime(() -> flux.log())
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(3))
        .expectNext(0l,1l,2l)
        .verifyComplete();
  }

}
