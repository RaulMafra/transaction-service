package com.safeway.test.dtos;

import java.math.BigDecimal;

public record ClientDTO(String name, String document, String email, BigDecimal balance) {
}
