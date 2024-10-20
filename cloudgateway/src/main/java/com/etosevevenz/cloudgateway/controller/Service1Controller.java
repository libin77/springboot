package com.etosevevenz.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service1")
public class Service1Controller {
    @GetMapping("/hello")
    public String helloService1() {
        return "Hello from Service 1!";
    }
}

