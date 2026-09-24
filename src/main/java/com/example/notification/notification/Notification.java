package com.example.notification.notification;

import java.time.Instant;

public record Notification(Long customerId, String event, String channel, String message, boolean delivered, Instant sentAt) {
}
