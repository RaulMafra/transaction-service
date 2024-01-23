package com.transaction.service.dtos.request;

import com.transaction.service.domain.transaction.TransactionType;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal transactionValue, BigDecimal tax, Long idClient, Long idCompany, TransactionType transactionType) {
}
