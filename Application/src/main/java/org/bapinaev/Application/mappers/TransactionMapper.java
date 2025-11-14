package org.bapinaev.Application.mappers;

import org.bapinaev.entities.transactions.TransactionEntity;
import org.bapinaev.models.transactions.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "account", ignore = true)
    TransactionEntity toEntity(Transaction domain);

    @Mapping(target = "account", ignore = true)
    Transaction toDomain(TransactionEntity entity);
}
