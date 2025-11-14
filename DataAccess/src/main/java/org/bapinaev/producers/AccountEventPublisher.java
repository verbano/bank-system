package org.bapinaev.producers;

import org.bapinaev.events.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountEventPublisher {
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public AccountEventPublisher(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String login, Event event) {
        String topic = "account-topic";
        kafkaTemplate.send(topic, login, event);
    }
}
