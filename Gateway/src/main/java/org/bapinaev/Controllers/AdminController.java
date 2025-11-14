package org.bapinaev.Controllers;

import org.bapinaev.Application.AccountServiceClient;
import org.bapinaev.Application.UserServiceClient;
import org.bapinaev.Application.contracts.AdminService;
import org.bapinaev.Application.contracts.ClientService;
import org.bapinaev.Controllers.dto.*;
import org.bapinaev.models.Admin;
import org.bapinaev.enums.Gender;
import org.bapinaev.enums.HairColor;
import org.bapinaev.enums.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final ClientService clientService;
    private final UserServiceClient userServiceClient;
    private final AccountServiceClient accountServiceClient;

    public AdminController(
            AdminService adminService,
            ClientService clientService,
            UserServiceClient userServiceClient,
            AccountServiceClient accountServiceClient
    ) {
        this.adminService = adminService;
        this.clientService = clientService;
        this.userServiceClient = userServiceClient;
        this.accountServiceClient = accountServiceClient;
    }

    @PostMapping("/admins")
    public ResponseEntity<Admin> createAdmin(@RequestBody RegisterAdminRequest request) {
        Admin admin = adminService.createAdmin(request.username(), request.password());
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/clients")
    public ResponseEntity<Void> createClient(
            @RequestBody RegisterClientRequest request
    ) {
        clientService.createClient(request.username(), request.password());

        userServiceClient.createUser(
                new CreateUserRequest(
                        request.username(),
                        request.name(),
                        request.age(),
                        request.gender(),
                        request.hairColor()
                )
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsersFiltered(
            @RequestParam HairColor hairColor,
            @RequestParam Gender gender
    ) {
        return ResponseEntity.ok(
                userServiceClient.getFilteredUsers(hairColor, gender)
        );
    }

    @GetMapping("/users/{login}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String login) {
        return ResponseEntity.ok(
                userServiceClient.getUserInfo(login)
        );
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(
                accountServiceClient.getAllAccounts()
        );
    }

    @GetMapping("/accounts/user/{userLogin}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(
            @PathVariable String userLogin
    ) {
        return ResponseEntity.ok(
                accountServiceClient.getAccountsByUser(userLogin)
        );
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam TransactionType type,
            @PathVariable UUID accountId
    ) {
        return ResponseEntity.ok(
                accountServiceClient.getTransactions(accountId, type)
        );
    }
}