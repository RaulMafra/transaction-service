package com.transaction.service.controller;

import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.dtos.response.TransactionMessageDTO;
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
    public ResponseEntity<TransactionMessageDTO> createCompany(@RequestBody UserDTO company){
        companyService.createCompany(company);
        return new ResponseEntity<>(new TransactionMessageDTO("Empresa criada!"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Company>> listAllCompanies(){
        List<Company> companies = companyService.listAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
}
