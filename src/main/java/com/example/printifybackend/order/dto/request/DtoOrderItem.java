package com.example.printifybackend.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class DtoOrderItem {
    private Integer amount;
    private String itemUuid;
    private BigDecimal price;
}
