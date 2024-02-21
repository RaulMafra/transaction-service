package com.transaction.service.exception.exceptions;

public class RestException extends RuntimeException{

    public RestException(String msg){
        super(msg);
    }

    public RestException(String msg, Throwable cause){
        super(msg, cause);
    }
}
