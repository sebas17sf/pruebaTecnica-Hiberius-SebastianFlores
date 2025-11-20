# Instrucciones para Implementar v2.0 - BIAN Payment Initiation

## 🚀 Guía de Implementación de Mejoras

Basado en el análisis BIAN realizado, aquí están las instrucciones paso a paso para implementar la versión 2.0 mejorada.

---

## Fase 1: Preparación (Día 1)

### 1.1 Revisar Documentación

```bash
Archivos a revisar en: d:\Proyectos\pruebatecnica\openapi\

1. RESUMEN_EJECUTIVO.md
   └─ Comprensión general del proyecto y alineamiento BIAN

2. BIAN_ALIGNMENT_REPORT.md
   └─ Análisis detallado de lo que está bien

3. BIAN_MAPPING_DETAILED.md
   └─ Mapeo completo BIAN → Implementación

4. COMPARATIVA_VISUAL.md
   └─ Cambios v1.0 → v2.0 lado a lado

5. payment-initiation-enhanced-v2.0.yaml
   └─ Contrato OpenAPI v2.0 completo
```

### 1.2 Generar Código desde OpenAPI v2.0

```bash
cd d:\Proyectos\pruebatecnica\openapi

# Generar código Spring Boot desde payment-initiation-enhanced-v2.0.yaml
mvn clean generate-sources \
  -Dorg.openapitools.codegen.languages.SpringCodegen \
  -Dinput=payment-initiation-enhanced-v2.0.yaml \
  -Doutput=banking-service
```

O usando OpenAPI Generator CLI:

```bash
npx @openapitools/openapi-generator-cli generate \
  -i payment-initiation-enhanced-v2.0.yaml \
  -g spring \
  -o banking-service/generated
```

---

## Fase 2: Implementar Operaciones Faltantes (Días 2-4)

### 2.1 Implementar UPDATE Pattern (PATCH)

#### Paso 1: Crear DTO de actualización

```java
// Ubicación: banking-service/src/main/java/com/bank/bankingservice/
//            openapi/model/PaymentOrderUpdateRequest.java

package com.bank.bankingservice.openapi.model;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

public class PaymentOrderUpdateRequest {
    
    @Size(max = 140)
    private String remittanceInfo;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate requestedExecutionDate;
    
    // Getters y Setters
    public String getRemittanceInfo() { return remittanceInfo; }
    public void setRemittanceInfo(String remittanceInfo) { 
        this.remittanceInfo = remittanceInfo; 
    }
    
    public LocalDate getRequestedExecutionDate() { 
        return requestedExecutionDate; 
    }
    public void setRequestedExecutionDate(LocalDate requestedExecutionDate) { 
        this.requestedExecutionDate = requestedExecutionDate; 
    }
}
```

#### Paso 2: Crear Use Case de actualización

```java
// Ubicación: banking-service/src/main/java/com/bank/bankingservice/
//            application/usecase/UpdatePaymentOrderUseCase.java

package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.openapi.model.PaymentOrderUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UpdatePaymentOrderUseCase {
    
    private final PaymentOrderRepository repository;
    
    public PaymentOrder execute(String paymentOrderId, 
                               PaymentOrderUpdateRequest request) 
            throws PaymentOrderNotFoundException {
        
        // Obtener orden existente
        PaymentOrder paymentOrder = repository.findById(paymentOrderId)
            .orElseThrow(() -> new PaymentOrderNotFoundException(
                "Payment order " + paymentOrderId + " not found"));
        
        // Validación: Solo se pueden actualizar órdenes INITIATED
        if (!"INITIATED".equals(paymentOrder.getStatus())) {
            throw new IllegalStateException(
                "Cannot update payment order in " + paymentOrder.getStatus() 
                + " state");
        }
        
        // Actualizar campos permitidos
        if (request.getRemittanceInfo() != null) {
            paymentOrder.setRemittanceInfo(request.getRemittanceInfo());
        }
        
        if (request.getRequestedExecutionDate() != null) {
            // Validar que sea fecha futura
            if (request.getRequestedExecutionDate()
                .isBefore(java.time.LocalDate.now())) {
                throw new IllegalArgumentException(
                    "Execution date cannot be in the past");
            }
            paymentOrder.setRequestedExecutionDate(
                request.getRequestedExecutionDate());
        }
        
        // Actualizar timestamp
        paymentOrder.setLastUpdate(OffsetDateTime.now());
        
        // Persistir cambios
        return repository.update(paymentOrder);
    }
}
```

