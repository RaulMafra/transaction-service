package com.transaction.service.exception.exceptions;

public class WebhookException extends RuntimeException{

    public WebhookException(String msg, Throwable throwable){
        super(msg, throwable);
    }
}
