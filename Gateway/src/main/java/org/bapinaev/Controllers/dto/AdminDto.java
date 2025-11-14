package org.bapinaev.Controllers.dto;

import org.bapinaev.models.Role;

public record AdminDto(
        Long id,
        String username,
        Role role
) {
}