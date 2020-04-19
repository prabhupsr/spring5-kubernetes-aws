package com.sample.kuber;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;


@Configuration
public class RouteConfig {

    private MerchantRepo merchantRepo;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RouteConfig(MerchantRepo merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    @Bean
    RouterFunction<ServerResponse> serverResponseRouterFunction() {

        return nest(path("/merchant"), RouterFunctions
                .route(GET("/create/sample/{num}").and(accept(MediaType.APPLICATION_JSON)), this::process)
                .andRoute(POST("/create").and(accept(MediaType.APPLICATION_JSON)), this::createMerchant)
                .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON)), this::fetchMerchant));
    }

    private Mono<ServerResponse> fetchMerchant(ServerRequest serverRequest) {

        return merchantRepo.findById(Integer.valueOf(serverRequest.pathVariable("id")))
                .switchIfEmpty(Mono.error(new RuntimeException("record not found")))
                .flatMap(merchant -> createSuccessResponse(merchant, Merchant.class))
                .onErrorResume(throwable -> createFailureResponse(throwable));
    }

    private Mono<ServerResponse> createMerchant(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(Merchant.class).flatMap(merchantRepo::insert)
                .flatMap(merchant -> createSuccessResponse(merchant, Merchant.class))
                .onErrorResume(throwable -> createFailureResponse(throwable));
    }

    private Mono<ServerResponse> createFailureResponse(Throwable throwable) {

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(throwable.getMessage()), String.class);
    }

    private <T> Mono<ServerResponse> createSuccessResponse(T response, Class<T> aClass) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Mono.just(response), aClass);
    }

    private Mono<ServerResponse>    process(ServerRequest serverRequest) {

        Stream<Merchant> integerStream =
                IntStream.range(0, Integer.valueOf(serverRequest.pathVariable("id")))
                        .boxed()
                        .map(integer -> new Merchant(integer, "name" + integer));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Flux.fromStream(integerStream).delayElements(Duration.ofSeconds(1)), Merchant.class);
    }


}
