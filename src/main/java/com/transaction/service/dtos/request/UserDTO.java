package com.transaction.service.dtos.request;

import java.math.BigDecimal;

public record UserDTO(String name, String document, String email, BigDecimal balance) {
}
