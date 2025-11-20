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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Get Payment Order Use Case Tests")
class GetPaymentOrderUseCaseTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @InjectMocks
    private GetPaymentOrderUseCase useCase;

    private PaymentOrder testPaymentOrder;

    @BeforeEach
    void setUp() {
        testPaymentOrder = PaymentOrder.builder()
                .id("PO-GET-001")
                .externalId("EXT-GET-001")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1500.50"))
                .currency("EUR")
                .requestedExecutionDate(LocalDate.of(2025, 12, 20))
                .status("INITIATED")
                .remittanceInfo("Test payment order")
                .build();
    }

    @Test
    @DisplayName("execute() should retrieve payment order by ID")
    void shouldRetrievePaymentOrderById() {
        // Arrange
        when(paymentOrderRepository.findById("PO-GET-001"))
                .thenReturn(Optional.of(testPaymentOrder));

        // Act
        PaymentOrder result = useCase.execute("PO-GET-001");

        // Assert
        assertNotNull(result);
        assertEquals("PO-GET-001", result.getId());
        verify(paymentOrderRepository).findById("PO-GET-001");
    }

    @Test
    @DisplayName("execute() should throw exception when order not found")
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(paymentOrderRepository.findById("NONEXISTENT"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PaymentOrderNotFoundException.class,
                () -> useCase.execute("NONEXISTENT"));
    }

    @Test
    @DisplayName("execute() should return all payment order details")
    void shouldReturnAllPaymentOrderDetails() {
        // Arrange
        when(paymentOrderRepository.findById("PO-GET-001"))
                .thenReturn(Optional.of(testPaymentOrder));

        // Act
        PaymentOrder result = useCase.execute("PO-GET-001");

        // Assert
        assertEquals("EXT-GET-001", result.getExternalId());
        assertEquals("ES9121540011270123456789", result.getDebtorIban());
        assertEquals("FR1420041010050500013M02606", result.getCreditorIban());
        assertEquals(new BigDecimal("1500.50"), result.getAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals("Test payment order", result.getRemittanceInfo());
        assertEquals("INITIATED", result.getStatus());
    }

    @Test
    @DisplayName("execute() should handle valid ID format")
    void shouldHandleValidIdFormat() {
        // Arrange
        when(paymentOrderRepository.findById("PO-GET-001"))
                .thenReturn(Optional.of(testPaymentOrder));

        // Act
        PaymentOrder result = useCase.execute("PO-GET-001");

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("execute() should preserve INITIATED status")
    void shouldPreserveInitiatedStatus() {
        // Arrange
        when(paymentOrderRepository.findById("PO-GET-001"))
                .thenReturn(Optional.of(testPaymentOrder));

        // Act
        PaymentOrder result = useCase.execute("PO-GET-001");

        // Assert
        assertEquals("INITIATED", result.getStatus());
    }

    @Test
    @DisplayName("execute() should preserve requested execution date")
    void shouldPreserveRequestedExecutionDate() {
        // Arrange
        when(paymentOrderRepository.findById("PO-GET-001"))
                .thenReturn(Optional.of(testPaymentOrder));

        // Act
        PaymentOrder result = useCase.execute("PO-GET-001");

        // Assert
        assertEquals(LocalDate.of(2025, 12, 20), result.getRequestedExecutionDate());
    }
}
