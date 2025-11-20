package com.bank.bankingservice.web;

import com.bank.bankingservice.application.mapper.PaymentOrderMapper;
import com.bank.bankingservice.application.usecase.CreatePaymentOrderUseCase;
import com.bank.bankingservice.application.usecase.GetPaymentOrderStatusUseCase;
import com.bank.bankingservice.application.usecase.GetPaymentOrderUseCase;
import com.bank.bankingservice.openapi.api.PaymentInitiationApi;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderInitiationResponse;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentInitiationController implements PaymentInitiationApi {

    private final CreatePaymentOrderUseCase createUseCase;
    private final GetPaymentOrderStatusUseCase statusUseCase;
    private final GetPaymentOrderUseCase getOrderUseCase;
    private final PaymentOrderMapper mapper;

    @Override
    public ResponseEntity<PaymentOrderInitiationResponse> initiatePaymentOrder(
            PaymentOrderInitiationRequest request) {

        PaymentOrderInitiationResponse response = createUseCase.execute(request);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PaymentOrderStatusResponse> retrievePaymentOrderStatus(
            String paymentOrderId) {

        PaymentOrderStatusResponse response = statusUseCase.execute(paymentOrderId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<com.bank.bankingservice.openapi.model.PaymentOrder> retrievePaymentOrder(
            String paymentOrderId) {

        com.bank.bankingservice.domain.model.PaymentOrder domain = 
            getOrderUseCase.execute(paymentOrderId);

        com.bank.bankingservice.openapi.model.PaymentOrder dto = 
            mapper.toBian(domain);

        return ResponseEntity.ok(dto);
    }

}
