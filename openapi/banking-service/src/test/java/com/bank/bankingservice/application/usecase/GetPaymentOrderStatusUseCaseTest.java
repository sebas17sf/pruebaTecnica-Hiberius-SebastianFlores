package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.infrastructure.soap.LegacyPaymentSoapSimulator;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GetPaymentOrderStatusUseCase.
 */
@ExtendWith(MockitoExtension.class)
class GetPaymentOrderStatusUseCaseTest {

    @Mock
    private PaymentOrderRepository repository;

    @Mock
    private LegacyPaymentSoapSimulator soap;

    private GetPaymentOrderStatusUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetPaymentOrderStatusUseCase(repository, soap);
    }

    @Test
    void testExecute_FromInitiatedToPending() {
        // Arrange
        String paymentOrderId = "PO-2025-000001";
        PaymentOrder domain = PaymentOrder.builder()
                .id(paymentOrderId)
                .status("INITIATED")
                .lastUpdate(OffsetDateTime.now())
                .build();

        when(repository.findById(paymentOrderId)).thenReturn(Mono.just(Optional.of(domain)));
        when(soap.nextStatus("INITIATED")).thenReturn("PROCESSING");
        when(repository.save(any(PaymentOrder.class))).thenReturn(Mono.just(domain));

        // Act
        PaymentOrderStatusResponse response = useCase.execute(paymentOrderId).block();

        // Assert
        assertNotNull(response);
        assertEquals(paymentOrderId, response.getPaymentOrderId());
        assertEquals(PaymentOrderStatusResponse.StatusEnum.PENDING, response.getStatus());
        
        // Verify interactions
        verify(repository, times(1)).findById(paymentOrderId);
        verify(soap, times(1)).nextStatus("INITIATED");
        verify(repository, times(1)).save(any(PaymentOrder.class));
    }

    @Test
    void testExecute_FromPendingToExecuted() {
        // Arrange
        String paymentOrderId = "PO-2025-000002";
        PaymentOrder domain = PaymentOrder.builder()
                .id(paymentOrderId)
                .status("PENDING")
                .lastUpdate(OffsetDateTime.now())
                .build();

        when(repository.findById(paymentOrderId)).thenReturn(Mono.just(Optional.of(domain)));
        when(soap.nextStatus("PENDING")).thenReturn("EXECUTED");
        when(repository.save(any(PaymentOrder.class))).thenReturn(Mono.just(domain));

        // Act
        PaymentOrderStatusResponse response = useCase.execute(paymentOrderId).block();

        // Assert
        assertNotNull(response);
        assertEquals(paymentOrderId, response.getPaymentOrderId());
        assertEquals(PaymentOrderStatusResponse.StatusEnum.EXECUTED, response.getStatus());
        
        verify(repository, times(1)).save(any(PaymentOrder.class));
    }

    @Test
    void testExecute_PaymentOrderNotFound() {
        // Arrange
        String paymentOrderId = "PO-INVALID";
        when(repository.findById(paymentOrderId)).thenReturn(Mono.just(Optional.empty()));

        // Act & Assert
        assertThrows(PaymentOrderNotFoundException.class, () -> 
            useCase.execute(paymentOrderId).block());
        
        verify(repository, times(1)).findById(paymentOrderId);
        verify(soap, never()).nextStatus(anyString());
    }

    @Test
    void testExecute_UpdatesLastUpdate() {
        // Arrange
        String paymentOrderId = "PO-2025-000003";
        OffsetDateTime originalTime = OffsetDateTime.now().minusHours(1);
        PaymentOrder domain = PaymentOrder.builder()
                .id(paymentOrderId)
                .status("INITIATED")
                .lastUpdate(originalTime)
                .build();

        when(repository.findById(paymentOrderId)).thenReturn(Mono.just(Optional.of(domain)));
        when(soap.nextStatus("INITIATED")).thenReturn("PROCESSING");
        when(repository.save(any(PaymentOrder.class))).thenReturn(Mono.just(domain));

        // Act
        useCase.execute(paymentOrderId).block();

        // Assert
        verify(repository, times(1)).save(argThat(order -> 
            order.getLastUpdate().isAfter(originalTime)
        ));
    }
}
