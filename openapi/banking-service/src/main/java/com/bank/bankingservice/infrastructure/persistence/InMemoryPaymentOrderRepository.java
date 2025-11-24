package com.bank.bankingservice.infrastructure.persistence;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentOrderRepository implements PaymentOrderRepository {

    private final ConcurrentHashMap<String, PaymentOrder> db = new ConcurrentHashMap<>();

    @Override
    public Mono<PaymentOrder> save(PaymentOrder order) {
        return Mono.fromCallable(() -> {
            db.put(order.getId(), order);
            return order;
        });
    }

    @Override
    public Mono<Optional<PaymentOrder>> findById(String id) {
        return Mono.fromCallable(() -> Optional.ofNullable(db.get(id)));
    }
}
