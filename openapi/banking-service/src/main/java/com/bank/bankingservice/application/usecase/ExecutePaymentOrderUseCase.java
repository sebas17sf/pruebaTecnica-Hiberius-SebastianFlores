package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Service
public class ExecutePaymentOrderUseCase {

    private final PaymentOrderRepository paymentOrderRepository;

    public ExecutePaymentOrderUseCase(PaymentOrderRepository paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    public Mono<PaymentOrder> execute(String paymentOrderId, String executionReference) {
        return paymentOrderRepository.findById(paymentOrderId)
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.error(() -> new PaymentOrderNotFoundException(
                                "Payment order not found: " + paymentOrderId
                        ))))
                .flatMap(existing -> {
                    if ("PENDING".equals(existing.getStatus())) {
                        PaymentOrder executed = existing.toBuilder()
                                .status("EXECUTED")
                                .lastUpdate(OffsetDateTime.now())
                                .build();

                        return paymentOrderRepository.save(executed);
                    }
                    return Mono.just(existing);
                });
    }
}
