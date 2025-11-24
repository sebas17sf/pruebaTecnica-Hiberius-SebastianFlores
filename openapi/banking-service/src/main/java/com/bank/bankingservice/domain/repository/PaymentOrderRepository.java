package com.bank.bankingservice.domain.repository;

import com.bank.bankingservice.domain.model.PaymentOrder;

import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PaymentOrderRepository {

    Mono<PaymentOrder> save(PaymentOrder order);

    Mono<Optional<PaymentOrder>> findById(String id);
}
