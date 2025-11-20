# Prompts Utilizados Durante el Desarrollo

Este documento recopila los prompts empleados como asistencia de IA durante la construcción del microservicio solicitado en la prueba técnica.  
Se incluye además un resumen de las respuestas recibidas y su impacto en el desarrollo.

---

## 1. Análisis del WSDL legado (SOAP)
**Prompt:**  
"Resume el WSDL de PaymentsOrder SOAP, identifica operaciones, estados del proceso y campos principales para migrar a REST alineado con BIAN."

**Resumen de respuesta:**  
La IA identificó las operaciones existentes, los estados INITIATED → PROCESSING → EXECUTED, así como los campos relevantes para PaymentOrder.

**Uso:**  
Este resumen se empleó para definir el modelo de dominio inicial y la estructura del mapper hacia BIAN.

---

## 2. Generación del borrador del OpenAPI (contract-first)
**Prompt:**  
"Genera el OpenAPI 3.0 contract-first para Payment Initiation conforme a BIAN, incluyendo endpoints POST y GET para órdenes de pago y estatus."

**Resumen:**  
Generó un borrador muy cercano al definitivo, con recursos, request/response y estructuras alineadas a PaymentOrder BQ de BIAN.

**Uso:**  
Se tomó este borrador como base y se ajustó manualmente para cumplir estrictamente con BIAN y con la colección Postman entregada.

---

## 3. Diseño de arquitectura hexagonal
**Prompt:**  
"Propón una arquitectura hexagonal para un microservicio Java 17 / Spring Boot 3 con dominios, casos de uso, puertos y adaptadores."

**Resumen:**  
La IA generó una estructura clara con Domain, Application, Infrastructure, Web. Propuso interfaces, puertos y clases.

**Uso:**  
Se creó la estructura exacta del proyecto tomando como referencia este diseño.

---

## 4. Simulador SOAP Mock
**Prompt:**  
"Genera un simulador SOAP mock que avance estados de INITIATED a PROCESSING a EXECUTED sin invocar SOAP real."

**Resumen:**  
Generó una clase que mantiene un ciclo fijo entre estados y permite simular latencia y transiciones progresivas.

**Uso:**  
Se implementó directamente con mejoras para garantizar idempotencia y evitar estados inválidos.

---

## 5. Mapper BIAN
**Prompt:**  
"Genera un mapper desde dominio hacia los objetos BIAN (PaymentOrder), incluyendo toDomain, toInitiationResponse, toStatus y toBian."

**Resumen:**  
Generó estructuras compatibles, aunque hubo que ajustar algunos nombres para adaptarse al OpenAPI final.

**Uso:**  
El mapper final se basó en el código generado y luego se refinó manualmente.

---

## 6. Generación de tests
**Prompt:**  
"Genera casos de prueba unitarios para dominio, mapper, repositorio, simulador SOAP y controladores, usando JUnit 5 + Mockito."

**Resumen:**  
La IA proporcionó pruebas base que posteriormente se adaptaron al caso real.

**Uso:**  
Fueron punto de partida para las pruebas unitarias e integración.

---

## 7. JaCoCo Coverage Configuration
**Prompt:**  
"Configura JaCoCo en Maven para enforcar 80% mínimo de coverage en capas de aplicación e infraestructura, excluyendo generated code."

**Resumen:**  
La IA generó la configuración de pom.xml con reglas específicas de coverage y exclusiones.

**Uso:**  
Se implementó en pom.xml para garantizar 80% de cobertura verificado en cada build.

---

## 8. Docker Multi-stage
**Prompt:**  
"Genera un Dockerfile multi-stage con JDK17 para build y JRE17-slim para runtime, optimizando tamaño de imagen."

**Resumen:**  
La IA proporcionó estructura clara de dos stages con nombres y best practices.

**Uso:**  
Se adaptó para el proyecto específico y se incluye docker-compose.yml.

---

## 9. RFC 7807 Error Handling
**Prompt:**  
"Diseña un GlobalExceptionHandler que implemente RFC 7807 Problem Details para respuestas de error."

**Resumen:**  
La IA generó estructura con fields: type, title, status, detail, instance.

**Uso:**  
El framework está listo en la clase GlobalExceptionHandler para ser completado.

---

## 10. Checkstyle y SpotBugs Configuration
**Prompt:**  
"Configura Checkstyle con Google Style Guide y SpotBugs para detectar bugs comunes en Maven."

**Resumen:**  
La IA generó checkstyle.xml y spotbugs-exclude.xml con configuraciones sensatas.

**Uso:**  
Se incluyeron en el proyecto como archivos de configuración para QA.

---

## Conclusión sobre el Uso de IA

La IA fue utilizada como **acelerador de desarrollo** en fases iniciales:
- ✅ Generó bodradores que fueron validados y refinados
- ✅ Proporcionó estructuras base que se adaptaron al caso específico
- ✅ Aceleró decisiones de arquitectura y configuración
- ✅ Sugirió mejores prácticas que se aplicaron manualmente

**Resultado final:**
Todas las piezas críticas fueron validadas, corregidas o reescritas parcialmente para garantizar:
- Alineación con BIAN v2.0
- Exactitud del contrato OpenAPI
- Cumplimiento de arquitectura hexagonal
- Código limpio y mantenible
- Fiabilidad de pruebas (39 tests, 80% coverage)
---

## 3. Diseño de arquitectura hexagonal
**Prompt:**  
"Propón una arquitectura hexagonal para un microservicio Java 17 / Spring Boot 3 con dominios, casos de uso, puertos y adaptadores."

**Resumen:**  
La IA generó una estructura clara con Domain, Application, Infrastructure, Web. Propuso interfaces, puertos y clases.

**Uso:**  
Se creó la estructura exacta del proyecto tomando como referencia este diseño.
