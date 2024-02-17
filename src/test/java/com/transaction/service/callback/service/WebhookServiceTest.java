package com.transaction.service.callback.service;

import com.transaction.service.domain.transaction.Transaction;
import com.transaction.service.domain.transaction.TransactionType;
import com.transaction.service.domain.user.Client;
import com.transaction.service.domain.user.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.description.type.TypeList;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WebhookService webhookService;

    String url;

    Transaction transaction;

    @BeforeEach
    void init(){
        url = "https://webhook.site/d62491e0-edaa-4608-ab3b-76e31e3a1b39";

        Client client = new Client(UUID.randomUUID(), "example", "11111111100",
                "example@teste.com", new BigDecimal("200"));
        Company company = new Company(UUID.randomUUID(), "example2", "34345678000123",
                "example2@teste.com", new BigDecimal("10"));
        transaction = new Transaction(UUID.randomUUID(), new BigDecimal("100"),
                0.02, client, company, LocalDateTime.now(), TransactionType.DEPOSIT);
    }

    @Test
    @DisplayName("Performs a send of the a callback with successfully if everything be OK")
    void should_send_a_callback_with_successfully() {
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        webhookService.setUrl(url);
        assertDoesNotThrow(() -> this.webhookService.sendInfoTransaction(transaction));
        verify(restTemplate, times(1)).exchange("https://webhook.site/d62491e0-edaa-4608-ab3b-76e31e3a1b39", HttpMethod.POST,
                request, String.class);

    }

    @Test
    @DisplayName("Throw an exception when the URL is null")
    void should_throw_an_exception_when_the_URL_is_null(){
        HttpEntity<Transaction> request = new HttpEntity<>(transaction);

        webhookService.setUrl(null);
        assertThrows(RuntimeException.class, () -> this.webhookService.sendInfoTransaction(transaction));
        verifyNoInteractions(restTemplate);

    }


}