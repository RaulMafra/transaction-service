package com.safeway.test.service;

import com.safeway.test.domain.user.Company;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.dtos.UserDTO;
import com.safeway.test.exception.exceptions.IllegalFormattingException;
import com.safeway.test.exception.exceptions.RestException;
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
        this.docFormatting(newCompany);
        this.saveCompany(newCompany);
    }

    private void docFormatting(Company company){
        if(company.getDocument().length() != 14){
            throw new IllegalFormattingException("A quantidade de caracteres do documento excede o permitido");
        }
        String docFormatted = String.format("%s.%s.%s/%s-%s",
                company.getDocument().subSequence(0,2), company.getDocument().subSequence(2,5),
                company.getDocument().subSequence(5,8), company.getDocument().subSequence(8,12),
                company.getDocument().subSequence(12,14));
        company.setDocument(docFormatted);
    }

    public Company getCompany(TransactionDTO transactionDTO){
        return companyRepository.findCompanyById(transactionDTO.idCompany()).orElseThrow(() -> new RestException("Empresa nao encontrada"));
    }

    public void saveCompany(Company company){
        companyRepository.save(company);
    }

    public List<Company> listAllCompanies(){
        return companyRepository.findAll();
    }
}
