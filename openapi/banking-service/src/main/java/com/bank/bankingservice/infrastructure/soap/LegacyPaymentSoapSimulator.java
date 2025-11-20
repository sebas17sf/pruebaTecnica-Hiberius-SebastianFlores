package com.bank.bankingservice.infrastructure.soap;

import org.springframework.stereotype.Component;

@Component
public class LegacyPaymentSoapSimulator {

    /**
     * Simula la transición de estados del sistema Legacy.
     */
    public String nextStatus(String current) {

        if (current == null) return "INITIATED";

        return switch (current) {
            case "INITIATED" -> "PROCESSING";
            case "PROCESSING" -> "EXECUTED";
            default -> "EXECUTED";
        };
    }
}
