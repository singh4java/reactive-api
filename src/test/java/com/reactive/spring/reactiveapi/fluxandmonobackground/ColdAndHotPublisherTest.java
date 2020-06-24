package com.reactive.spring.reactiveapi.fluxandmonobackground;

import java.time.Duration;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {

  @Test
  public void coldPublisherTest() throws InterruptedException {
    Flux<String> just = Flux.just("A", "B", "C", "D")
        .delayElements(Duration.ofSeconds(1));
    just.subscribe(s->System.out.println("Subscribe-1 : "+s));
    Thread.sleep(2000);
    just.subscribe(s->System.out.println("Subscribe-2 : "+s));

    Thread.sleep(4000);

  }

  @Test
  public void hotPublisherTest() throws InterruptedException {
    Flux<String> just = Flux.just("A", "B", "C", "D")
        .delayElements(Duration.ofSeconds(1));
    ConnectableFlux<String> connectableFlux = just.publish();
    connectableFlux.connect();
    connectableFlux.subscribe(s->System.out.println("Subscribe-1 : "+s));
    Thread.sleep(2000);
    connectableFlux.subscribe(s->System.out.println("Subscribe-2 : "+s));

    Thread.sleep(4000);

  }

}
