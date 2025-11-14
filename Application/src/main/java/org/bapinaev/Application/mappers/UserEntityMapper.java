package org.bapinaev.Application.mappers;

import org.bapinaev.entities.users.UserEntity;
import org.bapinaev.models.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "friends", ignore = true)
    UserEntity toEntity(User domain);

    @Mapping(target = "friends", ignore = true)
    User toDomain(UserEntity entity);
}