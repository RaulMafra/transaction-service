package com.transaction.service.service;

import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.exception.exceptions.IllegalFieldException;
import com.transaction.service.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
class CompanyServiceTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Obtain a company created with successfully")
    void should_create_and_obtain_a_company_with_successfully() {
        String document = "34345678000123";
        UserDTO company = new UserDTO("Test", document, "test@gmail.com", new BigDecimal(100));

        Company newCompany = new Company(company);
        assertDoesNotThrow(() -> this.docFormatting(newCompany));
        this.saveCompany(newCompany);

        assertThat(this.companyRepository.findCompanyByDocument("34.345.678/0001-23").isPresent()).isTrue();

    }

    @Test
    @DisplayName("Failed in the creation of the company")
    void should_failed_to_the_create_and_obtain_a_company() {
        String document = "3434567800012300";
        UserDTO company = new UserDTO("Jose", document, "jose@gmail.com", new BigDecimal(10));

        Company newCompany = new Company(company);
        assertThrows(IllegalFieldException.class, () -> this.docFormatting(newCompany));
        this.saveCompany(newCompany);

        assertThat(this.companyRepository.findCompanyByDocument("34.345.678/0001-2300").isEmpty()).isTrue();

    }

    @DisplayName("Verify the amount of the characters of document and format this document")
    private void docFormatting(Company company){
        if(company.getDocument().length() != 14){
            throw new IllegalFieldException("A quantidade de caracteres do documento excede o permitido");
        }
        String docFormatted = String.format("%s.%s.%s/%s-%s",
                company.getDocument().subSequence(0,2), company.getDocument().subSequence(2,5),
                company.getDocument().subSequence(5,8), company.getDocument().subSequence(8,12),
                company.getDocument().subSequence(12,14));
        company.setDocument(docFormatted);
    }

    @DisplayName("Save a company with the correct data")
    public void saveCompany(Company company) {
        this.entityManager.persist(company);
    }


}