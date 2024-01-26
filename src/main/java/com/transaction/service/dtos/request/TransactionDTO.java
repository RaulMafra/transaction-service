package com.transaction.service.dtos.request;

import com.transaction.service.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(BigDecimal transactionValue, BigDecimal tax, UUID idClient, UUID idCompany, TransactionType transactionType) {
}
