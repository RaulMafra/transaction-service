package com.transaction.service.emailservice.provider.ses;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.transaction.service.emailservice.adapters.EmailSendingGatewayTest;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class SesTestEmailSendingTest implements EmailSendingGatewayTest {

    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService;

    public static final String EMAIL = System.getenv("SENDER_EMAIL");


    @Override
    public void sendEmail(String to, String subject, String body) {
        assertAll(
                () -> assertNotNull(to),
                () -> assertNotNull(subject),
                () -> assertNotNull(body),
                () -> assertNotNull(EMAIL));
        SendEmailRequest request = new SendEmailRequest()
                .withSource(EMAIL)
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message
                        (new Content(subject), new Body(new Content(body))));

        try{
            Mockito.verify(this.amazonSimpleEmailService, Mockito.times(1)).sendEmail(request);
        } catch(AmazonSimpleEmailServiceException e){
            throw new AmazonClientException(e.getMessage());
        }

    }

}
