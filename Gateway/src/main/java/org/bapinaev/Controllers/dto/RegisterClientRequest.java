package org.bapinaev.Controllers.dto;

import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;

public record RegisterClientRequest(
        String username,
        String password,
        String name,
        int age,
        Gender gender,
        HairColor hairColor
) {}

