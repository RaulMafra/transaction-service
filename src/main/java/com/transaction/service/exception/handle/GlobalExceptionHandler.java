package com.transaction.service.exception.handle;

import com.amazonaws.AmazonClientException;
import com.transaction.service.exception.exceptions.IllegalFieldException;
import com.transaction.service.exception.exceptions.RestException;
import com.transaction.service.exception.exceptions.UserNotFound;
import com.transaction.service.exception.exceptions.WebhookException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.UnknownHostException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @ExceptionHandler({WebhookException.class, UnknownHostException.class})
    private ResponseEntity<ResponseError> handleWebhookException(WebhookException e, WebRequest request, HttpServletRequest servletRequest) {
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AmazonClientException.class)
    private ResponseEntity<ResponseError> handleWebServiceException(AmazonClientException e, WebRequest request, HttpServletRequest servletRequest) {
        String response = "Email service unavailable";
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalFieldException.class)
    private ResponseEntity<ResponseError> handleFieldExceptions(IllegalFieldException e, WebRequest request, HttpServletRequest servletRequest){
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    private ResponseEntity<ResponseError> handleRestApiException(RestException e, WebRequest request, HttpServletRequest servletRequest){
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFound.class)
     private ResponseEntity<ResponseError> handleUserNotFound(UserNotFound e, WebRequest request, HttpServletRequest servletRequest){
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ResponseError> handleDataPersistence(DataIntegrityViolationException e, WebRequest request, HttpServletRequest servletRequest){
        String response = "Conflict when persisting the client";
        ResponseError responseError = new ResponseError(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.CONFLICT);
    }

}
