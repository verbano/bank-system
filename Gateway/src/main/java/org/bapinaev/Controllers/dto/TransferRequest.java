package org.bapinaev.Controllers.dto;

import org.bapinaev.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferRequest(
        UUID fromAccountId,
        UUID toAccountId,
        LocalDateTime timestamp,
        BigDecimal amount,
        TransactionType type) {
}
