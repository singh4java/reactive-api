package com.reactive.spring.reactiveapi.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.reactive.spring.reactiveapi.constants.ItemConstants;
import com.reactive.spring.reactiveapi.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemsRouter {

  @Bean
  public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {
    return RouterFunctions
        .route(
            GET(ItemConstants.ITEM_END_POINT_V1_FUNCTIONAL).and(accept(MediaType.APPLICATION_JSON)),
            itemsHandler::getAllItems)
        .andRoute(GET(ItemConstants.ITEM_END_POINT_V1_FUNCTIONAL.concat("/{id}"))
            .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getOneItems);
  }
}
