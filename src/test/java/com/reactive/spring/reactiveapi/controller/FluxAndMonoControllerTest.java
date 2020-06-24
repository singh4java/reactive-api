package com.reactive.spring.reactiveapi.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest()
class FluxAndMonoControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void getFlux_1() {

    Flux<Integer> flux = webTestClient
        .get()
        .uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Integer.class)
        .getResponseBody();
    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(1, 2, 3, 4)
        .verifyComplete();
  }

  @Test
  void getFlux_2() {

    webTestClient
        .get()
        .uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBodyList(Integer.class)
        .hasSize(4);

  }

  @Test
  void getFlux_3() {

    List<Integer> list = Arrays.asList(1, 2, 3, 4);
    EntityExchangeResult<List<Integer>> result = webTestClient
        .get()
        .uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .returnResult();

    assertEquals(list, result.getResponseBody());
  }

  @Test
  void getFlux_4() {

    List<Integer> list = Arrays.asList(1, 2, 3, 4);
    webTestClient
        .get()
        .uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .consumeWith(listEntityExchangeResult -> assertEquals(list,
            listEntityExchangeResult.getResponseBody()));

  }

  @Test
  void getFluxStream() {
    Flux<Long> flux = webTestClient
        .get()
        .uri("/fluxinfinitestream")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Long.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectNext(0l)
        .expectNext(1l)
        .expectNext(2l)
        .thenCancel()
        .verify();
  }

  @Test
  public void getMono() {

    String expected = "Spring";
    webTestClient
        .get()
        .uri("/mono")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          assertEquals(expected, response.getResponseBody());
        });


  }


}