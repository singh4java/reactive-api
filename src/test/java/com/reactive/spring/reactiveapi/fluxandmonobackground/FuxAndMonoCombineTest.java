package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FuxAndMonoCombineTest {

  @Test
  public void combineUsingMarge(){
    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");

    Flux<String> merge = Flux.merge(flux1, flux2);

    StepVerifier.create(merge.log())
        .expectSubscription()
        .expectNext("A", "B", "C","D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void combineUsingMarge_WithDelay(){
    Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.of(1, ChronoUnit.SECONDS));
    Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.of(1, ChronoUnit.SECONDS));

    //Flux<String> merge = Flux.merge(flux1, flux2);
    Flux<String> merge = Flux.concat(flux1, flux2);

    StepVerifier.create(merge.log())
        .expectNext("A", "B", "C","D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void combineUsingZip_WithDelay(){
    Flux<String> flux1 = Flux.just("A", "B", "C");
    Flux<String> flux2 = Flux.just("D", "E", "F");

    //Flux<String> merge = Flux.merge(flux1, flux2);
    Flux<String> merge = Flux.zip(flux1, flux2,(t1,t2)->{
      return t1.concat(t2);
    });

    StepVerifier.create(merge.log())
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
  }

}
