package org.bapinaev.dto.accounts;

import org.bapinaev.dto.users.UserDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record AccountDto(UUID id, BigDecimal balance, UserDto owner, List<TransactionDto> transactions) {
}
