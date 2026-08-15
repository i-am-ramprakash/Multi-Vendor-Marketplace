package com.marketplace.order.infrastructure.service;

import com.marketplace.order.domain.service.PaymentService;
import com.marketplace.order.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String processPayment(Long userId, Money amount, String paymentMethod, String paymentReference) {
        // TODO: Integrate with Payment Gateway (Stripe, PayPal, etc.)
        log.debug("Processing payment for user: {}, amount: {} {}, method: {}",
            userId, amount.getAmount(), amount.getCurrency(), paymentMethod);

        // Simulate payment processing
        String reference = paymentReference != null ? paymentReference : UUID.randomUUID().toString();
        log.info("Payment processed successfully. Reference: {}", reference);
        return reference;
    }

    @Override
    public void refundPayment(String paymentReference, Money amount, String reason) {
        // TODO: Integrate with Payment Gateway
        log.debug("Processing refund for reference: {}, amount: {} {}, reason: {}",
            paymentReference, amount.getAmount(), amount.getCurrency(), reason);

        log.info("Refund processed successfully. Reference: {}", paymentReference);
    }
}