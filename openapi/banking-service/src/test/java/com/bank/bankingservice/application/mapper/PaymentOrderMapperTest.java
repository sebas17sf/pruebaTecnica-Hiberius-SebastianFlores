package com.bank.bankingservice.application.mapper;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PaymentOrderMapper.
 */
class PaymentOrderMapperTest {

    private PaymentOrderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentOrderMapper();
    }

    @Test
    void testToDomain_WithAllFields() {
        // Arrange
        PaymentOrderInitiationRequest request = new PaymentOrderInitiationRequest();
        request.setExternalId("EXT-123");
        request.setDebtorIban("ES9121540011270123456789");
        request.setCreditorIban("FR1420041010050500013M02606");
        request.setAmount(new BigDecimal("1500.50"));
        request.setCurrency("EUR");
        request.setRemittanceInfo("Invoice #123");
        request.setRequestedExecutionDate(LocalDate.now().plusDays(1));

        // Act
        PaymentOrder domain = mapper.toDomain(request);

        // Assert
        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertEquals("EXT-123", domain.getExternalId());
        assertEquals("ES9121540011270123456789", domain.getDebtorIban());
        assertEquals("FR1420041010050500013M02606", domain.getCreditorIban());
        assertEquals(new BigDecimal("1500.50"), domain.getAmount());
        assertEquals("EUR", domain.getCurrency());
        assertEquals("Invoice #123", domain.getRemittanceInfo());
        assertEquals("INITIATED", domain.getStatus());
        assertNotNull(domain.getLastUpdate());
    }

    @Test
    void testToDomain_WithoutRemittanceInfo() {
        // Arrange
        PaymentOrderInitiationRequest request = new PaymentOrderInitiationRequest();
        request.setExternalId("EXT-456");
        request.setDebtorIban("ES9121540011270123456789");
        request.setCreditorIban("FR1420041010050500013M02606");
        request.setAmount(new BigDecimal("500.00"));
        request.setCurrency("EUR");
        request.setRemittanceInfo(null);
        request.setRequestedExecutionDate(LocalDate.now().plusDays(1));

        // Act
        PaymentOrder domain = mapper.toDomain(request);

        // Assert
        assertNotNull(domain);
        assertNull(domain.getRemittanceInfo());
    }

    @Test
    void testToInitiationResponse() {
        // Arrange
        PaymentOrder domain = PaymentOrder.builder()
                .id("PO-2025-000001")
                .externalId("EXT-789")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("2000.00"))
                .currency("EUR")
                .status("INITIATED")
                .lastUpdate(OffsetDateTime.now())
                .build();

        // Act
        PaymentOrderInitiationResponse response = mapper.toInitiationResponse(domain);

        // Assert
        assertNotNull(response);
        assertEquals("PO-2025-000001", response.getPaymentOrderId());
        assertEquals(PaymentOrderInitiationResponse.StatusEnum.INITIATED, response.getStatus());
    }

    @Test
    void testToStatus() {
        // Arrange
        PaymentOrder domain = PaymentOrder.builder()
                .id("PO-2025-000002")
                .status("PENDING")
                .lastUpdate(OffsetDateTime.now())
                .build();

        // Act
        PaymentOrderStatusResponse response = mapper.toStatus(domain);

        // Assert
        assertNotNull(response);
        assertEquals("PO-2025-000002", response.getPaymentOrderId());
        assertEquals(PaymentOrderStatusResponse.StatusEnum.INITIATED, response.getStatus());
        assertEquals(domain.getLastUpdate(), response.getLastUpdate());
    }

    @Test
    void testToBian() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        PaymentOrder domain = PaymentOrder.builder()
                .id("PO-2025-000003")
                .externalId("EXT-999")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("3000.00"))
                .currency("EUR")
                .remittanceInfo("Invoice #999")
                .requestedExecutionDate(LocalDate.now().plusDays(5))
                .status("PENDING")
                .lastUpdate(now)
                .build();

        // Act
        com.bank.bankingservice.openapi.model.PaymentOrder dto = mapper.toBian(domain);

        // Assert
        assertNotNull(dto);
        assertEquals("PO-2025-000003", dto.getPaymentOrderId());
        assertEquals("EXT-999", dto.getExternalId());
        assertEquals("ES9121540011270123456789", dto.getDebtorIban());
        assertEquals("FR1420041010050500013M02606", dto.getCreditorIban());
        assertEquals(new BigDecimal("3000.00"), dto.getAmount());
        assertEquals("EUR", dto.getCurrency());
        assertEquals("Invoice #999", dto.getRemittanceInfo().get());
        assertEquals(LocalDate.now().plusDays(5), dto.getRequestedExecutionDate());
        assertEquals("PENDING", dto.getStatus());
        assertEquals(now, dto.getLastUpdate());
    }

    @Test
    void testToBian_WithNullRemittanceInfo() {
        // Arrange
        PaymentOrder domain = PaymentOrder.builder()
                .id("PO-2025-000004")
                .externalId("EXT-000")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1000.00"))
                .currency("EUR")
                .remittanceInfo(null)
                .requestedExecutionDate(LocalDate.now().plusDays(2))
                .status("EXECUTED")
                .lastUpdate(OffsetDateTime.now())
                .build();

        // Act
        com.bank.bankingservice.openapi.model.PaymentOrder dto = mapper.toBian(domain);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getRemittanceInfo());
    }
}
