package com.safeway.test.repository;

import com.safeway.test.domain.user.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findCompanyById(Long id);
}
