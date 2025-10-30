package com.eltosevenz.kafka.service;

import com.eltosevenz.kafka.payload.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private  KafkaTemplate<String, User> kafkaTemplateJson;

    public void sendMessage(String message) {
        kafkaTemplate.send("demo-topic", message);
        System.out.println("Message sent: " + message);
    }

    public void sendJsonMessage(User data) {
        Message<User> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC,"demo-topic")
                .build();
        kafkaTemplateJson.send(message);
        System.out.println("JSON Message sent: " + message);
    }
}

