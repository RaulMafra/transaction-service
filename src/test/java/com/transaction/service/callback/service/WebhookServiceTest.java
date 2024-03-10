package com.transaction.service.callback.service;

import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WebhookService webhookService;

    String uri;

    Transaction transaction;

    @BeforeEach
    void init(){
        uri = "https://webhook.site/d62491e0-edaa-4608-ab3b-76e31e3a1b39";

        Client client = new Client(1L, "example", "11111111100",
                "example@teste.com", new BigDecimal("200"));
        Company company = new Company(1L, "example2", "34345678000123",
                "example2@teste.com", new BigDecimal("10"));
        transaction = new Transaction(UUID.randomUUID(), new BigDecimal("100"),
                0.02, client, company, LocalDateTime.now(), TransactionType.DEPOSIT);
    }

    @Test
    void should_send_a_callback_with_successfully_if_everything_be_OK() {
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        webhookService.setUrl(uri);
        assertDoesNotThrow(() -> this.webhookService.sendInfoTransaction(transaction));

        verify(restTemplate, times(1)).exchange("https://webhook.site/d62491e0-edaa-4608-ab3b-76e31e3a1b39", HttpMethod.POST,
                request, String.class);
        verifyNoMoreInteractions(restTemplate);

    }

    @Test
    void should_throw_an_exception_when_the_URL_is_null(){
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        webhookService.setUrl(null);

        assertThrows(RuntimeException.class, () -> this.webhookService.sendInfoTransaction(transaction));
        verifyNoInteractions(restTemplate);

    }


}