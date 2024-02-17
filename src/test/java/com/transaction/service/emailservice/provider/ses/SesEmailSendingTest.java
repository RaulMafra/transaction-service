package com.transaction.service.emailservice.provider.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SesEmailSendingTest {

    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @InjectMocks
    private SesEmailSending sesEmailSending;


    @Test
    @DisplayName("Check if the method override send of the EmailSendingGateway is invoked with successfully")
    void should_invock_the_method_sendEmail_override_of_EmailSendingGateway_with_successfully() {
        assertDoesNotThrow(() -> this.sesEmailSending.sendEmail("examble@test.com", "test", "test"));
        verify(this.amazonSimpleEmailService, times(1)).sendEmail(any());

    }


}
