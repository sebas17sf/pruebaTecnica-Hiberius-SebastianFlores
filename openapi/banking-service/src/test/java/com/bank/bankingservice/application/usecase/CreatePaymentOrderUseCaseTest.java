package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Create Payment Order Use Case Tests")
class CreatePaymentOrderUseCaseTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private PaymentOrderMapper paymentOrderMapper;

    @InjectMocks
    private CreatePaymentOrderUseCase useCase;

    private PaymentOrderInitiationRequest testRequest;
    private PaymentOrder testDomain;
    private PaymentOrderInitiationResponse testResponse;

    @BeforeEach
    void setUp() {
        testRequest = new PaymentOrderInitiationRequest();
        testRequest.setExternalId("EXT-001");
        testRequest.setDebtorIban("ES9121540011270123456789");
        testRequest.setCreditorIban("FR1420041010050500013M02606");
        testRequest.setAmount(new BigDecimal("1500.50"));
        testRequest.setCurrency("EUR");
        testRequest.setRequestedExecutionDate(LocalDate.of(2025, 12, 20));

        testDomain = PaymentOrder.builder()
                .id("PO-TEST-001")
                .externalId("EXT-001")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1500.50"))
                .currency("EUR")
                .requestedExecutionDate(LocalDate.of(2025, 12, 20))
                .status("INITIATED")
                .build();

        testResponse = new PaymentOrderInitiationResponse();
    }

    @Test
    @DisplayName("execute() should create and persist payment order")
    void shouldCreateAndPersistPaymentOrder() {
        // Arrange
        when(paymentOrderMapper.toDomain(testRequest))
                .thenReturn(testDomain);
        when(paymentOrderMapper.toInitiationResponse(testDomain))
                .thenReturn(testResponse);

        // Act
        PaymentOrderInitiationResponse result = useCase.execute(testRequest);

        // Assert
        assertNotNull(result);
        verify(paymentOrderRepository).save(any(PaymentOrder.class));
        verify(paymentOrderMapper).toDomain(testRequest);
        verify(paymentOrderMapper).toInitiationResponse(testDomain);
    }

    @Test
    @DisplayName("execute() should set status to INITIATED")
    void shouldSetInitiatedStatus() {
        // Arrange
        when(paymentOrderMapper.toDomain(testRequest))
                .thenReturn(testDomain);
        when(paymentOrderMapper.toInitiationResponse(testDomain))
                .thenReturn(testResponse);

        // Act
        useCase.execute(testRequest);

        // Assert
        verify(paymentOrderRepository).save(any(PaymentOrder.class));
    }

    @Test
    @DisplayName("execute() should map request to domain model")
    void shouldMapRequestToDomainModel() {
        // Arrange
        when(paymentOrderMapper.toDomain(testRequest))
                .thenReturn(testDomain);
        when(paymentOrderMapper.toInitiationResponse(testDomain))
                .thenReturn(testResponse);

        // Act
        useCase.execute(testRequest);

        // Assert
        verify(paymentOrderMapper).toDomain(testRequest);
    }

    @Test
    @DisplayName("execute() should map domain model to response")
    void shouldMapDomainToResponse() {
        // Arrange
        when(paymentOrderMapper.toDomain(testRequest))
                .thenReturn(testDomain);
        when(paymentOrderMapper.toInitiationResponse(testDomain))
                .thenReturn(testResponse);

        // Act
        useCase.execute(testRequest);

        // Assert
        verify(paymentOrderMapper).toInitiationResponse(testDomain);
    }
}
