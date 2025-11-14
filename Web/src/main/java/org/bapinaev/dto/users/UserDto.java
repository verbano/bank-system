package org.bapinaev.dto.users;

import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

import java.util.Set;

public record UserDto(
        String login,
        String name,
        int age,
        Gender gender,
        HairColor hairColor,
        Set<FriendDto> friends) {
}
