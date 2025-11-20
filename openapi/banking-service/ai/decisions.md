# Decisiones Humanas Durante el Desarrollo

Este documento explica qué partes generadas por la IA fueron corregidas manualmente, así como las decisiones técnicas tomadas.

---

## 1. Ajustes al OpenAPI generado por IA
La IA generó un borrador correcto, pero se modificó manualmente para:
- Ajustarlo exactamente a los ejemplos del Postman entregado.
- Alinear los nombres de campos con el Service Domain BIAN 'Payment Initiation'.
- Corregir referencias circulares en schemas.
- Añadir el endpoint GET detail, que no venía en el contrato original.

---

## 2. Revisión del modelo de dominio
Aunque la IA propuso un modelo base, se mejoraron:
- Restricciones de invariantes de PaymentOrder.
- Simplificación de algunos objetos de valor.
- Eliminación de campos no requeridos en el WSDL original.

---

## 3. Arquitectura Hexagonal
La propuesta de la IA era válida, pero se realizaron mejoras:
- Separación estricta de puertos entrantes/salientes.
- Reordenación de paquetes para mayor claridad.
- Añadido soporte para repositorio en memoria, siguiendo principios DDD.

---

## 4. Simulador SOAP Mock
El código inicial generado por IA fue funcional, pero se cambiaron:
- Tipos de estados.
- Eliminación de temporizadores no necesarios.
- Mejor manejo de idempotencia.

---

## 5. Mapper BIAN
El mapper generado requería correcciones:
- Ajustar nombres exactos exigidos por el OpenAPI.
- Asegurar conversión explícita a ISO-8601.
- Añadir métodos faltantes: toStatus(), toBian().

---

## 6. Pruebas unitarias
Los tests propuestos por IA fueron incompletos:
- Se reescribieron para mayor cobertura.
- Se añadieron mocks del simulador SOAP.
- Se crearon pruebas de integración reales con WebTestClient.

---

## 7. Dockerfile y Docker-compose
La IA generó versiones funcionales, pero se mejoraron:
- Multi-stage con JDK17 + JRE base.
- Manejo de perfiles Spring.
- Variables de entorno más limpias.

---

## 8. Manejo de errores
La IA propuso un handler básico, pero se implementó un sistema más alineado a RFC 7807.

---

## Conclusión
La IA aceleró la fase inicial de construcción, pero todas las piezas críticas fueron validadas, corregidas o reescritas parcialmente para garantizar:
- Alineación con BIAN
- Exactitud del contrato OpenAPI
- Cumplimiento de arquitectura hexagonal
- Código limpio y mantenible
- Fiabilidad de pruebas
