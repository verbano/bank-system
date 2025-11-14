package org.bapinaev.repositories;

import org.bapinaev.entities.accounts.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByOwnerLogin(String login);
}