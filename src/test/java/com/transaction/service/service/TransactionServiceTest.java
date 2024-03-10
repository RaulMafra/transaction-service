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
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Transactional
class TransactionServiceTest {

    @Mock
    private CompanyService companyService;

    @Mock
    private ClientService clientService;

    @Mock
    private TransactionRepository mockTransactionRepository;

    @Mock
    private EmailSendingService emailSendingService;

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;
    

    Client client;
    Company company;

    @BeforeEach
    public void setup(){
         client = new Client(1L, "example", "12345678910", "example@teste.com", new BigDecimal(0));
         company = new Company(1L, "example2", "12345678000121", "example2@teste.com", new BigDecimal(0));
    }


    @Test
    void should_create_a_transaction_of_the_type_deposit_with_successfully() {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(50), 0.02, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("DEPOSIT"));

        this.client.setBalance(new BigDecimal(80));
        this.company.setBalance(new BigDecimal(100));

        when(this.clientService.getClient(clientService.docFormatting(transactionDTO.docClient()))).thenReturn(client);
        when(this.companyService.getCompany(companyService.docFormatting(transactionDTO.docCompany()))).thenReturn(company);

        this.transactionService.createDeposit(transactionDTO);

        assertEquals(new BigDecimal("149.00"), company.getBalance());
        assertEquals(new BigDecimal(30), client.getBalance());

        verify(clientService, times(2)).getClient(clientService.docFormatting(transactionDTO.docClient()));
        verify(companyService, times(2)).getCompany(companyService.docFormatting(transactionDTO.docCompany()));
        verify(clientService, times(1)).saveClient(client);
        verify(companyService, times(1)).saveCompany(company);
        verify(mockTransactionRepository, times(1)).save(any());

        verify(this.emailSendingService, times(1)).sendEmail(SesEmailSending.EMAIL, "Deposit status", "Your deposit were done with successfully");
        verify(this.webhookService, times(1)).sendInfoTransaction(any());

