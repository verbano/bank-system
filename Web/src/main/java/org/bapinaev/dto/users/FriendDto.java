package org.bapinaev.dto.users;

import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

public record FriendDto(String login, String name, int age, Gender gender, HairColor hairColor) {
}