#### Paso 3: Agregar método en el Controller

```java
// En: banking-service/src/main/java/com/bank/bankingservice/
//     web/PaymentInitiationController.java

// Agregar a la clase PaymentInitiationController:

@Override
public ResponseEntity<PaymentOrder> updatePaymentOrder(
        String paymentOrderId,
        PaymentOrderUpdateRequest request) {
    try {
        var response = updateUseCase.execute(paymentOrderId, request);
        return ResponseEntity.ok(response);
    } catch (PaymentOrderNotFoundException ex) {
        return ResponseEntity.notFound().build();
    } catch (IllegalStateException ex) {
        return ResponseEntity.status(409).build(); // Conflict
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.status(422).build(); // Unprocessable
    }
}

// Agregar inyección de dependencia en Constructor:
private final UpdatePaymentOrderUseCase updateUseCase;
```

### 2.2 Implementar EXECUTE Pattern (POST)

#### Paso 1: Crear DTO de ejecución

```java
// Ubicación: banking-service/src/main/java/com/bank/bankingservice/
//            openapi/model/PaymentOrderExecuteRequest.java

package com.bank.bankingservice.openapi.model;

import jakarta.validation.constraints.*;

public class PaymentOrderExecuteRequest {
    
    @Size(max = 50)
    private String executionReference;
    
    public String getExecutionReference() { return executionReference; }
    public void setExecutionReference(String executionReference) { 
        this.executionReference = executionReference; 
    }
}
```

#### Paso 2: Crear Use Case de ejecución

```java
// Ubicación: banking-service/src/main/java/com/bank/bankingservice/
//            application/usecase/ExecutePaymentOrderUseCase.java

package com.bank.bankingservice.application.usecase;

import com.bank.bankingservice.domain.model.PaymentOrder;
import com.bank.bankingservice.domain.repository.PaymentOrderRepository;
import com.bank.bankingservice.domain.exception.PaymentOrderNotFoundException;
import com.bank.bankingservice.openapi.model.PaymentOrderExecuteRequest;
import com.bank.bankingservice.openapi.model.PaymentOrderStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ExecutePaymentOrderUseCase {
    
    private final PaymentOrderRepository repository;
    
    public PaymentOrderStatusResponse execute(
            String paymentOrderId,
            PaymentOrderExecuteRequest request)
            throws PaymentOrderNotFoundException {
        
        // Obtener orden existente
        PaymentOrder paymentOrder = repository.findById(paymentOrderId)
            .orElseThrow(() -> new PaymentOrderNotFoundException(
                "Payment order " + paymentOrderId + " not found"));
        
        // Validación: Solo se pueden ejecutar órdenes PENDING
        if (!"PENDING".equals(paymentOrder.getStatus())) {
            throw new IllegalStateException(
                "Cannot execute payment order in " + 
                paymentOrder.getStatus() + " state");
        }
        
        // Validar fecha de ejecución
        if (paymentOrder.getRequestedExecutionDate()
            .isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Execution date is in the past");
        }
        
        // Cambiar estado a EXECUTED
        paymentOrder.setStatus("EXECUTED");
        paymentOrder.setLastUpdate(OffsetDateTime.now());
        
        // Persistir cambios
        repository.update(paymentOrder);
        
        // Crear response
        PaymentOrderStatusResponse response = 
            new PaymentOrderStatusResponse();
        response.setPaymentOrderId(paymentOrder.getId());
        response.setStatus("EXECUTED");
        response.setLastUpdate(paymentOrder.getLastUpdate());
        response.setStatusReason("Successfully executed");
        response.setExecutionDate(LocalDate.now());
        
        return response;
    }
}
```

#### Paso 3: Agregar método en el Controller

```java
// En: banking-service/src/main/java/com/bank/bankingservice/
//     web/PaymentInitiationController.java

// Agregar a la clase:

@Override
public ResponseEntity<PaymentOrderStatusResponse> executePaymentOrder(
        String paymentOrderId,
        PaymentOrderExecuteRequest request) {
    try {
        var response = executeUseCase.execute(paymentOrderId, request);
        return ResponseEntity.ok(response);
    } catch (PaymentOrderNotFoundException ex) {
        return ResponseEntity.notFound().build();
    } catch (IllegalStateException ex) {
        return ResponseEntity.status(409).build(); // Conflict
    } catch (IllegalArgumentException ex) {
        return ResponseEntity.status(400).build(); // Bad Request
    }
}

// Agregar inyección en Constructor:
private final ExecutePaymentOrderUseCase executeUseCase;
```

