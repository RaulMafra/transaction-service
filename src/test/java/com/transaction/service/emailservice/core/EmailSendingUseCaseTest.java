package com.transaction.service.emailservice.core;

public interface EmailSendingUseCase {

    void sendEmail(String to, String subject, String body);
}
