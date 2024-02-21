package com.transaction.service.controller;

import com.transaction.service.domain.user.Client;
import com.transaction.service.dtos.request.UserDTO;
import com.transaction.service.dtos.response.TransactionMessageDTO;
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
    public ResponseEntity<TransactionMessageDTO> createClient(@RequestBody UserDTO userDTO){
        clientService.createClient(userDTO);
        return new ResponseEntity<>(new TransactionMessageDTO("Created client with successfully"), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Client>> listAllClients(){
        List<Client> clients = clientService.listAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
