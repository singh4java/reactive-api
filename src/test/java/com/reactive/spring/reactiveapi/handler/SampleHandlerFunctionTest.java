package com.reactive.spring.reactiveapi.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerFunctionTest {

  @Autowired
  WebTestClient webTestClient;
  @Test
  void flux() {
    Flux<Integer> flux = webTestClient
        .get()
        .uri("/function/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Integer.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(1)
        .expectNext(2)
        .expectNext(3)
        .expectNext(4)
        .thenCancel()
        .verify();
  }

  @Test
  public void getMono() {


    webTestClient
        .get()
        .uri("/function/mono")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .consumeWith(response -> {
          assertEquals("Spring", response.getResponseBody());
        });


  }
}