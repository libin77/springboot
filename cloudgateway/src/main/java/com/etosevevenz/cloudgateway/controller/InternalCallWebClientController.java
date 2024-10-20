package com.etosevevenz.cloudgateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class InternalCallWebClientController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/call-api-reactive")
    public Mono<String> callApi() {
        String url = "http://localhost:8080/service1/hello";
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
}
