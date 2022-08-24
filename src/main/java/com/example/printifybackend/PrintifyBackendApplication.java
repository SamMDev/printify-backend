package com.example.printifybackend;

import com.example.printifybackend.auth.Sha256PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PrintifyBackendApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return Converter.getObjectMapper();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new Sha256PasswordEncoder();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(PrintifyBackendApplication.class, args);
    }

}
