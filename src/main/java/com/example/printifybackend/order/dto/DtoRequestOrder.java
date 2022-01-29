package com.example.printifybackend.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DtoRequestOrder {

    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private String postCode;
    private String phone;
    private String content;
    private BigDecimal price;

}
