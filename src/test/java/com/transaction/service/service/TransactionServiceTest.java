package com.transaction.service.service;

import com.transaction.service.callback.service.WebhookService;
import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.emailservice.provider.ses.SesEmailSending;
import com.transaction.service.emailservice.service.EmailSendingService;
import com.transaction.service.exception.exceptions.RestException;
import com.transaction.service.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CompanyService companyService;

    @Mock
    private ClientService clientService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EmailSendingService emailSendingService;

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    @DisplayName("Create a deposit with successfully when is everything ok")
    void should_create_a_transaction_of_the_type_deposit_with_successfully() {

        UUID clientUUID = UUID.randomUUID();
        UUID companyUUID = UUID.randomUUID();

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(50), 0.02, clientUUID, companyUUID,
                TransactionType.valueOf("DEPOSIT"));

        Client client = new Client(clientUUID, "example", "11111111100", "example@teste.com", new BigDecimal(80));
        Company company = new Company(companyUUID, "example2", "34345678000123", "example2@teste.com", new BigDecimal(100));

        when(this.clientService.getClient(transactionDTO)).thenReturn(client);
        when(this.companyService.getCompany(transactionDTO)).thenReturn(company);

        this.transactionService.createDeposit(transactionDTO);

        assertEquals(new BigDecimal("149.00"), company.getBalance());
        assertEquals(new BigDecimal("30"), client.getBalance());

        verify(clientService, times(1)).saveClient(client);
        verify(companyService, times(1)).saveCompany(company);
        verify(transactionRepository, times(1)).save(any());

        verify(this.emailSendingService, times(1)).sendEmail(SesEmailSending.EMAIL, "Status depósito", "Depósito realizado com sucesso!");
        verify(this.webhookService, times(1)).sendInfoTransaction(any());

    }


    @Test
    @DisplayName("Create a withdraw with successfully when is everything ok")
    void should_create_a_transaction_of_the_type_withdraw_with_successfully() {

        UUID clientUUID = UUID.randomUUID();
        UUID companyUUID = UUID.randomUUID();

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(100), 0.04, clientUUID, companyUUID,
                TransactionType.valueOf("WITHDRAW"));

        Client client = new Client(clientUUID, "example", "11111111100", "example@teste.com", new BigDecimal(50));
        Company company = new Company(companyUUID, "example2", "34345678000123", "example2@teste.com", new BigDecimal(120));

        when(this.clientService.getClient(transactionDTO)).thenReturn(client);
        when(this.companyService.getCompany(transactionDTO)).thenReturn(company);

        this.transactionService.createWithdraw(transactionDTO);

        assertEquals(new BigDecimal("16.00"), company.getBalance());
        assertEquals(new BigDecimal("150"), client.getBalance());

        verify(clientService, times(1)).saveClient(client);
        verify(companyService, times(1)).saveCompany(company);
        verify(transactionRepository, times(1)).save(any());

        verify(this.emailSendingService, times(1)).sendEmail(SesEmailSending.EMAIL, "Status saque", "Saque realizado com sucesso!");
        verify(this.webhookService, times(1)).sendInfoTransaction(any());
    }

    @Test
    @DisplayName("Failed while the creation of the deposit when isn't everything ok")
    void should_failed_in_the_creation_of_the_transaction_of_the_type_deposit() {

        UUID clientUUID = UUID.randomUUID();
        UUID companyUUID = UUID.randomUUID();

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(50), 0.02, clientUUID, companyUUID,
                TransactionType.valueOf("DEPOSIT"));

        Client client = new Client(clientUUID, "example", "11111111100", "example@teste.com", new BigDecimal(20));
        Company company = new Company(companyUUID, "example2", "34345678000123", "example2@teste.com", new BigDecimal(100));

        when(this.clientService.getClient(transactionDTO)).thenReturn(client);
        when(this.companyService.getCompany(transactionDTO)).thenReturn(company);

        assertThrows(RestException.class, () -> this.transactionService.createDeposit(transactionDTO),
                "Saldo do cliente insuficiente");

        assertEquals(new BigDecimal("100"), company.getBalance());
        assertEquals(new BigDecimal("20"), client.getBalance());

        verify(this.clientService, times(0)).saveClient(client);
        verify(this.companyService, times(0)).saveCompany(company);
        verify(this.transactionRepository, times(0)).save(any());


        verify(this.emailSendingService, times(0)).sendEmail(SesEmailSending.EMAIL, "Status depósito", "Depósito realizado com sucesso!");
        verify(this.webhookService, times(0)).sendInfoTransaction(any());
    }

    @Test
    @DisplayName("Failed while the creation of the withdraw when isn't everything ok")
    void should_failed_in_the_creation_of_the_transaction_of_the_type_withdraw() {

        UUID clientUUID = UUID.randomUUID();
        UUID companyUUID = UUID.randomUUID();

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(100), 0.04, clientUUID, companyUUID,
                TransactionType.valueOf("WITHDRAW"));

        Client client = new Client(clientUUID, "example", "11111111100", "example@teste.com", new BigDecimal(50));
        Company company = new Company(companyUUID, "example2", "34345678000123", "example2@teste.com", new BigDecimal(100));

        assertThrows(RestException.class, () -> this.transactionService.createDeposit(transactionDTO),
                "Saldo da empresa insuficiente");

        assertEquals(new BigDecimal("100"), company.getBalance());
        assertEquals(new BigDecimal("50"), client.getBalance());

        verify(clientService, times(0)).saveClient(client);
        verify(companyService, times(0)).saveCompany(company);
        verify(transactionRepository, times(0)).save(any());

        verify(this.emailSendingService, times(0)).sendEmail(SesEmailSending.EMAIL, "Status saque", "Saque realizado com sucesso!");
        verify(this.webhookService, times(0)).sendInfoTransaction(any());
    }


    @DisplayName("Search all transactions and sort")
    public List<Transaction> allTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());
        return transactions;
    }

}