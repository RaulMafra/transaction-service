package com.transaction.service.emailservice.service;

import com.transaction.service.emailservice.adapters.EmailSendingGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSendingServiceTest {

    @Mock
    private EmailSendingGateway emailSendingGateway;

    @InjectMocks
    private EmailSendingService emailSendingService;

    @Test
    @DisplayName("Check if the method override send of the EmailSendingUseCase is invoked with successfully")
    public void should_invock_the_method_send_of_EmailSendingUseCase_with_successfully() {
        emailSendingService.sendEmail("example@test.com", "test", "test");
        verify(emailSendingGateway, times(1)).sendEmail("example@test.com", "test", "test");
    }

}
