package com.transaction.service.controller;

import com.transaction.service.dtos.request.CreateUserDTO;
import com.transaction.service.dtos.response.ListCompanyDTO;
import com.transaction.service.dtos.response.TransactionResponseDTO;
import com.transaction.service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restservice/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    private CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createCompany(@RequestBody CreateUserDTO company){
        companyService.createCompany(company);
        return new ResponseEntity<>(new TransactionResponseDTO("Created company with successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ListCompanyDTO>> listAllCompanies(){
        List<ListCompanyDTO> companies = companyService.listAllCompanies().stream().map(ListCompanyDTO::new).toList();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
}
