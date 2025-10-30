package com.eltosevenz.kafka.service;

import com.eltosevenz.kafka.payload.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "demo-topic1", groupId = "demo-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

    @KafkaListener(topics = "demo-topic", groupId = "demo-group")
    public void listen(User message) {
        System.out.println("Received json message: " + message);
    }
}

