package com.example.notification.notification;

import com.example.notification.client.AccountClient;
import com.example.notification.client.AccountOwner;
import com.example.notification.client.Contact;
import com.example.notification.client.ContactsApi;
import com.example.notification.client.SmsGatewayClient;
import feign.FeignException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final AccountClient accounts;
    private final ContactsApi contacts;
    private final SmsGatewayClient sms;
    private final Map<Long, List<Notification>> sent = new ConcurrentHashMap<>();

    public NotificationService(AccountClient accounts, ContactsApi contacts, SmsGatewayClient sms) {
        this.accounts = accounts;
        this.contacts = contacts;
        this.sms = sms;
    }

    public void notifyAccountHolder(Long accountId, String event, String message) {
        AccountOwner owner;
        try {
            owner = accounts.get(accountId);
        } catch (FeignException e) {
            log.warn("Skipping {} notification for account {}: {}", event, accountId, e.status());
            return;
        }
        notifyCustomer(owner.customerId(), event, message);
    }

    public void notifyCustomer(Long customerId, String event, String message) {
        Contact contact = contact(customerId);
        String channel;
        boolean delivered;
        if (contact != null && contact.phone() != null) {
            channel = "sms";
            delivered = sms.send(contact.phone(), message);
        } else if (contact != null && contact.email() != null) {
            channel = "email";
            delivered = true;
        } else {
            channel = "app";
            delivered = true;
        }
        sent.computeIfAbsent(customerId, id -> new CopyOnWriteArrayList<>())
                .add(new Notification(customerId, event, channel, message, delivered, Instant.now()));
    }

    public List<Notification> byCustomer(Long customerId) {
        return sent.getOrDefault(customerId, List.of());
    }

    private Contact contact(Long customerId) {
        try {
            return contacts.get(customerId);
        } catch (RestClientException e) {
            return null;
        }
    }
}
