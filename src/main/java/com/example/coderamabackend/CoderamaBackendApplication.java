package com.example.coderamabackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoderamaBackendApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return Converter.getObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(CoderamaBackendApplication.class, args);
    }

}
