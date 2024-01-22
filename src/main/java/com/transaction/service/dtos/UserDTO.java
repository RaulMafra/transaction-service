package com.transaction.service.dtos;

import java.math.BigDecimal;

public record UserDTO(String name, String document, String email, BigDecimal balance) {
}
