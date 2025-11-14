package org.bapinaev.Application.contracts;

import org.bapinaev.models.Admin;

public interface AdminService {
    Admin createAdmin(String username, String password);
}
