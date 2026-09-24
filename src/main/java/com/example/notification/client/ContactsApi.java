package com.example.notification.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/contacts")
public interface ContactsApi {

    @GetExchange("/{customerId}")
    Contact get(@PathVariable Long customerId);
}
