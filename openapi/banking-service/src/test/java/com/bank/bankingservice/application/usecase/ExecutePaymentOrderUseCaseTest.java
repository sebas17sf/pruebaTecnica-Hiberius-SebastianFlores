package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Execute Payment Order Use Case Tests")
class ExecutePaymentOrderUseCaseTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @InjectMocks
    private ExecutePaymentOrderUseCase useCase;

    private PaymentOrder pendingOrder;

    @BeforeEach
    void setUp() {
        pendingOrder = PaymentOrder.builder()
                .id("PO-EXEC-001")
                .externalId("EXT-EXEC-001")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("2000.00"))
                .currency("EUR")
                .requestedExecutionDate(LocalDate.of(2025, 12, 20))
                .status("PENDING")
                .build();
    }

    @Test
    @DisplayName("execute() should change status from PENDING to EXECUTED")
    void shouldChangeStatusToExecuted() {
        // Arrange
        PaymentOrder executed = pendingOrder.toBuilder()
                .status("EXECUTED")
                .build();
        
        when(paymentOrderRepository.findById("PO-EXEC-001"))
                .thenReturn(Mono.just(Optional.of(pendingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(executed));

        PaymentOrder result = useCase.execute("PO-EXEC-001", "EXEC-2025-0001").block();

        assertNotNull(result);
        assertEquals("EXECUTED", result.getStatus());
    }

    @Test
    @DisplayName("execute() should throw exception when order not found")
    void shouldThrowExceptionWhenOrderNotFound() {
        when(paymentOrderRepository.findById(anyString()))
                .thenReturn(Mono.just(Optional.empty()));

        assertThrows(PaymentOrderNotFoundException.class,
                () -> useCase.execute("NONEXISTENT", "EXEC-001").block());
    }

    @Test
    @DisplayName("execute() should preserve payment order details during execution")
    void shouldPreservePaymentOrderDetails() {
        // Arrange
        PaymentOrder executed = pendingOrder.toBuilder()
                .status("EXECUTED")
                .build();
        
        when(paymentOrderRepository.findById("PO-EXEC-001"))
                .thenReturn(Mono.just(Optional.of(pendingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(executed));

        // Act
        PaymentOrder result = useCase.execute("PO-EXEC-001", "EXEC-2025-0001").block();

        // Assert
        assertEquals("PO-EXEC-001", result.getId());
        assertEquals("EXT-EXEC-001", result.getExternalId());
        assertEquals("ES9121540011270123456789", result.getDebtorIban());
        assertEquals(new BigDecimal("2000.00"), result.getAmount());
    }

    @Test
    @DisplayName("execute() should save executed order to repository")
    void shouldSaveExecutedOrderToRepository() {
        when(paymentOrderRepository.findById("PO-EXEC-001"))
                .thenReturn(Mono.just(Optional.of(pendingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(pendingOrder));

        useCase.execute("PO-EXEC-001", "EXEC-2025-0001").block();

        verify(paymentOrderRepository).save(any(PaymentOrder.class));
    }

    @Test
    @DisplayName("execute() should handle execution reference parameter")
    void shouldHandleExecutionReference() {
        // Arrange
        PaymentOrder executed = pendingOrder.toBuilder()
                .status("EXECUTED")
                .build();
        
        when(paymentOrderRepository.findById("PO-EXEC-001"))
                .thenReturn(Mono.just(Optional.of(pendingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(executed));

        // Act
        PaymentOrder result = useCase.execute("PO-EXEC-001", "EXEC-REF-123").block();

        // Assert
        assertNotNull(result);
        assertEquals("EXECUTED", result.getStatus());
    }

    @Test
    @DisplayName("execute() should mark order as EXECUTED with correct timestamp")
    void shouldMarkOrderAsExecutedWithTimestamp() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        PaymentOrder executed = pendingOrder.toBuilder()
                .status("EXECUTED")
                .lastUpdate(now)
                .build();
        
        when(paymentOrderRepository.findById("PO-EXEC-001"))
                .thenReturn(Mono.just(Optional.of(pendingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(executed));

        // Act
        PaymentOrder result = useCase.execute("PO-EXEC-001", "EXEC-001").block();

        // Assert
        assertEquals("EXECUTED", result.getStatus());
        assertNotNull(result.getLastUpdate());
    }
}
