package com.bank.bankingservice.infrastructure.persistence;

import com.bank.bankingservice.domain.model.PaymentOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemory Payment Order Repository Tests")
class InMemoryPaymentOrderRepositoryTest {

    private InMemoryPaymentOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPaymentOrderRepository();
    }

    @Test
    @DisplayName("save() should persist payment order and return it")
    void shouldSavePaymentOrder() {
        PaymentOrder order = PaymentOrder.builder()
                .id("PO-001")
                .externalId("EXT-001")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1000.00"))
                .currency("EUR")
                .status("INITIATED")
                .build();

        PaymentOrder saved = repository.save(order).block();

        assertNotNull(saved);
        assertEquals("PO-001", saved.getId());
        assertEquals("INITIATED", saved.getStatus());
    }

    @Test
    @DisplayName("findById() should retrieve saved payment order")
    void shouldFindPaymentOrderById() {
        PaymentOrder order = PaymentOrder.builder()
                .id("PO-002")
                .externalId("EXT-002")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1000.00"))
                .currency("EUR")
                .status("PENDING")
                .build();
        repository.save(order).block();

        Optional<PaymentOrder> found = repository.findById("PO-002").block();

        assertTrue(found.isPresent());
        assertEquals("PO-002", found.get().getId());
    }

    @Test
    @DisplayName("findById() should return empty Optional when order not exists")
    void shouldReturnEmptyWhenNotFound() {
        Optional<PaymentOrder> found = repository.findById("NONEXISTENT").block();

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("save() should persist multiple orders independently")
    void shouldSaveMultipleOrdersIndependently() {
        PaymentOrder order1 = PaymentOrder.builder()
                .id("PO-003")
                .externalId("EXT-003")
                .amount(new BigDecimal("100.00"))
                .currency("EUR")
                .status("INITIATED")
                .build();
        PaymentOrder order2 = PaymentOrder.builder()
                .id("PO-004")
                .externalId("EXT-004")
                .amount(new BigDecimal("200.00"))
                .currency("EUR")
                .status("PENDING")
                .build();

        repository.save(order1).block();
        repository.save(order2).block();

        Optional<PaymentOrder> found1 = repository.findById("PO-003").block();
        Optional<PaymentOrder> found2 = repository.findById("PO-004").block();

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertEquals("PO-003", found1.get().getId());
        assertEquals("PO-004", found2.get().getId());
    }

    @Test
    @DisplayName("save() should overwrite existing order with same id")
    void shouldOverwriteExistingOrder() {
        PaymentOrder order1 = PaymentOrder.builder()
                .id("PO-005")
                .status("INITIATED")
                .build();
        PaymentOrder order2 = PaymentOrder.builder()
                .id("PO-005")
                .status("PENDING")
                .build();

        repository.save(order1).block();
        repository.save(order2).block();

        Optional<PaymentOrder> found = repository.findById("PO-005").block();

        assertTrue(found.isPresent());
        assertEquals("PENDING", found.get().getStatus());
    }

    @Test
    @DisplayName("findById() should return same instance for same id")
    void shouldReturnConsistentResults() {
        PaymentOrder original = PaymentOrder.builder()
                .id("PO-006")
                .externalId("EXT-006")
                .debtorIban("ES9121540011270123456789")
                .creditorIban("FR1420041010050500013M02606")
                .amount(new BigDecimal("1000.00"))
                .currency("EUR")
                .status("INITIATED")
                .build();
        repository.save(original).block();

        Optional<PaymentOrder> found1 = repository.findById("PO-006").block();
        Optional<PaymentOrder> found2 = repository.findById("PO-006").block();

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertEquals(found1.get().getId(), found2.get().getId());
        assertEquals(found1.get().getStatus(), found2.get().getStatus());
    }

    @Test
    @DisplayName("save() should handle concurrent saves")
    void shouldHandleConcurrentSaves() {
        PaymentOrder order = PaymentOrder.builder()
                .id("PO-007")
                .status("INITIATED")
                .build();

        PaymentOrder saved1 = repository.save(order).block();
        PaymentOrder saved2 = repository.save(order).block();

        assertNotNull(saved1);
        assertNotNull(saved2);
        assertEquals("PO-007", saved1.getId());
        assertEquals("PO-007", saved2.getId());
    }
}
