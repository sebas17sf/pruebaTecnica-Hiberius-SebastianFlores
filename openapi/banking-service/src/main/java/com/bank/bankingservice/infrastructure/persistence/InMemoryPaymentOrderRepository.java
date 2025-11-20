package com.bank.bankingservice.infrastructure.persistence;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentOrderRepository implements PaymentOrderRepository {

    private final ConcurrentHashMap<String, PaymentOrder> db = new ConcurrentHashMap<>();

    @Override
    public PaymentOrder save(PaymentOrder order) {
        db.put(order.getId(), order);
        return order;
    }

    @Override
    public Optional<PaymentOrder> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }
}
