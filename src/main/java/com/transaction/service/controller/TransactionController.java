package com.transaction.service.controller;

import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.dtos.response.TransactionMessageDTO;
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
    public ResponseEntity<TransactionMessageDTO> deposit(@RequestBody TransactionDTO transactionDTO){
        transactionService.createDeposit(transactionDTO);
        return new ResponseEntity<>(new TransactionMessageDTO("Deposit done with successfully"), HttpStatus.OK);
    }

    @PostMapping("/withdraw")

    public ResponseEntity<TransactionMessageDTO> withdraw(@RequestBody TransactionDTO transactionDTO){
        transactionService.createWithdraw(transactionDTO);
        return new ResponseEntity<>(new TransactionMessageDTO("Withdraw done with successfully"), HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Transaction>> listTransactions(){
        List<Transaction> transactions = transactionService.allTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
