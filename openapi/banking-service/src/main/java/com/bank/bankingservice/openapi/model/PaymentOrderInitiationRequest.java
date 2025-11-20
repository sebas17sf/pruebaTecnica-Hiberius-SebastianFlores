package com.bank.bankingservice.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;


/**
 * PaymentOrderInitiationRequest
 */
public class PaymentOrderInitiationRequest {

  private String externalId;

  private String debtorIban;

  private String creditorIban;

  private BigDecimal amount;

  private String currency;

  @Nullable
  private String remittanceInfo;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate requestedExecutionDate;

  public PaymentOrderInitiationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PaymentOrderInitiationRequest(String externalId, String debtorIban, String creditorIban, BigDecimal amount, String currency, LocalDate requestedExecutionDate) {
    this.externalId = externalId;
    this.debtorIban = debtorIban;
    this.creditorIban = creditorIban;
    this.amount = amount;
    this.currency = currency;
    this.requestedExecutionDate = requestedExecutionDate;
  }

  public PaymentOrderInitiationRequest externalId(String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * Get externalId
   * @return externalId
   */
  @NotNull 
  @Schema(name = "externalId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("externalId")
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public PaymentOrderInitiationRequest debtorIban(String debtorIban) {
    this.debtorIban = debtorIban;
    return this;
  }

  /**
   * Get debtorIban
   * @return debtorIban
   */
  @NotNull 
  @Schema(name = "debtorIban", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("debtorIban")
  public String getDebtorIban() {
    return debtorIban;
  }

  public void setDebtorIban(String debtorIban) {
    this.debtorIban = debtorIban;
  }

  public PaymentOrderInitiationRequest creditorIban(String creditorIban) {
    this.creditorIban = creditorIban;
    return this;
  }

  /**
   * Get creditorIban
   * @return creditorIban
   */
  @NotNull 
  @Schema(name = "creditorIban", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("creditorIban")
  public String getCreditorIban() {
    return creditorIban;
  }

  public void setCreditorIban(String creditorIban) {
    this.creditorIban = creditorIban;
  }

  public PaymentOrderInitiationRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull @Valid 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentOrderInitiationRequest currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * ISO 4217 currency code
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", description = "ISO 4217 currency code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public PaymentOrderInitiationRequest remittanceInfo(String remittanceInfo) {
    this.remittanceInfo = remittanceInfo;
    return this;
  }

  /**
   * Get remittanceInfo
   * @return remittanceInfo
   */
  @Nullable
  @Schema(name = "remittanceInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remittanceInfo")
  public String getRemittanceInfo() {
    return remittanceInfo;
  }

  public void setRemittanceInfo(String remittanceInfo) {
    this.remittanceInfo = remittanceInfo;
  }

  public PaymentOrderInitiationRequest requestedExecutionDate(LocalDate requestedExecutionDate) {
    this.requestedExecutionDate = requestedExecutionDate;
    return this;
  }

  /**
   * Get requestedExecutionDate
   * @return requestedExecutionDate
   */
  @NotNull @Valid 
  @Schema(name = "requestedExecutionDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requestedExecutionDate")
  public LocalDate getRequestedExecutionDate() {
    return requestedExecutionDate;
  }

  public void setRequestedExecutionDate(LocalDate requestedExecutionDate) {
    this.requestedExecutionDate = requestedExecutionDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentOrderInitiationRequest paymentOrderInitiationRequest = (PaymentOrderInitiationRequest) o;
    return Objects.equals(this.externalId, paymentOrderInitiationRequest.externalId) &&
        Objects.equals(this.debtorIban, paymentOrderInitiationRequest.debtorIban) &&
        Objects.equals(this.creditorIban, paymentOrderInitiationRequest.creditorIban) &&
        Objects.equals(this.amount, paymentOrderInitiationRequest.amount) &&
        Objects.equals(this.currency, paymentOrderInitiationRequest.currency) &&
        Objects.equals(this.remittanceInfo, paymentOrderInitiationRequest.remittanceInfo) &&
        Objects.equals(this.requestedExecutionDate, paymentOrderInitiationRequest.requestedExecutionDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalId, debtorIban, creditorIban, amount, currency, remittanceInfo, requestedExecutionDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentOrderInitiationRequest {\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    debtorIban: ").append(toIndentedString(debtorIban)).append("\n");
    sb.append("    creditorIban: ").append(toIndentedString(creditorIban)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    remittanceInfo: ").append(toIndentedString(remittanceInfo)).append("\n");
    sb.append("    requestedExecutionDate: ").append(toIndentedString(requestedExecutionDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

