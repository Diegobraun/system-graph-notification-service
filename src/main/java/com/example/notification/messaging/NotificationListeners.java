package com.example.notification.messaging;

import com.example.notification.notification.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListeners {

    private final NotificationService service;

    public NotificationListeners(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(topics = "${app.topics.account-opened}")
    public void onAccountOpened(Events.AccountOpened event) {
        service.notifyCustomer(event.customerId(), "account-opened", "Sua conta " + event.accountId() + " foi aberta e está em análise.");
    }

    @KafkaListener(topics = "${app.topics.customer-kyc-approved}")
    public void onKycApproved(Events.KycApproved event) {
        service.notifyCustomer(event.customerId(), "customer-kyc-approved", "Cadastro aprovado. Sua conta " + event.accountId() + " está ativa.");
    }

    @KafkaListener(topics = "${app.topics.loan-disbursed}")
    public void onLoanDisbursed(Events.LoanDisbursed event) {
        service.notifyAccountHolder(event.accountId(), "loan-disbursed", "Empréstimo de R$ " + event.amount() + " creditado.");
    }

    @KafkaListener(topics = "${app.topics.payment-completed}")
    public void onPaymentCompleted(Events.PaymentCompleted event) {
        service.notifyAccountHolder(event.accountId(), "payment-completed", "Pix de R$ " + event.amount() + " enviado para " + event.pixKey() + ".");
    }

    @KafkaListener(topics = "${app.topics.investment-applied}")
    public void onInvestmentApplied(Events.InvestmentApplied event) {
        service.notifyCustomer(event.customerId(), "investment-applied", "Aplicação de R$ " + event.amount() + " em " + event.productCode() + " confirmada.");
    }

    @KafkaListener(topics = "${app.topics.fraud-alert}")
    public void onFraudAlert(Events.FraudAlert event) {
        service.notifyAccountHolder(event.accountId(), "fraud-alert", "Identificamos um Pix de R$ " + event.amount() + " fora do seu padrão. Confirme no app.");
    }
}
