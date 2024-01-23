package com.transaction.service.service;

import com.transaction.service.callback.service.WebhookService;
import com.transaction.service.dtos.response.ListTransactionsDTO;
import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.emailservice.provider.ses.SesEmailSending;
import com.transaction.service.emailservice.service.EmailSendingService;
import com.transaction.service.exception.exceptions.RestException;
import com.transaction.service.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
            throw new RestException("O tipo da transacao nao e deposito");
        }
        Company company = companyService.getCompany(transactionDTO);
        Client client = clientService.getClient(transactionDTO);
        Transaction deposit = new Transaction(transactionDTO.transactionValue(),
                transactionDTO.tax(), client, company, transactionDTO.transactionType());

        this.checkBalanceClient(client, deposit);

        BigDecimal tax = deposit.getValue().multiply(transactionDTO.tax());
        company.setBalance(company.getBalance().add(deposit.getValue().subtract(tax)));
        client.setBalance(client.getBalance().subtract(deposit.getValue()));

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Status depósito", "Depósito realizado com sucesso!");

        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(deposit);

        ListTransactionsDTO latestTransaction = this.allTransactions().get(0);
        webhookService.sendInfoTransaction(latestTransaction);
    }

    public void createWithdraw(TransactionDTO transactionDTO) {
        if (!(transactionDTO.transactionType().equals(TransactionType.WITHDRAW))) {
            throw new RestException("O tipo da transacao nao e saque");
        }
        Company company = companyService.getCompany(transactionDTO);
        Client client = clientService.getClient(transactionDTO);
        Transaction withdraw = new Transaction(transactionDTO.transactionValue(),
                transactionDTO.tax(), client, company, transactionDTO.transactionType());

        this.checkBalanceCompany(company, withdraw);

        BigDecimal tax = withdraw.getValue().multiply(transactionDTO.tax());
        company.setBalance(company.getBalance().subtract(withdraw.getValue().add(tax)));
        client.setBalance(client.getBalance().add(withdraw.getValue()));

        this.emailSendingService.sendEmail(SesEmailSending.EMAIL, "Status saque", "Saque realizado com sucesso!");

        clientService.saveClient(client);
        companyService.saveCompany(company);
        transactionRepository.save(withdraw);

        ListTransactionsDTO latestTransaction = this.allTransactions().get(0);
        webhookService.sendInfoTransaction(latestTransaction);
    }

    private void checkBalanceClient(Client client, Transaction deposit) {
        if (client.getBalance().compareTo(deposit.getValue()) < 0) {
            throw new RestException("Saldo do cliente insuficiente");
        }
    }

    private void checkBalanceCompany(Company company, Transaction withdraw) {
        if (company.getBalance().compareTo(withdraw.getValue()) < 0) {
            throw new RestException("Saldo da empresa insuficiente");
        }
    }

    public List<ListTransactionsDTO> allTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        List<ListTransactionsDTO> listTransactions = new ArrayList<>();
        transactions.forEach(transaction -> {
            listTransactions.add(new ListTransactionsDTO(transaction.getId(),
                    transaction.getValue(), transaction.getTax(), transaction.getClient().getId(),
                    transaction.getCompany().getId(), transaction.getTransactionType(), transaction.getTimestamp()));
        });
        listTransactions.sort(Comparator.comparing(ListTransactionsDTO::timestamp).reversed());
        return listTransactions;
    }
}
