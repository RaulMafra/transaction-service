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
        UserDTO company = new UserDTO("Test", "34345678000123", "test@example.com", new BigDecimal(100));

        Company newCompany = new Company(company);
        assertDoesNotThrow(() -> newCompany.setDocument(docFormatting(newCompany.getDocument())));
        this.saveCompany(newCompany);

        assertThat(this.companyRepository.findCompanyByDocument("34.345.678/0001-23").isPresent()).isTrue();

    }

    @Test
    @DisplayName("Failed in the creation of the company when the size document is different than 14 digits")
    void should_failed_to_the_create_a_company_when_size_document_different_of_the_14() {
        String document = "3434567800012300";
        UserDTO company = new UserDTO("Test", document, "test@example.com", new BigDecimal(10));

        Company newCompany = new Company(company);
        assertThrows(IllegalFieldException.class, () -> newCompany.setDocument(docFormatting(newCompany.getDocument())),
                "Quantity of characters of the document insufficient or exceeds the allowed");
        this.saveCompany(newCompany);

        assertThat(this.companyRepository.findCompanyByDocument("34.345.678/0001-2300").isEmpty()).isTrue();

    }

    @Test
    @DisplayName("Search with successfully all companies created")
    void should_search_all_companies_created() {
        UserDTO company = new UserDTO("Test", "34345678000123", "test@example.com", new BigDecimal(80));
        UserDTO company2 = new UserDTO("Test2", "34345678000567", "test2@example.com", new BigDecimal(50));

        Company newcompany = new Company(company);
        Company newcompany2 = new Company(company2);

        assertDoesNotThrow(() -> newcompany.setDocument(this.docFormatting(newcompany.getDocument())));
        assertDoesNotThrow(() -> newcompany2.setDocument(this.docFormatting(newcompany2.getDocument())));

        this.saveCompany(newcompany);
        this.saveCompany(newcompany2);

        assertThat(this.companyRepository.findAll()).isNotEmpty();

    }

    @DisplayName("Verify the amount of the characters of document and format this document")
    private String docFormatting(String document){
        if(document.length() != 14){
            throw new IllegalFieldException("A quantidade de caracteres do documento excede o permitido");
        }
        return String.format("%s.%s.%s/%s-%s",
                document.subSequence(0,2), document.subSequence(2,5),
                document.subSequence(5,8), document.subSequence(8,12),
                document.subSequence(12,14));
    }

    @DisplayName("Save a company with the correct data")
    public void saveCompany(Company company) {
        this.entityManager.persist(company);
    }


}