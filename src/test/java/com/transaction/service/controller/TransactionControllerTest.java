package com.transaction.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import com.transaction.service.dtos.request.TransactionDTO;
import com.transaction.service.service.TransactionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    MockMvc mockMvc;

    String serializeObject;

    URI uri;


    @SneakyThrows
    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .alwaysDo(print()).build();

        uri = new URI("/restservice/v1/transaction");
    }

    @SneakyThrows
    @Test
    @DisplayName("Created a deposit with successfully and return OK")
    void should_created_a_deposit_with_successfully_and_return_status_code_200() throws JsonProcessingException {
        TransactionDTO deposit = new TransactionDTO(new BigDecimal(20), 0.03, UUID.randomUUID(), UUID.randomUUID(), TransactionType.DEPOSIT);

        doNothing().when(this.transactionService).createDeposit(deposit);

        serializeObject = new ObjectMapper().writeValueAsString(deposit);

        mockMvc.perform(post(uri + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(serializeObject))
                .andExpect(status().isOk())
                .andReturn();

        verify(this.transactionService, times(1)).createDeposit(deposit);
        verifyNoMoreInteractions(this.transactionService);
    }

    @SneakyThrows
    @Test
    @DisplayName("Created a withdraw with successfully and return OK")
    void should_created_a_withdraw_with_successfully_and_return_status_code_200() throws JsonProcessingException {
        TransactionDTO withdraw = new TransactionDTO(new BigDecimal(20), 0.03, UUID.randomUUID(), UUID.randomUUID(), TransactionType.DEPOSIT);

        doNothing().when(this.transactionService).createWithdraw(withdraw);

        serializeObject = new ObjectMapper().writeValueAsString(withdraw);

        mockMvc.perform(post(uri + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(serializeObject))
                .andExpect(status().isOk())
                .andReturn();

        verify(this.transactionService, times(1)).createWithdraw(withdraw);
        verifyNoMoreInteractions(this.transactionService);
    }

    @SneakyThrows
    @Test
    @DisplayName("Return all transactions done and status code OK")
    void should_search_all_transactions_done_and_return_status_code_200() throws JsonProcessingException {
        Client client = new Client(UUID.randomUUID(), "Client", "123456789100", "client@test.com", new BigDecimal(50));
        Company company = new Company(UUID.randomUUID(), "Company", "34345678000121", "company@test.com", new BigDecimal(60));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(UUID.randomUUID(), new BigDecimal(10), 0.03, client, company, LocalDateTime.now(), TransactionType.WITHDRAW));
        transactions.add(new Transaction(UUID.randomUUID(), new BigDecimal(20), 0.02, client, company, LocalDateTime.now(), TransactionType.DEPOSIT));


        serializeObject = new ObjectMapper().findAndRegisterModules()
                .writeValueAsString(transactions);

        when(this.transactionService.allTransactions()).thenReturn(transactions);

        mockMvc.perform(get(uri + "/listAll")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(serializeObject))
                .andExpect(status().isOk())
                .andReturn();

        verify(this.transactionService, times(1)).allTransactions();
        verifyNoMoreInteractions(this.transactionService);
    }
}