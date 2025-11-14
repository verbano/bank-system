package org.bapinaev.dto.accounts;

import org.bapinaev.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        LocalDateTime timestamp,
        BigDecimal amount,
        TransactionType type
) {}