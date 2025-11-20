# Generación asistida del simulador SOAP

El simulador generado por IA implementaba una máquina de estados simple:
INITIATED → PROCESSING → EXECUTED

Se refinó para:
- Evitar ciclos infinitos.
- Soportar idempotencia.
- Integrarse con el repositorio en memoria.
- Mantener consistencia en los códigos BIAN.
