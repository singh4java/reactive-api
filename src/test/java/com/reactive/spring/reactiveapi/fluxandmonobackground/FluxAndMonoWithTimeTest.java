package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.time.Duration;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoWithTimeTest {

  @Test
  public void inFiniteSequence() throws InterruptedException {
    Flux<Long> flux = Flux.interval(Duration.ofMillis(200)).
        log();
    flux.subscribe(aLong -> System.out.println(aLong));

    Thread.sleep(3000);

  }

  @Test
  public void inFiniteSequence_Test(){
    Flux<Long> flux = Flux.interval(Duration.ofMillis(100))
        .take(3)
        .log();
    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(0l,1l,2l)
        .verifyComplete();
  }

  @Test
  public void inFiniteSequencemap_Test(){
    Flux<Integer> flux = Flux.interval(Duration.ofMillis(100))
        .map(l->new Integer(l.intValue()))
        .take(3)
        .log();
    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(0,1,2)
        .verifyComplete();
  }

  @Test
  public void inFiniteSequencemap_withDelay_Test(){
    Flux<Integer> flux = Flux.interval(Duration.ofMillis(100))
        .delayElements(Duration.ofSeconds(1))
        .map(l->new Integer(l.intValue()))
        .take(3)
        .log();
    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(0,1,2)
        .verifyComplete();
  }
}
