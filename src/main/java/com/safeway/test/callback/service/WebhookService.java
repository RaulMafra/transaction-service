package com.safeway.test.callback.service;

import com.safeway.test.dtos.ResponseCompanyDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Setter
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    @Value(value = "${webhook.absolute.url}")
    private String url;

    public void sendInfoTransaction(ResponseCompanyDTO responseCompanyDTO){
        HttpEntity<ResponseCompanyDTO> request = new HttpEntity<>(responseCompanyDTO);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

}
