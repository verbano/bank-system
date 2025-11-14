package org.bapinaev.Controllers;

import org.bapinaev.Application.AccountServiceClient;
import org.bapinaev.Application.UserServiceClient;
import org.bapinaev.Controllers.dto.AccountResponse;
import org.bapinaev.Controllers.dto.AmountRequest;
import org.bapinaev.Controllers.dto.TransferRequest;
import org.bapinaev.models.Client;
import org.bapinaev.Controllers.dto.UserInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    private final UserServiceClient userServiceClient;
    private final AccountServiceClient accountServiceClient;

    public ClientController(
            UserServiceClient userServiceClient,
            AccountServiceClient accountServiceClient
    ) {
        this.userServiceClient = userServiceClient;
        this.accountServiceClient = accountServiceClient;
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMe(@AuthenticationPrincipal Client client) {
        return ResponseEntity.ok(
                userServiceClient.getUserInfo(client.getUsername())
        );
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal Client client) {
        return ResponseEntity.ok(
                accountServiceClient.getAccountsByUser(client.getUsername())
        );
    }

    @PostMapping("/friends/{friendLogin}")
    public ResponseEntity<Void> addFriend(
            @AuthenticationPrincipal Client client,
            @PathVariable String friendLogin
    ) {
        userServiceClient.addFriend(client.getUsername(), friendLogin);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends/{friendLogin}")
    public ResponseEntity<Void> removeFriend(
            @AuthenticationPrincipal Client client,
            @PathVariable String friendLogin
    ) {
        userServiceClient.removeFriend(client.getUsername(), friendLogin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestBody TransferRequest request,
            @AuthenticationPrincipal Client client
    ) {
        if (!validateAccountOwnership(client.getUsername(), request.fromAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        accountServiceClient.transfer(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<Void> withdraw(
            @PathVariable UUID accountId,
            @RequestBody AmountRequest request,
            @AuthenticationPrincipal Client client
    ) {
        if (!validateAccountOwnership(client.getUsername(), accountId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        accountServiceClient.withdraw(accountId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Void> deposit(
            @PathVariable UUID accountId,
            @RequestBody AmountRequest request,
            @AuthenticationPrincipal Client client
    ) {
        if (!validateAccountOwnership(client.getUsername(), accountId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        accountServiceClient.deposit(accountId, request);
        return ResponseEntity.ok().build();
    }

    private boolean validateAccountOwnership(String userLogin, UUID accountId) {
        List<AccountResponse> accounts = accountServiceClient.getAccountsByUser(userLogin);
        return accounts.stream()
                .anyMatch(account -> account.id().equals(accountId));
    }
}