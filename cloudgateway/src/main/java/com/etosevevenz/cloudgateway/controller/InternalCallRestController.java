package com.etosevevenz.cloudgateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InternalCallRestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/call-api")
    public String callApi() {
        String url = "http://localhost:8080/service1/hello";
        //Calling api inside api using RestTemplate
        return restTemplate.getForObject(url, String.class);
    }
}
