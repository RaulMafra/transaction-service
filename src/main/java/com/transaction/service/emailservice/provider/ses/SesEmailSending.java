package com.transaction.service.emailservice.provider.ses;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.transaction.service.emailservice.adapters.EmailSendingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SesEmailSending implements EmailSendingGateway {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public static final String EMAIL = System.getenv("SENDER_EMAIL");

    @Autowired
    public SesEmailSending(AmazonSimpleEmailService amazonSimpleEmailService){
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SendEmailRequest request = new SendEmailRequest()
                .withSource(EMAIL)
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message
                        (new Content(subject), new Body(new Content(body))));

        try{
            this.amazonSimpleEmailService.sendEmail(request);
        } catch(AmazonSimpleEmailServiceException e){
            throw new AmazonClientException(e.getMessage());
        }

    }

}
