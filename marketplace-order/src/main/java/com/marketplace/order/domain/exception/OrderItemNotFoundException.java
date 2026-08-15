package com.marketplace.order.domain.exception;

public class OrderItemNotFoundException extends RuntimeException {

    public OrderItemNotFoundException(Long itemId) {
        super("Order item not found with ID: " + itemId);
    }
}