package com.bank.bankingservice.domain.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
public class PaymentOrder {

    private String id;
    private String externalId;
    private String debtorIban;
    private String creditorIban;
    private BigDecimal amount;
    private String currency;
    private String remittanceInfo;
    private LocalDate requestedExecutionDate;

    private String status;
    private OffsetDateTime lastUpdate;
}
