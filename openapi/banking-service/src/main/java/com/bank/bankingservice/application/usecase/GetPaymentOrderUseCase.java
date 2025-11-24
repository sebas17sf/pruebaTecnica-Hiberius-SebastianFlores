package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetPaymentOrderUseCase {

    private final PaymentOrderRepository repository;
    private final PaymentOrderMapper mapper;

    public Mono<PaymentOrder> execute(String id) {
        return repository.findById(id)
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.error(() -> new PaymentOrderNotFoundException("Payment order not found: " + id))));
    }
}
