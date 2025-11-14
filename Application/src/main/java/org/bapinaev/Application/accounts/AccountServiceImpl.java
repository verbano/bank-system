package org.bapinaev.Application.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bapinaev.Application.mappers.AccountEntityMapper;
import org.bapinaev.Application.mappers.TransactionMapper;
import org.bapinaev.Application.mappers.UserEntityMapper;
import org.bapinaev.contracts.accounts.AccountService;
import org.bapinaev.entities.accounts.AccountEntity;
import org.bapinaev.entities.transactions.TransactionEntity;
import org.bapinaev.enums.TransactionType;
import org.bapinaev.events.Event;
import org.bapinaev.models.accounts.Account;
import org.bapinaev.models.transactions.Transaction;
import org.bapinaev.models.users.User;
import org.bapinaev.producers.AccountEventPublisher;
import org.bapinaev.repositories.AccountRepository;
import org.bapinaev.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountEntityMapper accountEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final TransactionMapper transactionMapper;
    private final AccountEventPublisher accountEventPublisher;
    private final ObjectMapper objectMapper;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository, AccountEntityMapper accountEntityMapper, UserEntityMapper userEntityMapper, TransactionMapper transactionMapper, AccountEventPublisher accountEventPublisher, ObjectMapper objectMapper)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountEntityMapper = accountEntityMapper;
        this.userEntityMapper = userEntityMapper;
        this.transactionMapper = transactionMapper;
        this.accountEventPublisher = accountEventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void create(Account account) {
        AccountEntity accountEntity = accountEntityMapper.toEntity(account);
        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public Account create(String login) {
        User user = userRepository.findById(login).map(userEntityMapper::toDomain).get();
        Account account = new Account(user);
        create(account);

        String accountJson = null;
        try {
            accountJson = objectMapper.writeValueAsString(account);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Event event = new Event(
                account.getId().toString(),
                "ACCOUNT_CREATED",
                null,
                accountJson,
                Instant.now()
        );

        accountEventPublisher.sendMessage(account.getId().toString(), event);

        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public User getOwner(String login) {
        return userRepository.findById(login).map(userEntityMapper::toDomain).get();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID id) {
        Account account = accountRepository.findById(id).map(accountEntityMapper::toDomain).get();
        return account.getBalance();
    }

    @Override
    @Transactional
    public void deposit(UUID id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        AccountEntity accountEntity = accountRepository.findById(id).get();

        BigDecimal oldBalance = accountEntity.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);
        accountEntity.setBalance(newBalance);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccount(accountEntity);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.Deposit);
        transaction.setTimestamp(LocalDateTime.now());

        accountEntity.addTransaction(transaction);

        accountRepository.save(accountEntity);

        Event event = new Event(
                id.toString(),
                "DEPOSIT",
                oldBalance.toString(),
                newBalance.toString(),
                Instant.now()
        );

        accountEventPublisher.sendMessage(id.toString(), event);
    }

@Override
@Transactional
public void withdrawal(UUID id, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) < 0)
        throw new IllegalArgumentException("Amount must be greater than zero");

    AccountEntity accountEntity = accountRepository.findById(id).get();

    if (accountEntity.getBalance().compareTo(amount) < 0)
        throw new IllegalArgumentException("Insufficient funds");

    BigDecimal oldBalance = accountEntity.getBalance();
    BigDecimal newBalance = oldBalance.subtract(amount);
    accountEntity.setBalance(newBalance);

    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setAccount(accountEntity);
    transactionEntity.setAmount(amount);
    transactionEntity.setType(TransactionType.Withdrawal);
    transactionEntity.setTimestamp(LocalDateTime.now());

    accountEntity.addTransaction(transactionEntity);

    accountRepository.save(accountEntity);

    Event event = new Event(
            id.toString(),
            "WITHDRAWAL",
            oldBalance.toString(),
            newBalance.toString(),
            Instant.now()
    );

    accountEventPublisher.sendMessage(id.toString(), event);
}

@Override
@Transactional
public void transfer(UUID id1, UUID id2, BigDecimal amount) {
    AccountEntity account1 = accountRepository.findById(id1).get();
    AccountEntity account2 = accountRepository.findById(id2).get();

    BigDecimal commission = calculateCommissionRate(
            accountEntityMapper.toDomain(account1),
            accountEntityMapper.toDomain(account2)
    ).multiply(amount);

    if (account1.getBalance().compareTo(amount.add(commission)) < 0) {
        throw new IllegalArgumentException("Insufficient funds");
    }

    BigDecimal old1Balance = account1.getBalance();
    BigDecimal old2Balance = account2.getBalance();

    BigDecimal account1NewBalance = account1.getBalance().subtract(amount.add(commission));
    BigDecimal account2NewBalance = account2.getBalance().add(amount);

    account1.setBalance(account1NewBalance);
    account2.setBalance(account2NewBalance);

    TransactionEntity debitTransaction = new TransactionEntity(
            account1,
            amount.add(commission).negate(),
            TransactionType.Transfer
    );

    TransactionEntity creditTransaction = new TransactionEntity(
            account2,
            amount,
            TransactionType.Transfer
    );

    account1.addTransaction(debitTransaction);
    account2.addTransaction(creditTransaction);

    accountRepository.save(account1);
    accountRepository.save(account2);

    try {
        Event event1 = new Event(
                id1.toString(),
                "TRANSFER",
                objectMapper.writeValueAsString(Map.of("from", id1, "amount", old1Balance)),
                objectMapper.writeValueAsString(Map.of("to", id2, "account1NewBalance", account1NewBalance)),
                Instant.now()
        );
        Event event2 = new Event(
                id1.toString(),
                "TRANSFER",
                objectMapper.writeValueAsString(Map.of("from", id1, "amount", old2Balance)),
                objectMapper.writeValueAsString(Map.of("to", id2, "account1NewBalance", account2NewBalance)),
                Instant.now()
        );

        accountEventPublisher.sendMessage(id1.toString(), event1);

    } catch (JsonProcessingException e) {
        throw new RuntimeException("Error serializing friends data", e);
    }

}

    @Transactional(readOnly = true)
    @Override
    public List<Account> getAccountsByUserLogin(String login) {
        List<AccountEntity> accounts = accountRepository.findByOwnerLogin(login);
        return accounts.stream()
                .map(accountEntityMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountEntityMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> getTransactionsByType(UUID accountId, TransactionType type) {
        return accountRepository.findById(accountId)
                .map(AccountEntity::getTransactions)
                .orElse(List.of())
                .stream()
                .filter(t -> t.getType() == type)
                .map(transactionMapper::toDomain)
                .toList();
    }


    private BigDecimal calculateCommissionRate(Account account1, Account account2) {
        if (account1.getOwner().equals(account2.getOwner()))
            return BigDecimal.ZERO;

        User user1 = account1.getOwner();
        User user2 = account2.getOwner();

        if (user1.getFriends().contains(user2))
            return new BigDecimal("0.03");

        return new BigDecimal("0.1");
    }
}