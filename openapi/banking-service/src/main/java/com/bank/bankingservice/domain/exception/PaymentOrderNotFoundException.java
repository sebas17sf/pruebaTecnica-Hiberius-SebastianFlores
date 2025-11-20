package com.bank.bankingservice.domain.exception;

public class PaymentOrderNotFoundException extends RuntimeException {
    public PaymentOrderNotFoundException(String id) {
        super("Payment order not found: " + id);
    }
}
