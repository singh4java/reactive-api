package com.reactive.spring.reactiveapi.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.reactive.spring.reactiveapi.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

  @Bean
  public RouterFunction<ServerResponse> route(SampleHandlerFunction handlerFunction) {
    return RouterFunctions
        .route(GET("/function/flux").and(accept(MediaType.APPLICATION_JSON)),
            handlerFunction::flux)
        .andRoute(GET("/function/mono").and(accept(MediaType.APPLICATION_JSON)),
            handlerFunction::mono);
  }

}
