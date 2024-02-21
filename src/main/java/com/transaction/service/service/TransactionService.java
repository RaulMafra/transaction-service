package com.transaction.service.service;

import com.transaction.service.callback.service.WebhookService;
import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.emailservice.provider.ses.SesEmailSending;
import com.transaction.service.emailservice.service.EmailSendingService;
import com.transaction.service.exception.exceptions.IllegalFieldException;
import com.transaction.service.exception.exceptions.RestException;
import com.transaction.service.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

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

    public void createDeposit(TransactionDTO withdrawDTO) {
        if(Stream.of(withdrawDTO.idClient(), withdrawDTO.idCompany(), withdrawDTO.transactionValue(), withdrawDTO.tax(), withdrawDTO.transactionType()).anyMatch(Objects::isNull)){
            throw new IllegalFieldException("There's some value absent is the body");
        }
        if (!(withdrawDTO.transactionType().equals(TransactionType.DEPOSIT))) {
            throw new RestException("Transaction type isn't deposit");
        }
        Company company = companyService.getCompany(withdrawDTO);
        Client client = clientService.getClient(withdrawDTO);
        Transaction deposit = new Transaction(UUID.randomUUID(), withdrawDTO.transactionValue(),
                withdrawDTO.tax(), client, company, LocalDateTime.now(), withdrawDTO.transactionType());

        this.checkBalanceClient(client, deposit);

        BigDecimal tax = deposit.getValue().multiply(BigDecimal.valueOf(withdrawDTO.tax()));
        company.setBalance(company.getBalance().add(deposit.getValue().subtract(tax)));
        client.setBalance(client.getBalance().subtract(deposit.getValue()));

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Deposit status", "Your deposit were done with successfully");

        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(deposit);

        webhookService.sendInfoTransaction(deposit);
    }

    public void createWithdraw(TransactionDTO withdrawDTO) {
        if(Stream.of(withdrawDTO.idClient(), withdrawDTO.idCompany(), withdrawDTO.transactionValue(), withdrawDTO.tax(), withdrawDTO.transactionType()).anyMatch(Objects::isNull)){
            throw new IllegalFieldException("There's some value absent is the body");
        }
        if (!(withdrawDTO.transactionType().equals(TransactionType.WITHDRAW))) {
            throw new RestException("Transaction type isn't withdraw");
        }
        Company company = companyService.getCompany(withdrawDTO);
        Client client = clientService.getClient(withdrawDTO);
        Transaction withdraw = new Transaction(UUID.randomUUID(), withdrawDTO.transactionValue(),
                withdrawDTO.tax(), client, company, LocalDateTime.now(), withdrawDTO.transactionType());

        this.checkBalanceCompany(company, withdraw);

        BigDecimal tax = withdraw.getValue().multiply(BigDecimal.valueOf(withdrawDTO.tax()));
        company.setBalance(company.getBalance().subtract(withdraw.getValue().add(tax)));
        client.setBalance(client.getBalance().add(withdraw.getValue()));

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Withdraw status", "Your withdraw were done with successfully");

        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(withdraw);

        webhookService.sendInfoTransaction(withdraw);
    }

    private void checkBalanceClient(Client client, Transaction deposit) {
        if (client.getBalance().compareTo(deposit.getValue()) < 0) {
            throw new RestException("Balance of the client insufficient");
        }
    }

    private void checkBalanceCompany(Company company, Transaction withdraw) {
        if (company.getBalance().compareTo(withdraw.getValue()) < 0) {
            throw new RestException("Balance of the company insufficient");
        }
    }

    public List<Transaction> allTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return transactions;
    }
}
