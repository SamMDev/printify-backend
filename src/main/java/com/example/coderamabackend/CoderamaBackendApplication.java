package com.example.coderamabackend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoderamaBackendApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return DtoConverter.getObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoderamaBackendApplication.class, args);
    }

}
