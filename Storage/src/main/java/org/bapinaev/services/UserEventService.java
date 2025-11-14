package org.bapinaev.services;

import org.bapinaev.entities.UserEventEntity;
import org.bapinaev.events.Event;
import org.bapinaev.repositories.UserEventRepository;
import org.springframework.stereotype.Service;

@Service
public class UserEventService {
    private final UserEventRepository userEventRepository;

    public UserEventService(UserEventRepository userEventRepository) {
        this.userEventRepository = userEventRepository;
    }


    public void handle(Event event) {
        UserEventEntity entity = new UserEventEntity(
                event.id(),
                event.eventType(),
                event.oldData(),
                event.newData(),
                event.timestamp()
        );;

        userEventRepository.save(entity);
    }
}
