package com.example.notification.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class SmsGatewayClient {

    private final RestClient restClient;

    public SmsGatewayClient(RestClient.Builder builder, @Value("${services.sms-gateway.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public boolean send(String phone, String text) {
        try {
            restClient.post()
                    .uri("/messages")
                    .body(new SmsMessage(phone, text))
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }

    record SmsMessage(String to, String text) {
    }
}
