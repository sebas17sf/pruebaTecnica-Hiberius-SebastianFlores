package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * PaymentOrder
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-18T22:49:19.373248300-05:00[America/Guayaquil]", comments = "Generator version: 7.17.0")
public class PaymentOrder {

  private @Nullable String paymentOrderId;

  private @Nullable String externalId;

  private @Nullable String debtorIban;

  private @Nullable String creditorIban;

  private @Nullable BigDecimal amount;

  private @Nullable String currency;

  private JsonNullable<String> remittanceInfo = JsonNullable.<String>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate requestedExecutionDate;

  private @Nullable String status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdate;

  public PaymentOrder paymentOrderId(@Nullable String paymentOrderId) {
    this.paymentOrderId = paymentOrderId;
    return this;
  }

  /**
   * Get paymentOrderId
   * @return paymentOrderId
   */
  
  @Schema(name = "paymentOrderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentOrderId")
  public @Nullable String getPaymentOrderId() {
    return paymentOrderId;
  }

  public void setPaymentOrderId(@Nullable String paymentOrderId) {
    this.paymentOrderId = paymentOrderId;
  }

  public PaymentOrder externalId(@Nullable String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * Get externalId
   * @return externalId
   */
  
  @Schema(name = "externalId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalId")
  public @Nullable String getExternalId() {
    return externalId;
  }

  public void setExternalId(@Nullable String externalId) {
    this.externalId = externalId;
  }

  public PaymentOrder debtorIban(@Nullable String debtorIban) {
    this.debtorIban = debtorIban;
    return this;
  }

  /**
   * Get debtorIban
   * @return debtorIban
   */
  
  @Schema(name = "debtorIban", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debtorIban")
  public @Nullable String getDebtorIban() {
    return debtorIban;
  }

  public void setDebtorIban(@Nullable String debtorIban) {
    this.debtorIban = debtorIban;
  }

  public PaymentOrder creditorIban(@Nullable String creditorIban) {
    this.creditorIban = creditorIban;
    return this;
  }

  /**
   * Get creditorIban
   * @return creditorIban
   */
  
  @Schema(name = "creditorIban", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creditorIban")
  public @Nullable String getCreditorIban() {
    return creditorIban;
  }

  public void setCreditorIban(@Nullable String creditorIban) {
    this.creditorIban = creditorIban;
  }

  public PaymentOrder amount(@Nullable BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @Valid 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(@Nullable BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentOrder currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public PaymentOrder remittanceInfo(String remittanceInfo) {
    this.remittanceInfo = JsonNullable.of(remittanceInfo);
    return this;
  }

  /**
   * Get remittanceInfo
   * @return remittanceInfo
   */
  
  @Schema(name = "remittanceInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remittanceInfo")
  public JsonNullable<String> getRemittanceInfo() {
    return remittanceInfo;
  }

  public void setRemittanceInfo(JsonNullable<String> remittanceInfo) {
    this.remittanceInfo = remittanceInfo;
  }

  public PaymentOrder requestedExecutionDate(@Nullable LocalDate requestedExecutionDate) {
    this.requestedExecutionDate = requestedExecutionDate;
    return this;
  }

  /**
   * Get requestedExecutionDate
   * @return requestedExecutionDate
   */
  @Valid 
  @Schema(name = "requestedExecutionDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedExecutionDate")
  public @Nullable LocalDate getRequestedExecutionDate() {
    return requestedExecutionDate;
  }

  public void setRequestedExecutionDate(@Nullable LocalDate requestedExecutionDate) {
    this.requestedExecutionDate = requestedExecutionDate;
  }

  public PaymentOrder status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public PaymentOrder lastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  /**
   * Get lastUpdate
   * @return lastUpdate
   */
  @Valid 
  @Schema(name = "lastUpdate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdate")
  public @Nullable OffsetDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentOrder paymentOrder = (PaymentOrder) o;
    return Objects.equals(this.paymentOrderId, paymentOrder.paymentOrderId) &&
        Objects.equals(this.externalId, paymentOrder.externalId) &&
        Objects.equals(this.debtorIban, paymentOrder.debtorIban) &&
        Objects.equals(this.creditorIban, paymentOrder.creditorIban) &&
        Objects.equals(this.amount, paymentOrder.amount) &&
        Objects.equals(this.currency, paymentOrder.currency) &&
        equalsNullable(this.remittanceInfo, paymentOrder.remittanceInfo) &&
        Objects.equals(this.requestedExecutionDate, paymentOrder.requestedExecutionDate) &&
        Objects.equals(this.status, paymentOrder.status) &&
        Objects.equals(this.lastUpdate, paymentOrder.lastUpdate);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentOrderId, externalId, debtorIban, creditorIban, amount, currency, hashCodeNullable(remittanceInfo), requestedExecutionDate, status, lastUpdate);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentOrder {\n");
    sb.append("    paymentOrderId: ").append(toIndentedString(paymentOrderId)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    debtorIban: ").append(toIndentedString(debtorIban)).append("\n");
    sb.append("    creditorIban: ").append(toIndentedString(creditorIban)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    remittanceInfo: ").append(toIndentedString(remittanceInfo)).append("\n");
    sb.append("    requestedExecutionDate: ").append(toIndentedString(requestedExecutionDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
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

