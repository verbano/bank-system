package org.bapinaev.Application;

import org.bapinaev.Application.contracts.AdminService;
import org.bapinaev.models.Admin;
import org.bapinaev.models.Role;
import org.bapinaev.DataAccess.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin createAdmin(String username, String password) {
        if (adminRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Admin with this username already exists");
        }

        return adminRepository.save(
                Admin.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(Role.ADMIN)
                        .build()
        );
    }
}
