package com.transaction.service.dtos.response;

import com.transaction.service.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListTransactionsDTO(Long id, BigDecimal transactionValue, BigDecimal tax, Long idClient, Long idCompany, TransactionType transactionType, LocalDateTime timestamp) {
}
