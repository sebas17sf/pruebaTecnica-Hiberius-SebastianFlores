package com.bank.bankingservice.domain.repository;

import com.bank.bankingservice.domain.model.PaymentOrder;

import java.util.Optional;

public interface PaymentOrderRepository {

    PaymentOrder save(PaymentOrder order);

    Optional<PaymentOrder> findById(String id);
}
