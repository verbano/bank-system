package org.bapinaev.repositories;

import org.bapinaev.entities.UserEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends JpaRepository<UserEventEntity, Integer> {
}
