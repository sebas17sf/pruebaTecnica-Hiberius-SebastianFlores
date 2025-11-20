package com.bank.bankingservice.application.mapper;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class PaymentOrderMapper {

    // DOMAIN ← REQUEST
    public PaymentOrder toDomain(PaymentOrderInitiationRequest dto) {
        return PaymentOrder.builder()
                .id(UUID.randomUUID().toString())
                .externalId(dto.getExternalId())
                .debtorIban(dto.getDebtorIban())
                .creditorIban(dto.getCreditorIban())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .remittanceInfo(dto.getRemittanceInfo())
                .requestedExecutionDate(dto.getRequestedExecutionDate())
                .status("INITIATED")
                .lastUpdate(OffsetDateTime.now())
                .build();
    }

    // RESPONSE INIT
    public PaymentOrderInitiationResponse toInitiationResponse(PaymentOrder domain) {
        return new PaymentOrderInitiationResponse()
                .paymentOrderId(domain.getId())
                .status(PaymentOrderInitiationResponse.StatusEnum.INITIATED);
    }

    // RESPONSE STATUS
    public PaymentOrderStatusResponse toStatus(PaymentOrder domain) {
        return new PaymentOrderStatusResponse()
                .paymentOrderId(domain.getId())
                .status(PaymentOrderStatusResponse.StatusEnum.INITIATED)
                .lastUpdate(domain.getLastUpdate());
    }

    // BIAN FULL OBJECT
    public com.bank.bankingservice.openapi.model.PaymentOrder toBian(PaymentOrder domain) {

        // Usamos FQN: NO hay import para evitar conflicto
        com.bank.bankingservice.openapi.model.PaymentOrder dto =
                new com.bank.bankingservice.openapi.model.PaymentOrder();

        dto.setPaymentOrderId(domain.getId());
        dto.setExternalId(domain.getExternalId());
        dto.setDebtorIban(domain.getDebtorIban());
        dto.setCreditorIban(domain.getCreditorIban());
        dto.setAmount(domain.getAmount());
        dto.setCurrency(domain.getCurrency());

        dto.setRemittanceInfo(
                domain.getRemittanceInfo() != null
                        ? JsonNullable.of(domain.getRemittanceInfo())
                        : JsonNullable.undefined()
        );

        dto.setRequestedExecutionDate(domain.getRequestedExecutionDate());
        dto.setStatus(domain.getStatus());
        dto.setLastUpdate(domain.getLastUpdate());

        return dto;
    }
}
