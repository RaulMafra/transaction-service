package com.transaction.service.emailservice.adapters;

public interface EmailSendingGateway {

    void sendEmail(String to, String subject, String body);
}
