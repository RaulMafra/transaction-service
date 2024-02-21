package com.transaction.service.exception.exceptions;

public class UserNotFound extends RestException {

    public UserNotFound(String msg){
        super(msg);
    }
}
