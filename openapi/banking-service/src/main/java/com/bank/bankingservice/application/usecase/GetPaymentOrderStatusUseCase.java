package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.infrastructure.soap.LegacyPaymentSoapSimulator;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class GetPaymentOrderStatusUseCase {

    private final PaymentOrderRepository repository;
    private final LegacyPaymentSoapSimulator soap;

    public Mono<PaymentOrderStatusResponse> execute(String id) {
        return repository.findById(id)
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.error(() -> new PaymentOrderNotFoundException(id))))
                .flatMap(domain -> {
                    String legacyStatus = soap.nextStatus(domain.getStatus());
                    String bianStatus = mapLegacyStatusToBian(legacyStatus);

                    domain.setStatus(bianStatus);
                    domain.setLastUpdate(OffsetDateTime.now());

                    return repository.save(domain)
                            .map(saved -> new PaymentOrderStatusResponse()
                                    .paymentOrderId(saved.getId())
                                    .status(PaymentOrderStatusResponse.StatusEnum.fromValue(bianStatus))
                                    .lastUpdate(saved.getLastUpdate()));
                });
    }

   
    private String mapLegacyStatusToBian(String legacyStatus) {
        return switch (legacyStatus) {
            case "INITIATED" -> "INITIATED";
            case "PROCESSING" -> "PENDING";    
            case "EXECUTED" -> "EXECUTED";
            default -> "PENDING";
        };
    }
}

