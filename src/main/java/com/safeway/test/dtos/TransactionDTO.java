package com.safeway.test.dtos;

import com.safeway.test.domain.transaction.TransactionType;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal transactionValue, BigDecimal tax, Long idClient, Long idCompany, TransactionType transactionType) {
}
