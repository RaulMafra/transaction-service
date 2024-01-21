package com.safeway.test.service;

import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.safeway.test.callback.service.WebhookService;
import com.safeway.test.domain.transaction.Transaction;
import com.safeway.test.domain.transaction.TransactionType;
import com.safeway.test.domain.user.Client;
import com.safeway.test.domain.user.Company;
import com.safeway.test.dtos.ResponseCompanyDTO;
import com.safeway.test.dtos.TransactionDTO;
import com.safeway.test.emailservice.provider.ses.SesEmailSending;
import com.safeway.test.emailservice.service.EmailSendingService;
import com.safeway.test.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ClientInfoStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private WebhookService webhookService;

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

//        try {
//            this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Status depósito", "Depósito realizado com sucesso!");
//        } catch (AmazonSimpleEmailServiceException e) {
//            throw new AmazonSimpleEmailServiceException(e.getMessage());
//        }
        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(deposit);

        Transaction latestTransaction = this.allTransactions().get(0);
        webhookService.sendInfoTransaction(new ResponseCompanyDTO(latestTransaction));
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
        transactionRepository.save(withdraw);

        Transaction latestTransaction = this.allTransactions().get(0);
        webhookService.sendInfoTransaction(new ResponseCompanyDTO(latestTransaction));
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

    public List<Transaction> allTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return transactions;
    }
}
