package org.bapinaev;

import org.bapinaev.DataAccess.AdminRepository;
import org.bapinaev.DataAccess.ClientRepository;
import org.bapinaev.models.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    public CustomUserDetailsService(
            AdminRepository adminRepository,
            ClientRepository clientRepository
    ) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin != null) {
            return admin;
        }

        return clientRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}