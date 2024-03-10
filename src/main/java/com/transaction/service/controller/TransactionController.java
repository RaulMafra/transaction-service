package com.transaction.service.controller;

import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.dtos.response.ListTransactionDTO;
import com.transaction.service.dtos.response.TransactionResponseDTO;
import com.transaction.service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restservice/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody TransactionDTO transactionDTO){
        transactionService.createDeposit(transactionDTO);
        return new ResponseEntity<>(new TransactionResponseDTO("Deposit done with successfully"), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@RequestBody TransactionDTO transactionDTO){
        transactionService.createWithdraw(transactionDTO);
        return new ResponseEntity<>(new TransactionResponseDTO("Withdraw done with successfully"), HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ListTransactionDTO>> listTransactions(){
        List<ListTransactionDTO> transactions = transactionService.allTransactions().stream().map(ListTransactionDTO::new).toList();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
