package com.bank.bankingservice.openapi.model;

import java.time.OffsetDateTime;
import jakarta.annotation.Generated;

@Generated(
        value = "manual",
        comments = "DTO created manually because OpenAPI does not generate it"
)
public class PaymentOrderDTO {

    private String paymentOrderId;
    private String externalId;
    private String debtorIban;
    private String creditorIban;
    private Double amount;
    private String currency;
    private String remittanceInfo;
    private OffsetDateTime requestedExecutionDate;
    private String status;
    private OffsetDateTime lastUpdate;

    public String getPaymentOrderId() { return paymentOrderId; }
    public void setPaymentOrderId(String paymentOrderId) { this.paymentOrderId = paymentOrderId; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getDebtorIban() { return debtorIban; }
    public void setDebtorIban(String debtorIban) { this.debtorIban = debtorIban; }

    public String getCreditorIban() { return creditorIban; }
    public void setCreditorIban(String creditorIban) { this.creditorIban = creditorIban; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getRemittanceInfo() { return remittanceInfo; }
    public void setRemittanceInfo(String remittanceInfo) { this.remittanceInfo = remittanceInfo; }

    public OffsetDateTime getRequestedExecutionDate() { return requestedExecutionDate; }
    public void setRequestedExecutionDate(OffsetDateTime requestedExecutionDate) { this.requestedExecutionDate = requestedExecutionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public OffsetDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(OffsetDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
