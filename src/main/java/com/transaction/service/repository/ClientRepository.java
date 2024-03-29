package com.transaction.service.repository;

import com.transaction.service.domain.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByDocument(String document);
}
