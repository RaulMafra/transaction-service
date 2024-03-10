package com.transaction.service.controller;

import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.CreateUserDTO;
import com.transaction.service.dtos.response.ListClientDTO;
import com.transaction.service.dtos.response.TransactionResponseDTO;
import com.transaction.service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restservice/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    private ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createClient(@RequestBody CreateUserDTO createUserDTO){
        clientService.createClient(createUserDTO);
        return new ResponseEntity<>(new TransactionResponseDTO("Created client with successfully"), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ListClientDTO>> listAllClients(){
        List<ListClientDTO> clients = clientService.listAllClients().stream().map(ListClientDTO::new).toList();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
