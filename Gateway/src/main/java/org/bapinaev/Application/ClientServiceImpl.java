package org.bapinaev.Application;

import org.bapinaev.Application.contracts.ClientService;
import org.bapinaev.models.Client;
import org.bapinaev.DataAccess.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client createClient(String login, String password) {
        if (clientRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Client already exists");
        }

        Client client = new Client(login, passwordEncoder.encode(password));
        return clientRepository.save(client);
    }
}