package org.bapinaev.Application.contracts;

import org.bapinaev.models.Client;

public interface ClientService {
    Client createClient(String login, String password);
}
