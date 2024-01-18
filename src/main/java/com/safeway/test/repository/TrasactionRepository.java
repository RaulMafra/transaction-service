package com.safeway.test.repository;

import com.safeway.test.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasactionRepository extends JpaRepository<Transaction, Long> {
}
