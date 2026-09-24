package com.example.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "${services.account-service.url}", path = "/accounts")
public interface AccountClient {

    @GetMapping("/{id}")
    AccountOwner get(@PathVariable("id") Long id);
}
