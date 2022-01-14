package com.example.coderamabackend.rate.dto;

import com.example.coderamabackend.greenlight.GreenlightStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DtoRate {
    private Long id;
    private BigDecimal price;
    private String who;
    private LocalDate from;
    private LocalDate to;
    private Long budgetItemId;
    private String upgrade;
    private GreenlightStatus greenlightStatus;
}
