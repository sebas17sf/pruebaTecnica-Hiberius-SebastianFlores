package com.bank.bankingservice.web;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.application.usecase.CreatePaymentOrderUseCase;
import com.bank.bankingservice.application.usecase.GetPaymentOrderStatusUseCase;
import com.bank.bankingservice.application.usecase.GetPaymentOrderUseCase;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-initiation")
public class PaymentInitiationController {

    private final CreatePaymentOrderUseCase createUseCase;
    private final GetPaymentOrderStatusUseCase statusUseCase;
    private final GetPaymentOrderUseCase getOrderUseCase;
    private final PaymentOrderMapper mapper;

    @PostMapping("/payment-orders")
    public Mono<ResponseEntity<PaymentOrderInitiationResponse>> initiatePaymentOrder(
            @RequestBody PaymentOrderInitiationRequest request) {

        return createUseCase.execute(request)
                .map(response -> ResponseEntity.status(201).body(response));
    }

    @GetMapping("/payment-orders/{paymentOrderId}/status")
    public Mono<ResponseEntity<PaymentOrderStatusResponse>> retrievePaymentOrderStatus(
            @PathVariable String paymentOrderId) {

        return statusUseCase.execute(paymentOrderId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/payment-orders/{paymentOrderId}")
    public Mono<ResponseEntity<com.bank.bankingservice.openapi.model.PaymentOrder>> retrievePaymentOrder(
            @PathVariable String paymentOrderId) {

        return getOrderUseCase.execute(paymentOrderId)
                .map(domain -> mapper.toBian(domain))
                .map(ResponseEntity::ok);
    }}
