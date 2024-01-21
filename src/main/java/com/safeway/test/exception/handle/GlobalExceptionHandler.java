package com.safeway.test.exception.handle;

import com.amazonaws.AmazonClientException;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

    private org.springframework.http.HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    @ExceptionHandler({WebhookException.class, UnknownHostException.class})
    private ResponseEntity<ResponseError> handleWebhookException(WebhookException e, WebRequest request, HttpServletRequest servletRequest) {
        String response = "Servico de envio de confirmacao da transacao indisponivel!";
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(AmazonClientException.class)
    private ResponseEntity<ResponseError> handleWebServiceException(AmazonClientException e, WebRequest request, HttpServletRequest servletRequest) {
        String response = "Servico de e-mail indisponivel!";
        ResponseError responseError = new ResponseError(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE, response, servletRequest.getRequestURI());
        return new ResponseEntity<>(responseError, headers(), HttpStatus.SERVICE_UNAVAILABLE);
    }

//    @ExceptionHandler(JdbcSQLDataException)

}
