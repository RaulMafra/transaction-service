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

    public void createDeposit(TransactionDTO depositDTO) {
        if(Stream.of(depositDTO.docClient(), depositDTO.docCompany(), depositDTO.transactionValue(), depositDTO.tax(), depositDTO.transactionType()).anyMatch(Objects::isNull)){
            throw new IllegalFieldException("There's some value absent is the body");
        }
        this.checkInfoDeposit(depositDTO);

        Company company = companyService.getCompany(companyService.docFormatting(depositDTO.docCompany()));
        Client client = clientService.getClient(clientService.docFormatting(depositDTO.docClient()));

        Transaction deposit = new Transaction(UUID.randomUUID(), depositDTO.transactionValue(),
                depositDTO.tax(), client, company, LocalDateTime.now(), depositDTO.transactionType());

        BigDecimal tax = deposit.getValue().multiply(BigDecimal.valueOf(deposit.getTax()));
        this.checkBalanceClient(client.getBalance(), deposit.getValue().add(tax));

        company.setBalance(company.getBalance().add(deposit.getValue().subtract(tax)));
        client.setBalance(client.getBalance().subtract(deposit.getValue()));


        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(deposit);

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Deposit status", "Your deposit were done with successfully");
        webhookService.sendInfoTransaction(deposit);
    }

    public void createWithdraw(TransactionDTO withdrawDTO) {
        if(Stream.of(withdrawDTO.docClient(), withdrawDTO.docCompany(), withdrawDTO.transactionValue(), withdrawDTO.tax(), withdrawDTO.transactionType()).anyMatch(Objects::isNull)){
            throw new IllegalFieldException("There's some value absent is the body");
        }
        this.checkInfoWithdraw(withdrawDTO);

        Company company = companyService.getCompany(companyService.docFormatting(withdrawDTO.docCompany()));
        Client client = clientService.getClient(clientService.docFormatting(withdrawDTO.docClient()));

        Transaction withdraw = new Transaction(UUID.randomUUID(), withdrawDTO.transactionValue(),
                withdrawDTO.tax(), client, company, LocalDateTime.now(), withdrawDTO.transactionType());

        BigDecimal tax = withdraw.getValue().multiply(BigDecimal.valueOf(withdraw.getTax()));
        this.checkBalanceCompany(company.getBalance(), withdraw.getValue().add(tax));

        company.setBalance(company.getBalance().subtract(withdraw.getValue().add(tax)));
        client.setBalance(client.getBalance().add(withdraw.getValue()));

        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(withdraw);

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Withdraw status", "Your withdraw were done with successfully");
        webhookService.sendInfoTransaction(withdraw);
    }

    private void checkBalanceClient(BigDecimal balance, BigDecimal valueTransaction) {
        if (balance.compareTo(valueTransaction) < 0) {
            throw new RestException("Balance of the client insufficient");
        }
    }

    private void checkBalanceCompany(BigDecimal balance, BigDecimal valueTransaction) {
        if (balance.compareTo(valueTransaction) < 0) {
            throw new RestException("Balance of the company insufficient");
        }

    }

    private void checkInfoDeposit(TransactionDTO depositDTO){
        if(depositDTO.transactionValue().compareTo(BigDecimal.ZERO) <= 0){
            throw new RestException("Transaction value should be higher than zero");
        }
        if (!(depositDTO.transactionType().equals(TransactionType.DEPOSIT))) {
            throw new RestException("Transaction type isn't deposit");
        }
    }

    private void checkInfoWithdraw(TransactionDTO withdrawDTO){
        if(withdrawDTO.transactionValue().compareTo(BigDecimal.ZERO) <= 0){
            throw new RestException("Transaction value should be higher than zero");
        }
        if (!(withdrawDTO.transactionType().equals(TransactionType.WITHDRAW))) {
            throw new RestException("Transaction type isn't withdraw");
        }
    }

    public List<Transaction> allTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return transactions;
    }
}
