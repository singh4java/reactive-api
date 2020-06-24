package com.reactive.spring.reactiveapi.fluxandmonobackground;


import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MonkBacKPressureTest {

  @Test
  public void backPressureTest() {
    Flux<Integer> flux = Flux.range(1, 10)
        .log();

    StepVerifier.create(flux)
        .expectSubscription()
        .thenRequest(2)
        .expectNext(1, 2)
        .thenRequest(2)
        .expectNext(3, 4)
        .thenCancel()
        .verify();

  }

  @Test
  public void backPressureTest_Subscribe() {
    Flux<Integer> flux = Flux.range(1, 10)
        .log();

    flux.subscribe(i -> System.out.println("Data : " + i)
        , (e) -> e.printStackTrace()
        , () -> System.out.println("Completed")
        , (subscription -> subscription.request(2)));


  }

  @Test
  public void backPressureTest_cancel() {
    Flux<Integer> flux = Flux.range(1, 10)
        .log();

    flux.subscribe(i -> System.out.println("Data : " + i)
        , (e) -> e.printStackTrace()
        , () -> System.out.println("Completed")
        , (subscription -> subscription.cancel()));


  }


  @Test
  public void customized_backPressure() {
    Flux<Integer> flux = Flux.range(1, 10)
        .log();

    flux.subscribe(new BaseSubscriber<Integer>() {
      @Override
      protected void hookOnNext(Integer value) {
        request(1);
        System.out.println("value :"+value);
        if(value==4)
          cancel();
      }
    });


  }

}
