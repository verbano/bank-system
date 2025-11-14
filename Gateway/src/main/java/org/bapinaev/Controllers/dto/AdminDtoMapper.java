package org.bapinaev.Controllers.dto;

import org.bapinaev.models.Admin;

public class AdminDtoMapper {
    public AdminDto toDto(Admin admin) {
        return new AdminDto(
                admin.getId(),
                admin.getUsername(),
                admin.getRole()
        );
    }
}
