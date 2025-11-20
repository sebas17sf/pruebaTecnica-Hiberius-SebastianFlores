package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.infrastructure.soap.LegacyPaymentSoapSimulator;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class GetPaymentOrderStatusUseCase {

    private final PaymentOrderRepository repository;
    private final LegacyPaymentSoapSimulator soap;

    public PaymentOrderStatusResponse execute(String id) {

        var domain = repository.findById(id)
                .orElseThrow(() -> new PaymentOrderNotFoundException(id));

        String legacyStatus = soap.nextStatus(domain.getStatus());
        String bianStatus = mapLegacyStatusToBian(legacyStatus);
        
        domain.setStatus(bianStatus);
        domain.setLastUpdate(OffsetDateTime.now());

        repository.save(domain);

        return new PaymentOrderStatusResponse()
                .paymentOrderId(domain.getId())
                .status(PaymentOrderStatusResponse.StatusEnum.fromValue(bianStatus))
                .lastUpdate(domain.getLastUpdate());
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

