package org.bapinaev.services;

import org.bapinaev.entities.AccountEventEntity;
import org.bapinaev.events.Event;
import org.bapinaev.repositories.AccountEventRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountEventService {

    private final AccountEventRepository accountEventRepository;

    public AccountEventService(AccountEventRepository accountEventRepository) {
        this.accountEventRepository = accountEventRepository;
    }

    public void handle(Event event) {
        AccountEventEntity entity = new AccountEventEntity(
                event.id(),
                event.eventType(),
                event.oldData(),
                event.newData(),
                event.timestamp()
        );

        accountEventRepository.save(entity);
    }
}
