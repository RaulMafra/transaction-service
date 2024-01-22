package com.safeway.test.exception.handle;

import com.amazonaws.AmazonClientException;
import com.amazonaws.Response;
import com.safeway.test.exception.exceptions.IllegalFormattingException;
import com.safeway.test.exception.exceptions.RestException;
import com.safeway.test.exception.exceptions.WebhookException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.UnknownHostException;
import java.util.IllegalFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private org.springframework.http.HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @ExceptionHandler({WebhookException.class, UnknownHostException.class})
    private ResponseEntity<ResponseError> handleWebhookException(WebhookException e, WebRequest request, HttpServletRequest servletRequest) {
        String response = "Servico de envio de confirmacao da transacao indisponivel";
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AmazonClientException.class)
    private ResponseEntity<ResponseError> handleWebServiceException(AmazonClientException e, WebRequest request, HttpServletRequest servletRequest) {
        String response = "Servico de e-mail indisponivel";
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalFormattingException.class)
    private ResponseEntity<ResponseError> handleApiRestExceptions(IllegalFormattingException e, WebRequest request, HttpServletRequest servletRequest){
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    private ResponseEntity<ResponseError> handleRestApiException(RestException e, WebRequest request, HttpServletRequest servletRequest){
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, e.getMessage(), servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.NOT_FOUND);
    }

}
