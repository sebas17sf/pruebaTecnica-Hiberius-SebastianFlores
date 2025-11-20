# Generación asistida del contrato OpenAPI

La IA generó un borrador del contrato basado en BIAN que incluía:

- POST /payment-initiation/payment-orders
- GET /payment-initiation/payment-orders/{id}
- GET /payment-initiation/payment-orders/{id}/status

También propuso schemas como:
- PaymentOrder
- PaymentInitiationRequest
- PaymentInitiationResponse
- StatusResponse

Se corrigió manualmente:
- Tipos de datos inconsistentes.
- Estructuras no alineadas a BIAN.
- Falta del endpoint "detail" adaptado manualmente.

El resultado fue un OpenAPI 100% contract-first y funcional.
