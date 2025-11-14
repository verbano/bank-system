package org.bapinaev.repositories;

import org.bapinaev.entities.AccountEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEventEntity, Integer> {
}
