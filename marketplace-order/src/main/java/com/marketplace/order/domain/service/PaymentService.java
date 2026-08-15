package com.marketplace.order.domain.service;

import com.marketplace.order.domain.valueobject.Money;

public interface PaymentService {

    String processPayment(Long userId, Money amount, String paymentMethod, String paymentReference);

    void refundPayment(String paymentReference, Money amount, String reason);
}