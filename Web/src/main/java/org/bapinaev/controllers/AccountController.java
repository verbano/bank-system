package org.bapinaev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bapinaev.contracts.accounts.AccountService;
import org.bapinaev.dto.accounts.*;
import org.bapinaev.enums.TransactionType;
import org.bapinaev.mappers.AccountDtoMapper;
import org.bapinaev.models.accounts.Account;
import org.bapinaev.models.transactions.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountDtoMapper accountDtoMapper;

    public AccountController(AccountService accountService, AccountDtoMapper accountDtoMapper) {
        this.accountService = accountService;
        this.accountDtoMapper = accountDtoMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new account for a given user login")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.create(request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDtoMapper.toDto(account));
    }

    @GetMapping("/{id}/balance")
    @Operation(summary = "Get the balance of an account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw funds from an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Invalid amount or insufficient funds")
    })
    public ResponseEntity<Void> withdrawal(@PathVariable UUID id, @RequestBody AmountRequest request) {
        accountService.withdrawal(id, request.amount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit funds to an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Void> deposit(@PathVariable UUID id, @RequestBody AmountRequest request) {
        accountService.deposit(id, request.amount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds between two accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "404", description = "One or both accounts not found"),
            @ApiResponse(responseCode = "400", description = "Invalid amount or insufficient funds")
    })
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userLogin}")
    @Operation(summary = "Get all accounts for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(@PathVariable String userLogin) {
        List<Account> accounts = accountService.getAccountsByUserLogin(userLogin);
        List<AccountResponse> response = accounts.stream().map(accountDtoMapper::toAccountResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No accounts found")
    })
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponse> response = accounts.stream().map(accountDtoMapper::toAccountResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get transactions for an account filtered by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam TransactionType type,
            @RequestParam UUID accountId) {
        List<Transaction> transactions = accountService.getTransactionsByType(accountId, type);
        List<TransactionResponse> response = transactions.stream().map(accountDtoMapper::toTransactionResponse).toList();
        return ResponseEntity.ok(response);
    }
}
