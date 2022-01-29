package com.example.printifybackend.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DtoOrder {
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private String postCode;
    private String phone;
    private LocalDateTime date;
    private String content;
    private BigDecimal price;
}
