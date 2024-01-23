package com.transaction.service.dtos;

import com.transaction.service.emailservice.domain.transaction.TransactionType;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal transactionValue, BigDecimal tax, Long idClient, Long idCompany, TransactionType transactionType) {
}