        verifyNoMoreInteractions(clientService, companyService, mockTransactionRepository, emailSendingService, webhookService);

    }


    @Test
    void should_create_a_transaction_of_the_type_withdraw_with_successfully() {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(100), 0.04, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("WITHDRAW"));

        this.client.setBalance(new BigDecimal(50));
        this.company.setBalance(new BigDecimal(120));

        when(this.clientService.getClient(clientService.docFormatting(transactionDTO.docClient()))).thenReturn(client);
        when(this.companyService.getCompany(companyService.docFormatting(transactionDTO.docCompany()))).thenReturn(company);

        this.transactionService.createWithdraw(transactionDTO);

        assertEquals(new BigDecimal("16.00"), company.getBalance());
        assertEquals(new BigDecimal(150), client.getBalance());

        verify(clientService, times(2)).getClient(clientService.docFormatting(transactionDTO.docClient()));
        verify(companyService, times(2)).getCompany(companyService.docFormatting(transactionDTO.docCompany()));
        verify(clientService, times(1)).saveClient(client);
        verify(companyService, times(1)).saveCompany(company);
        verify(mockTransactionRepository, times(1)).save(any());

        verify(this.emailSendingService, times(1)).sendEmail(SesEmailSending.EMAIL, "Withdraw status", "Your withdraw were done with successfully");
        verify(this.webhookService, times(1)).sendInfoTransaction(any());

        verifyNoMoreInteractions(clientService, companyService, mockTransactionRepository, emailSendingService, webhookService);


    }

    @Test
    @DisplayName("Failed while the creation of the deposit when the balance of the client is insufficient")
    void should_fail_if_the_client_balance_is_insufficient() {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(50), 0.02, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("DEPOSIT"));

        this.client.setBalance(new BigDecimal(20));
        this.company.setBalance(new BigDecimal(100));

        when(this.clientService.getClient(clientService.docFormatting(transactionDTO.docClient()))).thenReturn(client);
        when(this.companyService.getCompany(companyService.docFormatting(transactionDTO.docCompany()))).thenReturn(company);

        assertThrows(RestException.class, () -> this.transactionService.createDeposit(transactionDTO),
                "Balance of the client insufficient");

        assertEquals(new BigDecimal(100), company.getBalance());
        assertEquals(new BigDecimal(20), client.getBalance());

        verify(this.clientService, times(2)).getClient(clientService.docFormatting(client.getDocument()));
        verify(this.companyService, times(2)).getCompany(companyService.docFormatting(company.getDocument()));

        verifyNoInteractions(mockTransactionRepository, emailSendingService, webhookService);
    }

    @Test
    @DisplayName("Failed while the creation of the withdraw when the balance of the company is insufficient")
    void should_fail_if_the_company_balance_is_insufficient() {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(100), 0.04, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("WITHDRAW"));

        this.client.setBalance(new BigDecimal(50));
        this.company.setBalance(new BigDecimal(100));

        when(this.clientService.getClient(clientService.docFormatting(transactionDTO.docClient()))).thenReturn(client);
        when(this.companyService.getCompany(companyService.docFormatting(transactionDTO.docCompany()))).thenReturn(company);

        assertThrows(RestException.class, () -> this.transactionService.createWithdraw(transactionDTO),
                "Balance of the company insufficient");

        assertEquals(new BigDecimal(100), company.getBalance());
        assertEquals(new BigDecimal(50), client.getBalance());

        verify(this.clientService, times(2)).getClient(clientService.docFormatting(client.getDocument()));
        verify(this.companyService, times(2)).getCompany(companyService.docFormatting(company.getDocument()));

        verifyNoInteractions(mockTransactionRepository, emailSendingService, webhookService);
    }

    @SneakyThrows
    @Test
    void should_failed_if_the_type_of_transaction_is_incorrect() {
        TransactionDTO withdrawDTO = new TransactionDTO(new BigDecimal(20), 0.04, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("WITHDRAW"));

        this.client.setBalance(new BigDecimal(50));
        this.company.setBalance(new BigDecimal(100));

        assertThrows(RestException.class, () -> transactionService.createDeposit(withdrawDTO),
                "Transaction type isn't deposit");

        assertEquals(new BigDecimal(50), client.getBalance());
        assertEquals(new BigDecimal(100), company.getBalance());

        verifyNoInteractions(clientService, companyService, mockTransactionRepository,
                emailSendingService, webhookService);

    }

    @Test
    void should_failed_if_transaction_value_is_smaller_or_equal_the_zero(){
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(-20), 0.04, company.getDocument(), client.getDocument(),
                TransactionType.valueOf("WITHDRAW"));

        this.client.setBalance(new BigDecimal(50));
        this.company.setBalance(new BigDecimal(100));

        assertThrows(RestException.class, () -> this.transactionService.createWithdraw(transactionDTO),
                "Transaction value should be higher than zero");

        assertAll(
                () -> assertEquals(new BigDecimal(50), this.client.getBalance()),
                () -> assertEquals(new BigDecimal(100), this.company.getBalance())
        );

        verifyNoInteractions(clientService, companyService, mockTransactionRepository,
                emailSendingService, webhookService);
    }

    @Test
    @DisplayName("Search with successfully all transactions created")
    void should_search_all_transactions_done(){
        UUID depositUUID = UUID.randomUUID();
        UUID withdrawUUID = UUID.randomUUID();
        Transaction deposit = new Transaction(depositUUID, new BigDecimal(50), 0.02, client, company,
                LocalDateTime.now(), TransactionType.DEPOSIT);
        Transaction withdraw = new Transaction(withdrawUUID, new BigDecimal(100), 0.04, client, company,
                LocalDateTime.now(), TransactionType.WITHDRAW);

        Session session = entityManager.unwrap(Session.class);

        session.persist(session.merge(deposit));
        session.persist(session.merge(withdraw));

        List<Transaction> transactions = transactionRepository.findAll();
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());

        assertThat(transactions).isNotEmpty();
    }

}