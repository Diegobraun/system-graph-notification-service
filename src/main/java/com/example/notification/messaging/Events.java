package com.example.notification.messaging;

import java.math.BigDecimal;

public final class Events {

    public record AccountOpened(Long accountId, Long customerId) {
    }

    public record KycApproved(Long customerId, Long accountId, String riskTier) {
    }

    public record LoanDisbursed(String loanId, Long accountId, BigDecimal amount) {
    }

    public record PaymentCompleted(String paymentId, Long accountId, BigDecimal amount, String pixKey) {
    }

    public record InvestmentApplied(String investmentId, Long accountId, Long customerId, String productCode, BigDecimal amount) {
    }

    public record FraudAlert(String paymentId, Long accountId, BigDecimal amount, String decision) {
    }

    private Events() {
    }
}
