package com.bank.bankingservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    public JacksonConfig(Jackson2ObjectMapperBuilder builder) {
        builder.modules(
            new JsonNullableModule(),
            new JavaTimeModule()
        );
    }
}
