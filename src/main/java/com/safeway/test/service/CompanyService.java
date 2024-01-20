package com.safeway.test.service;

import com.safeway.test.domain.user.Company;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.dtos.UserDTO;
import com.safeway.test.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    private CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public void createCompany(UserDTO company){
        Company newCompany = new Company(company);
        this.checkFields(newCompany);
        this.saveCompany(newCompany);
    }

    private void checkFields(Company company){
        if(company.getDocument().length() != 14){
            throw new RuntimeException("O CNPJ deve ter 14 dígitos!");
        }
    }

    public Company getCompany(TransactionDTO transactionDTO){
        return companyRepository.findCompanyById(transactionDTO.idCompany()).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public void saveCompany(Company company){
        companyRepository.save(company);
    }

    public List<Company> listAllCompanies(){
        return companyRepository.findAll();
    }
}
