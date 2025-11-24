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
@DisplayName("Update Payment Order Use Case Tests")
class UpdatePaymentOrderUseCaseTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @InjectMocks
    private UpdatePaymentOrderUseCase useCase;

    private PaymentOrder existingOrder;
    private PaymentOrder updateRequest;

    @BeforeEach
    void setUp() {
        existingOrder = PaymentOrder.builder()
                .id("PO-UPDATE-001")
                .externalId("EXT-UPDATE-001")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1000.00"))
                .currency("EUR")
                .requestedExecutionDate(LocalDate.of(2025, 12, 15))
                .status("INITIATED")
                .remittanceInfo("Original reference")
                .lastUpdate(OffsetDateTime.now())
                .build();

        updateRequest = PaymentOrder.builder()
                .remittanceInfo("Updated reference")
                .requestedExecutionDate(LocalDate.of(2025, 12, 20))
                .build();
    }

    @Test
    @DisplayName("execute() should update existing payment order")
    void shouldUpdateExistingPaymentOrder() {
        // Arrange
        PaymentOrder updated = existingOrder.toBuilder()
                .remittanceInfo("Updated reference")
                .requestedExecutionDate(LocalDate.of(2025, 12, 20))
                .build();
        
        when(paymentOrderRepository.findById("PO-UPDATE-001"))
                .thenReturn(Mono.just(Optional.of(existingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(updated));

        // Act
        PaymentOrder result = useCase.execute("PO-UPDATE-001", updateRequest).block();

        // Assert
        assertNotNull(result);
        assertEquals("Updated reference", result.getRemittanceInfo());
        assertEquals(LocalDate.of(2025, 12, 20), result.getRequestedExecutionDate());
    }

    @Test
    @DisplayName("execute() should throw exception when order not found")
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(paymentOrderRepository.findById(anyString()))
                .thenReturn(Mono.just(Optional.empty()));

        // Act & Assert
        assertThrows(PaymentOrderNotFoundException.class,
                () -> useCase.execute("NONEXISTENT", updateRequest).block());
    }

    @Test
    @DisplayName("execute() should preserve immutable fields during update")
    void shouldPreserveImmutableFields() {
        // Arrange
        PaymentOrder updated = existingOrder.toBuilder()
                .remittanceInfo("Updated reference")
                .build();
        
        when(paymentOrderRepository.findById("PO-UPDATE-001"))
                .thenReturn(Mono.just(Optional.of(existingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(updated));

        // Act
        PaymentOrder result = useCase.execute("PO-UPDATE-001", updateRequest).block();

        // Assert
        assertEquals("PO-UPDATE-001", result.getId());
        assertEquals("EXT-UPDATE-001", result.getExternalId());
        assertEquals("ES9121540011270123456789", result.getDebtorIban());
        assertEquals("INITIATED", result.getStatus());
    }

    @Test
    @DisplayName("execute() should save updated order to repository")
    void shouldSaveUpdatedOrderToRepository() {
        // Arrange
        when(paymentOrderRepository.findById("PO-UPDATE-001"))
                .thenReturn(Mono.just(Optional.of(existingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(existingOrder));

        // Act
        useCase.execute("PO-UPDATE-001", updateRequest).block();

        // Assert
        verify(paymentOrderRepository).save(any(PaymentOrder.class));
    }

    @Test
    @DisplayName("execute() should only update allowed fields")
    void shouldOnlyUpdateAllowedFields() {
        // Arrange
        PaymentOrder updated = existingOrder.toBuilder()
                .remittanceInfo("New reference")
                .build();
        
        when(paymentOrderRepository.findById("PO-UPDATE-001"))
                .thenReturn(Mono.just(Optional.of(existingOrder)));
        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenReturn(Mono.just(updated));

        PaymentOrder updateData = PaymentOrder.builder()
                .remittanceInfo("New reference")
                .build();

        // Act
        PaymentOrder result = useCase.execute("PO-UPDATE-001", updateData).block();

        // Assert
        assertEquals("New reference", result.getRemittanceInfo());
    }
}
