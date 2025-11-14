package org.bapinaev.repositories;

import org.bapinaev.entities.users.UserEntity;
import org.bapinaev.enums.HairColor;
import org.bapinaev.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findByHairColorAndGender(HairColor hairColor, Gender gender);
}