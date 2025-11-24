package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreatePaymentOrderUseCase {

    private final PaymentOrderRepository repository;
    private final PaymentOrderMapper mapper;

    public Mono<PaymentOrderInitiationResponse> execute(PaymentOrderInitiationRequest request) {
        PaymentOrder domain = mapper.toDomain(request);
        domain.setStatus("INITIATED");
        return repository.save(domain)
                .map(mapper::toInitiationResponse);
    }
}
