package org.bapinaev.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bapinaev.events.Event;
import org.bapinaev.services.AccountEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener {

    private final AccountEventService accountEventService;

    private final ObjectMapper objectMapper;

    public AccountEventListener(AccountEventService accountEventService, ObjectMapper objectMapper) {
        this.accountEventService = accountEventService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topics.account-events}")
    public void handleAccountEvent(String eventString) {

        try {
            Event event = objectMapper.readValue(eventString, Event.class);
            accountEventService.handle(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
