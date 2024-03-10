package com.transaction.service.dtos.request;

import java.math.BigDecimal;

public record CreateUserDTO(String name, String document, String email, BigDecimal balance) {
}
