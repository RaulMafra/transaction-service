package com.transaction.service.dtos.response;

import com.transaction.service.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ListTransactionsDTO(UUID id, BigDecimal transactionValue, BigDecimal tax, UUID idClient, UUID idCompany, TransactionType transactionType, LocalDateTime timestamp) {
}
