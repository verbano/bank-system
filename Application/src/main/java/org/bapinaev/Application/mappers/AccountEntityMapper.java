package org.bapinaev.Application.mappers;

import org.bapinaev.entities.accounts.AccountEntity;
import org.bapinaev.models.accounts.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserEntityMapper.class, TransactionMapper.class})
public interface AccountEntityMapper {
    AccountEntity toEntity(Account domain);

    Account toDomain(AccountEntity entity);
}
