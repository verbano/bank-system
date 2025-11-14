package org.bapinaev.dto.users;

import jakarta.validation.constraints.NotBlank;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

public record CreateUserRequest(
        @NotBlank String login,
        @NotBlank String name,
        int age,
        Gender gender,
        HairColor hairColor
) {}
