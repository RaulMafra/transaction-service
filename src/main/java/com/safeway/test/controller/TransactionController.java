package com.safeway.test.controller;

import com.safeway.test.dtos.ResponseDTO;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ResponseDTO> deposit(@RequestBody TransactionDTO transactionDTO){
        transactionService.createDeposit(transactionDTO);
        return new ResponseEntity<>(new ResponseDTO("Deposito realizado com sucesso!"), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResponseDTO> withdraw(@RequestBody TransactionDTO transactionDTO){
        transactionService.createWithdraw(transactionDTO);
        return new ResponseEntity<>(new ResponseDTO("Saque realizado com sucesso!"), HttpStatus.OK);
    }
}
