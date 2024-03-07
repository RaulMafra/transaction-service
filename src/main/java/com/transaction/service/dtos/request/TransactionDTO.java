package com.transaction.service.dtos.request;

import com.transaction.service.domain.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(BigDecimal transactionValue, Double tax, String docCompany, String docClient, TransactionType transactionType) {
}