### 2.3 Implementar LIST con Filtrado (GET)

#### Paso 1: Extender Repository para búsqueda

```java
// En: banking-service/src/main/java/com/bank/bankingservice/
//     domain/repository/PaymentOrderRepository.java

package com.bank.bankingservice.domain.repository;

import com.bank.bankingservice.domain.model.PaymentOrder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository {
    
    PaymentOrder save(PaymentOrder paymentOrder);
    Optional<PaymentOrder> findById(String id);
    PaymentOrder update(PaymentOrder paymentOrder);
    
    // Nuevos métodos para búsqueda
    List<PaymentOrder> findAll(
        String status,
        String debtorIban,
        String creditorIban,
        LocalDate dateFrom,
        LocalDate dateTo,
        int limit,
        int offset);
    
    long count(
        String status,
        String debtorIban,
        String creditorIban,
        LocalDate dateFrom,
        LocalDate dateTo);
}
```

#### Paso 2: Implementar búsqueda en InMemoryRepository

```java
// En: banking-service/src/main/java/com/bank/bankingservice/
//     infrastructure/persistence/InMemoryPaymentOrderRepository.java

@Override
public List<PaymentOrder> findAll(
        String status,
        String debtorIban,
        String creditorIban,
        LocalDate dateFrom,
        LocalDate dateTo,
        int limit,
        int offset) {
    
    return database.values().stream()
        .filter(p -> status == null || p.getStatus().equals(status))
        .filter(p -> debtorIban == null || 
                     p.getDebtorIban().equals(debtorIban))
        .filter(p -> creditorIban == null || 
                     p.getCreditorIban().equals(creditorIban))
        .filter(p -> dateFrom == null || 
                     !p.getRequestedExecutionDate().isBefore(dateFrom))
        .filter(p -> dateTo == null || 
                     !p.getRequestedExecutionDate().isAfter(dateTo))
        .skip(offset)
        .limit(limit)
        .toList();
}

@Override
public long count(
        String status,
        String debtorIban,
        String creditorIban,
        LocalDate dateFrom,
        LocalDate dateTo) {
    
    return database.values().stream()
        .filter(p -> status == null || p.getStatus().equals(status))
        .filter(p -> debtorIban == null || 
                     p.getDebtorIban().equals(debtorIban))
        .filter(p -> creditorIban == null || 
                     p.getCreditorIban().equals(creditorIban))
        .filter(p -> dateFrom == null || 
                     !p.getRequestedExecutionDate().isBefore(dateFrom))
        .filter(p -> dateTo == null || 
                     !p.getRequestedExecutionDate().isAfter(dateTo))
        .count();
}
```

#### Paso 3: Agregar método en Controller

```java
// En: banking-service/src/main/java/com/bank/bankingservice/
//     web/PaymentInitiationController.java

@Override
public ResponseEntity<List<PaymentOrder>> listPaymentOrders(
        String status,
        String debtorIban,
        String creditorIban,
        LocalDate dateFrom,
        LocalDate dateTo,
        Integer limit,
        Integer offset) {
    
    // Valores por defecto
    int pageLimit = limit != null ? Math.min(limit, 100) : 20;
    int pageOffset = offset != null ? offset : 0;
    
    List<PaymentOrder> results = paymentOrderRepository.findAll(
        status, debtorIban, creditorIban, 
        dateFrom, dateTo, pageLimit, pageOffset);
    
    long total = paymentOrderRepository.count(
        status, debtorIban, creditorIban, 
        dateFrom, dateTo);
    
    long totalPages = (total + pageLimit - 1) / pageLimit;
    
    return ResponseEntity.ok()
        .header("X-Total-Count", String.valueOf(total))
        .header("X-Page-Count", String.valueOf(totalPages))
        .body(results);
}
```

---

## Fase 3: Agregar Seguridad (Día 5)

### 3.1 Configurar JWT Security

Agregar dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### 3.2 Crear Configuración de Seguridad

