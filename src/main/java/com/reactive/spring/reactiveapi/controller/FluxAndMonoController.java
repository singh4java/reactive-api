package com.reactive.spring.reactiveapi.controller;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxAndMonoController {

  @GetMapping("/flux")
  public Flux<Integer> getFlux() {
    return Flux.just(1, 2, 3, 4)
        .log();
  }

  @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Integer> getFluxStream() {
    return Flux.just(1, 2, 3, 4)
        .log();
  }

  @GetMapping(value = "/fluxinfinitestream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Long> getFluxInfiniteStream() {
    return Flux.interval(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping("/mono")
  public Mono<String> getMono() {
    return Mono.just("Spring");
  }
}
