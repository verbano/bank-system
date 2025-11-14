package org.bapinaev.DataAccess;

import org.bapinaev.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
    boolean existsByLogin(String login);
}
