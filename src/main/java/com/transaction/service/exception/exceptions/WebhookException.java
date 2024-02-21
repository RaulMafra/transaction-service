package com.transaction.service.exception.exceptions;

public class WebhookException extends RestException {

    public WebhookException(String msg, Throwable cause){
        super(msg, cause);
    }
}
