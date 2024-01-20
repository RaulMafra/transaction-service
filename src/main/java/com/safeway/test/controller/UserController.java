package com.safeway.test.controller;

import com.safeway.test.domain.user.Client;
import com.safeway.test.domain.user.Company;
import com.safeway.test.dtos.UserDTO;
import com.safeway.test.dtos.ResponseDTO;
import com.safeway.test.service.ClientService;
import com.safeway.test.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safeway/v1/users")
public class UserController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/create-client")
    public ResponseEntity<ResponseDTO> createClient(@RequestBody UserDTO userDTO){
        clientService.createClient(userDTO);
        return new ResponseEntity<>(new ResponseDTO("Cliente criado!"), HttpStatus.CREATED);
    }

    @PostMapping("/create-company")
    public ResponseEntity<ResponseDTO> createCompany(@RequestBody UserDTO company){
        companyService.createCompany(company);
        return new ResponseEntity<>(new ResponseDTO("Empresa criada!"), HttpStatus.CREATED);
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
