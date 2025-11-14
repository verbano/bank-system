package org.bapinaev.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bapinaev.events.Event;
import org.bapinaev.services.UserEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class UserEventListener {

    private final UserEventService userEventService;

    private final ObjectMapper objectMapper;

    public UserEventListener(UserEventService userEventService, ObjectMapper objectMapper) {
        this.userEventService = userEventService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "${kafka.topics.client-topic}")
    public void handleUserEvent(String eventString) {
        try {
            Event event = objectMapper.readValue(eventString, Event.class);
            userEventService.handle(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
