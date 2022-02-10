package com.example.printifybackend.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime date;
    private String content;
    private BigDecimal price;
    private Boolean finished;
}
