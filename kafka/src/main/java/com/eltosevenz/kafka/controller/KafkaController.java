package com.eltosevenz.kafka.controller;

import com.eltosevenz.kafka.payload.User;
import com.eltosevenz.kafka.service.KafkaProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    public KafkaController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/publish_text")
    public String sendMessage(@RequestParam("message") String message) {
        kafkaProducerService.sendMessage(message);
        return "Message published: " + message;
    }

    @PostMapping("/publish_json")
    public String sendMessageJson(@RequestBody User user) {
        kafkaProducerService.sendJsonMessage(user);
        return "Message published: " + user;
    }
}

