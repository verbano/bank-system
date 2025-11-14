package org.bapinaev.mappers;

import org.bapinaev.dto.users.CreateUserRequest;
import org.bapinaev.dto.users.FriendDto;
import org.bapinaev.dto.users.UserDto;
import org.bapinaev.dto.users.UserInfoResponse;
import org.bapinaev.models.users.User;
import org.bapinaev.models.users.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDto toDto(User user);

    UserInfoResponse toInfoResponse(UserInfo userInfo);

    FriendDto toFriend(User user);

    User toDomain(UserDto userDto);

    User toDomain(CreateUserRequest request);
}
