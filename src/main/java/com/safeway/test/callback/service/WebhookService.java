package com.safeway.test.callback.service;

import com.safeway.test.dtos.ResponseCompanyDTO;
import com.safeway.test.exception.exceptions.RestException;
import com.safeway.test.exception.exceptions.WebhookException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.*;

@Service
@Setter
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    @Value(value = "${webhook.absolute.url}")
    private String url;

    public void sendInfoTransaction(ResponseCompanyDTO responseCompanyDTO) {
        HttpEntity<ResponseCompanyDTO> request = new HttpEntity<>(responseCompanyDTO);
        try {
            URL uri = new URL(url);
            uri.openConnection().connect();
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (RestClientException | UnknownHostException e) {
            throw new WebhookException(e.getMessage(), e.getCause());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}