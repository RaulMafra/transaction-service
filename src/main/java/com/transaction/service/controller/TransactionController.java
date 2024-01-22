package com.transaction.service.controller;

import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.dtos.ResponseDTO;
import com.transaction.service.dtos.TransactionDTO;
import com.transaction.service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/safeway/v1/transaction")
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

    @GetMapping("/listAll")
    public ResponseEntity<List<Transaction>> listTransactions(){
        List<Transaction> transactions = transactionService.allTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
