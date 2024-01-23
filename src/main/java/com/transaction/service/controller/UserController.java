package com.transaction.service.controller;

import com.transaction.service.emailservice.domain.user.Client;
import com.transaction.service.emailservice.domain.user.Company;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.dtos.response.TransactionMessageDTO;
import com.transaction.service.service.ClientService;
import com.transaction.service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restservices/v1/users")
public class UserController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/create-client")
    public ResponseEntity<TransactionMessageDTO> createClient(@RequestBody UserDTO userDTO){
        clientService.createClient(userDTO);
        return new ResponseEntity<>(new TransactionMessageDTO("Cliente criado!"), HttpStatus.CREATED);
    }

    @PostMapping("/create-company")
    public ResponseEntity<TransactionMessageDTO> createCompany(@RequestBody UserDTO company){
        companyService.createCompany(company);
        return new ResponseEntity<>(new TransactionMessageDTO("Empresa criada!"), HttpStatus.CREATED);
    }

    @GetMapping("/list-companies")
    public ResponseEntity<List<Company>> listAllCompanies(){
        List<Company> companies = companyService.listAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/list-clients")
    public ResponseEntity<List<Client>> listAllClients(){
        List<Client> clients = clientService.listAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