```java
// Ubicación: banking-service/src/main/java/com/bank/bankingservice/
//            infrastructure/config/SecurityConfig.java

package com.bank.bankingservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/payment-initiation/**").authenticated()
                .antMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            .and()
            .httpBasic();
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = 
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## Fase 4: Testing (Días 6-7)

### 4.1 Tests para UPDATE

```java
@Test
void testUpdatePaymentOrder_Success() {
    // Arrange
    PaymentOrder order = createTestPaymentOrder("INITIATED");
    repository.save(order);
    
    PaymentOrderUpdateRequest request = new PaymentOrderUpdateRequest();
    request.setRemittanceInfo("Updated info");
    
    // Act
    PaymentOrder result = updateUseCase.execute(order.getId(), request);
    
    // Assert
    assertEquals("Updated info", result.getRemittanceInfo());
}

@Test
void testUpdatePaymentOrder_InvalidState() {
    // Arrange
    PaymentOrder order = createTestPaymentOrder("EXECUTED");
    repository.save(order);
    
    PaymentOrderUpdateRequest request = new PaymentOrderUpdateRequest();
    
    // Act & Assert
    assertThrows(IllegalStateException.class, () ->
        updateUseCase.execute(order.getId(), request));
}
```

### 4.2 Tests para EXECUTE

```java
@Test
void testExecutePaymentOrder_Success() {
    // Arrange
    PaymentOrder order = createTestPaymentOrder("PENDING");
    repository.save(order);
    
    // Act
    PaymentOrderStatusResponse result = 
        executeUseCase.execute(order.getId(), new PaymentOrderExecuteRequest());
    
    // Assert
    assertEquals("EXECUTED", result.getStatus());
}
```

### 4.3 Tests para LIST

```java
@Test
void testListPaymentOrders_WithFilters() {
    // Arrange
    createMultiplePaymentOrders(10);
    
    // Act
    List<PaymentOrder> results = repository.findAll(
        "PENDING", null, null, null, null, 20, 0);
    
    // Assert
    assertTrue(results.size() <= 10);
}
```

---

## 📋 Checklist de Implementación

```
FASE 1: PREPARACIÓN
[ ] Revisar RESUMEN_EJECUTIVO.md
[ ] Revisar payment-initiation-enhanced-v2.0.yaml
[ ] Entender la arquitectura actual

FASE 2: OPERACIONES
[ ] Crear PaymentOrderUpdateRequest
[ ] Crear UpdatePaymentOrderUseCase
[ ] Agregar updatePaymentOrder() en Controller
[ ] Crear PaymentOrderExecuteRequest
[ ] Crear ExecutePaymentOrderUseCase
[ ] Agregar executePaymentOrder() en Controller
[ ] Extender Repository con findAll() y count()
[ ] Implementar en InMemoryRepository
[ ] Agregar listPaymentOrders() en Controller

FASE 3: SEGURIDAD
[ ] Agregar dependencia JWT en pom.xml
[ ] Crear SecurityConfig
[ ] Configurar JWT Token Validation
[ ] Actualizar OpenAPI con securitySchemes

FASE 4: TESTING
[ ] Tests para UPDATE
[ ] Tests para EXECUTE
[ ] Tests para LIST
[ ] Tests de integración
[ ] Tests de seguridad

FASE 5: DOCUMENTACIÓN
[ ] Actualizar README.md
[ ] Agregar ejemplos en Swagger
[ ] Actualizar Postman collection
[ ] Documentar cambios en CHANGELOG.md

FASE 6: DEPLOYMENT
[ ] Ejecutar pruebas completas
[ ] Code review
[ ] Merge a main
[ ] Build final
[ ] Deployment a staging
```

---

## ⏱️ Cronograma Estimado

| Fase | Tarea | Duración | Fechas |
|------|-------|----------|--------|
| 1 | Preparación | 0.5 días | Día 1 mañana |
| 2 | Operaciones CRUD | 2.5 días | Días 1-3 |
| 3 | Seguridad | 1 día | Día 4 |
| 4 | Testing | 1.5 días | Días 5-6 |
| 5 | Documentación | 1 día | Día 7 mañana |
| 6 | Review & Deploy | 0.5 días | Día 7 tarde |
| | **TOTAL** | **7 días** | 1 semana |

---

## 📚 Recursos Adicionales

1. **Documento BIAN Mapping**: `BIAN_MAPPING_DETAILED.md`
2. **OpenAPI Spec v2.0**: `payment-initiation-enhanced-v2.0.yaml`
3. **Report Alineamiento**: `BIAN_ALIGNMENT_REPORT.md`
4. **Comparativa**: `COMPARATIVA_VISUAL.md`

---

**Documento preparado por**: Technical Implementation Guide  
**Fecha**: 2025-11-19  
**Versión**: 1.0
