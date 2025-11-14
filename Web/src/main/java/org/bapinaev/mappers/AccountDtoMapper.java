package org.bapinaev.mappers;

import org.bapinaev.dto.accounts.AccountDto;
import org.bapinaev.dto.accounts.AccountResponse;
import org.bapinaev.dto.accounts.TransactionResponse;
import org.bapinaev.models.accounts.Account;
import org.bapinaev.models.transactions.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDtoMapper {
    AccountDto toDto(Account account);
    Account toDomain(AccountDto accountDto);

    AccountResponse toAccountResponse(Account account);
    TransactionResponse toTransactionResponse(Transaction transaction);
}
