package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdatePaymentOrderUseCase {

    private final PaymentOrderRepository paymentOrderRepository;

    public UpdatePaymentOrderUseCase(PaymentOrderRepository paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    public PaymentOrder execute(String paymentOrderId, PaymentOrder updateData) {
        PaymentOrder existing = paymentOrderRepository.findById(paymentOrderId)
                .orElseThrow(() -> new PaymentOrderNotFoundException(
                        "Payment order not found: " + paymentOrderId
                ));

        // Update only allowed fields for INITIATED status orders
        if ("INITIATED".equals(existing.getStatus())) {
            PaymentOrder updated = existing.toBuilder()
                    .remittanceInfo(updateData.getRemittanceInfo() != null 
                            ? updateData.getRemittanceInfo() 
                            : existing.getRemittanceInfo())
                    .requestedExecutionDate(updateData.getRequestedExecutionDate() != null 
                            ? updateData.getRequestedExecutionDate() 
                            : existing.getRequestedExecutionDate())
                    .build();
            
            return paymentOrderRepository.save(updated);
        }

        return existing;
    }
}
