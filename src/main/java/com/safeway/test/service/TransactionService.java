package com.safeway.test.service;

import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.safeway.test.domain.transaction.Transaction;
import com.safeway.test.domain.transaction.TransactionType;
import com.safeway.test.domain.user.Client;
import com.safeway.test.domain.user.Company;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.emailservice.provider.ses.SesEmailSending;
import com.safeway.test.emailservice.provider.ses.config.Sesconfig;
import com.safeway.test.emailservice.service.EmailSendingService;
import com.safeway.test.repository.TrasactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TrasactionRepository trasactionRepository;

    @Autowired
    private EmailSendingService emailSendingService;


    public void createDeposit(TransactionDTO transactionDTO) {
        if (!(transactionDTO.transactionType().equals(TransactionType.DEPOSIT))) {
            throw new RuntimeException("O tipo da transação não é depósito");
        }
        Company company = companyService.getCompany(transactionDTO);
        Client client = clientService.getClient(transactionDTO);
        Transaction deposit = new Transaction(transactionDTO.transactionValue(),
                transactionDTO.tax(), client, company, transactionDTO.transactionType());

        this.checkBalanceClient(client, deposit);

        BigDecimal tax = deposit.getValue().multiply(transactionDTO.tax());
        company.setBalance(company.getBalance().add(deposit.getValue().subtract(tax)));
        client.setBalance(client.getBalance().subtract(deposit.getValue()));

        try {
            this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Status depósito", "Depósito realizado com sucesso!");
        } catch (AmazonSimpleEmailServiceException e) {
            throw new AmazonSimpleEmailServiceException(e.getMessage());
        }
        clientService.saveClient(client);
        companyService.saveCompany(company);
        trasactionRepository.save(deposit);

    }

    public void createWithdraw(TransactionDTO transactionDTO) {
        if (!(transactionDTO.transactionType().equals(TransactionType.WITHDRAW))) {
            throw new RuntimeException("O tipo da transação não é saque");
        }
        Company company = companyService.getCompany(transactionDTO);
        Client client = clientService.getClient(transactionDTO);
        Transaction withdraw = new Transaction(transactionDTO.transactionValue(),
                transactionDTO.tax(), client, company, transactionDTO.transactionType());

        this.checkBalanceCompany(company, withdraw);

        BigDecimal tax = withdraw.getValue().multiply(transactionDTO.tax());
        company.setBalance(company.getBalance().subtract(withdraw.getValue().add(tax)));
        client.setBalance(client.getBalance().add(withdraw.getValue()));

        try {
            this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Status saque", "Saque realizado com sucesso!");
        } catch (AmazonSimpleEmailServiceException e) {
            throw new AmazonSimpleEmailServiceException(e.getMessage());
        }
        clientService.saveClient(client);
        companyService.saveCompany(company);
        trasactionRepository.save(withdraw);

    }

    private void checkBalanceClient(Client client, Transaction deposit) {
        if (client.getBalance().compareTo(deposit.getValue()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private void checkBalanceCompany(Company company, Transaction withdraw) {
        if (company.getBalance().compareTo(withdraw.getValue()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }
}
