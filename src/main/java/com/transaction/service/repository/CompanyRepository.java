package com.transaction.service.repository;

import com.transaction.service.domain.user.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findCompanyById(Long id);
    Optional<Company> findCompanyByDocument(String document);
}
