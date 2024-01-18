package com.safeway.test.repository;

import com.safeway.test.domain.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientById(Long id);
}
