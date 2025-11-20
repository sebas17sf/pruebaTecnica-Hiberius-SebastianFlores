package com.bank.bankingservice.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;


/**
 * PaymentOrderStatusResponse
 */
public class PaymentOrderStatusResponse {

  private @Nullable String paymentOrderId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    INITIATED("INITIATED"),
    
    VALIDATED("VALIDATED"),
    
    PENDING("PENDING"),
    
    EXECUTED("EXECUTED"),
    
    REJECTED("REJECTED");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdate;

  public PaymentOrderStatusResponse paymentOrderId(@Nullable String paymentOrderId) {
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

  public PaymentOrderStatusResponse status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public PaymentOrderStatusResponse lastUpdate(@Nullable OffsetDateTime lastUpdate) {
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
    PaymentOrderStatusResponse paymentOrderStatusResponse = (PaymentOrderStatusResponse) o;
    return Objects.equals(this.paymentOrderId, paymentOrderStatusResponse.paymentOrderId) &&
        Objects.equals(this.status, paymentOrderStatusResponse.status) &&
        Objects.equals(this.lastUpdate, paymentOrderStatusResponse.lastUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentOrderId, status, lastUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentOrderStatusResponse {\n");
    sb.append("    paymentOrderId: ").append(toIndentedString(paymentOrderId)).append("\n");
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

