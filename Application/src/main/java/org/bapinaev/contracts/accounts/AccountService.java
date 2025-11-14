package org.bapinaev.contracts.accounts;

import org.bapinaev.enums.TransactionType;
import org.bapinaev.models.accounts.Account;
import org.bapinaev.models.transactions.Transaction;
import org.bapinaev.models.users.User;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    void create(Account account);

    Account create(String login);

    User getOwner(String login);

    BigDecimal getBalance(UUID id);

    void deposit(UUID id, BigDecimal amount);

    void withdrawal(UUID id, BigDecimal amount);

    void transfer(UUID id1, UUID id2, BigDecimal amount);

    List<Account> getAccountsByUserLogin(String login);

    List<Account> getAllAccounts();

    @Transactional(readOnly = true)
    List<Transaction> getTransactionsByType(UUID accountId, TransactionType type);
}