package com.transaction.service.emailservice.service;

import com.transaction.service.emailservice.adapters.EmailSendingGateway;
import com.transaction.service.emailservice.core.EmailSendingUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService implements EmailSendingUseCase {

    private final EmailSendingGateway emailSendingGateway;

    @Autowired
    public EmailSendingService(EmailSendingGateway emailSendingGateway){
        this.emailSendingGateway = emailSendingGateway;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        this.emailSendingGateway.sendEmail(to, subject, body);
    }
}
